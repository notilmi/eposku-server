package org.ilmi.eposkuserver.service;

import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.jspecify.annotations.NonNull;

import java.util.List;

public interface ProdukService {
    List<@NonNull ProdukSummary> getAllProduk(
            int page,
            int size
    );

    Produk getProdukById(Long produkId);

    Produk buatProduk(
            String nama, String deskripsi,
            Double harga, Integer stok
    );

    Produk updateProduk(
            Long produkId, String nama,
            String deskripsi, Double harga
    );

    void hapusProduk(Long produkId);

    Produk restokProduk(
            Long produkId, Integer jumlahMasuk,
            Integer jumlahKeluar
    );

    Produk buatTransaksi(
            Long produkId, Integer jumlah,
            Double diskon
    );

    Produk batalkanTransaksi(Long produkId, Long transaksiId);

    Produk batalkanRestok(Long produkId, Long pergerakanStokId);
}
