package org.ilmi.eposkuserver.service;

import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.output.persistence.projection.DailyStatsSummary;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.ilmi.eposkuserver.service.data.BulkCreateTransaksiCommand;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface ProdukService {
    List<@NonNull ProdukSummary> getAllProduk(@Nullable String keyword);

    Produk getProdukById(Long produkId);

    DailyStatsSummary getDailyStats(LocalDate date);

    List<DailyStatsSummary> getMonthlyStats();

    Produk buatProduk(
            String nama, String deskripsi,
            Double harga, Integer stok, String image
    );

    Produk updateProduk(
            Long produkId, String nama,
            String deskripsi, Double harga, String image
    );

    void hapusProduk(Long produkId);

    Produk restokProduk(
            Long produkId, Integer jumlahMasuk,
            Integer jumlahKeluar
    );

    List<Produk> bulkCreateTransaksi(
      List<@NonNull BulkCreateTransaksiCommand> request
    );

    Produk buatTransaksi(
            Long produkId, Integer jumlah,
            Double diskon
    );

    Produk batalkanTransaksi(Long produkId, Long transaksiId);

    Produk batalkanRestok(Long produkId, Long pergerakanStokId);
}
