package org.ilmi.eposkuserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ilmi.eposkuserver.domain.base.Aggregate;
import org.ilmi.eposkuserver.domain.entity.PergerakanStok;
import org.ilmi.eposkuserver.domain.entity.Transaksi;
import org.ilmi.eposkuserver.exception.PergerakanStokNotFoundException;
import org.ilmi.eposkuserver.exception.TransaksiNotFoundException;
import org.ilmi.eposkuserver.output.persistence.entity.aggregate.UserUploadedFile;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produk extends Aggregate {
    private String nama;
    @Nullable private String deskripsi;
    private Double harga;
    @Nullable private String imageUrl;
    private List<PergerakanStok> pergerakanStok = new ArrayList<>();
    private List<Transaksi> transaksi = new ArrayList<>();

    public void addPergerakanStok(PergerakanStok stok) {
        this.pergerakanStok.add(stok);
    }

    public void addTransaksi(Transaksi transaksi) {

        this.transaksi.add(transaksi);
    }

    public Integer getStokSaatIni() {
        if (pergerakanStok == null || pergerakanStok.isEmpty()) {
            return 0;
        }

        return pergerakanStok.stream()
                .mapToInt(ps -> ps.getJumlahMasuk() - ps.getJumlahKeluar())
                .sum();
    }

    public Integer getTotalTransaksi() {
        if (transaksi == null || transaksi.isEmpty()) {
            return 0;
        }

        return transaksi.size();
    }

    public Double getTotalPendapatan() {
        if (transaksi == null || transaksi.isEmpty()) {
            return 0.0;
        }

        return transaksi.stream()
                .mapToDouble(t -> (t.getJumlah() * this.harga) * (1 - t.getDiskon() / 100))
                .sum();
    }

    public void restokProduk(Integer jumlahMasuk, Integer jumlahKeluar) {
        var pergerakanStok = new PergerakanStok();
        pergerakanStok.setJumlahMasuk(jumlahMasuk == null ? 0 : jumlahMasuk);
        pergerakanStok.setJumlahKeluar(jumlahKeluar == null ? 0 : jumlahKeluar);
        pergerakanStok.setTanggal(LocalDate.now());

        this.addPergerakanStok(pergerakanStok);
    }

    public void tambahStok(Integer jumlahMasuk) {
        restokProduk(jumlahMasuk == null ? 0 : jumlahMasuk, 0);
    }

    public void kurangiStok(Integer jumlahKeluar) {
        restokProduk(0, jumlahKeluar == null ? 0 : jumlahKeluar);
    }

    public void buatTransaksi(Integer jumlah, Double diskon, LocalDate tanggal) {
        var transaksi = new Transaksi();
        transaksi.setJumlah(jumlah);
        transaksi.setDiskon(diskon);
        transaksi.setHarga(harga);
        transaksi.setTanggal(tanggal);

        kurangiStok(jumlah);
        addTransaksi(transaksi);
    }

    public void batalkanTransaksi(Long transaksiId) throws TransaksiNotFoundException {
        var transaksi = this.transaksi.stream()
                .filter(t -> t.getId().equals(transaksiId))
                .findFirst()
                .orElseThrow(() -> new TransaksiNotFoundException("Transaksi tidak ditemukan"));

        this.transaksi.remove(transaksi);

        tambahStok(transaksi.getJumlah());
    }

    public void batalkanRestok(Long pergerakanStokId) throws PergerakanStokNotFoundException {
        var pergerakanStok = this.pergerakanStok.stream()
                .filter(ps -> ps.getId().equals(pergerakanStokId))
                .findFirst()
                .orElseThrow(() -> new PergerakanStokNotFoundException("Pergerakan Stok tidak ditemukan"));

        this.pergerakanStok.remove(pergerakanStok);

        // Revert the stock changes
        tambahStok(pergerakanStok.getJumlahKeluar());
        kurangiStok(pergerakanStok.getJumlahMasuk());
    }

}
