package org.ilmi.eposkuserver.service.impl;

import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.domain.entity.PergerakanStok;
import org.ilmi.eposkuserver.exception.ProdukNotFoundException;
import org.ilmi.eposkuserver.output.persistence.adapter.ProdukPersistenceAdapter;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.ilmi.eposkuserver.service.ProdukService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProdukServiceImpl implements ProdukService {
    private final ProdukPersistenceAdapter produkPersistenceAdapter;

    public ProdukServiceImpl(ProdukPersistenceAdapter produkPersistenceAdapter) {
        this.produkPersistenceAdapter = produkPersistenceAdapter;
    }

    @Override
    public List<@NonNull ProdukSummary> getAllProduk(int page, int size) {
        return produkPersistenceAdapter.findAllProduk(page, size);
    }

    @Override
    public Produk getProdukById(Long produkId) {

        return produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);
    }

    @Override
    public Produk buatProduk(
            String nama,
            String deskripsi,
            Double harga,
            Integer stok
    ) {
        var produk = new Produk();
        produk.setNama(nama);
        produk.setDeskripsi(deskripsi);
        produk.setHarga(harga);

        var initialStok = new PergerakanStok();
        initialStok.setJumlahMasuk(stok);
        initialStok.setTanggal(LocalDate.now());

        produk.addPergerakanStok(initialStok);

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public Produk updateProduk(
            Long produkId, String nama,
            String deskripsi, Double harga
    ) {
        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produk.setNama(nama);
        produk.setDeskripsi(deskripsi);
        produk.setHarga(harga);

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public void hapusProduk(Long produkId) {
        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produkPersistenceAdapter.delete(produk);
    }

    @Override
    public Produk restokProduk(
            Long produkId, Integer jumlahMasuk,
            Integer jumlahKeluar
    ) {
        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        var pergerakanStok = new PergerakanStok();
        pergerakanStok.setJumlahMasuk(jumlahMasuk == null ? 0 : jumlahMasuk);
        pergerakanStok.setJumlahKeluar(jumlahKeluar == null ? 0 : jumlahKeluar);
        pergerakanStok.setTanggal(LocalDate.now());

        produk.addPergerakanStok(pergerakanStok);

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public Produk buatTransaksi(Long produkId, Integer jumlah, Double diskon) {

        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produk.buatTransaksi(jumlah, diskon, LocalDate.now());

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public Produk batalkanTransaksi(Long produkId, Long transaksiId) {

        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produk.batalkanTransaksi(transaksiId);

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public Produk batalkanRestok(Long produkId, Long pergerakanStokId) {

        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produk.batalkanRestok(pergerakanStokId);

        return produkPersistenceAdapter.save(produk);
    }
}
