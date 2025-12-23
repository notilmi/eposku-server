package org.ilmi.eposkuserver.output.persistence.repository;

import org.ilmi.eposkuserver.output.persistence.entity.aggregate.ProdukEntity;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdukRepository extends CrudRepository<@NonNull ProdukEntity, @NonNull Long> {
    @Query("SELECT p.id AS id, p.nama AS nama, p.deskripsi AS deskripsi, p.harga AS harga, p.image_url as image_url, " +
            // stock movement sum as INT
            "(SELECT COALESCE(SUM(ps.jumlah_masuk - ps.jumlah_keluar), 0) FROM pergerakan_stok ps WHERE ps.produk_id = p.id) AS stok_saat_ini, " +
            // total transactions as BIGINT/COUNT
            "(SELECT COALESCE(COUNT(t.id), 0) FROM transaksi t WHERE t.produk_id = p.id) AS total_transaksi, " +
            // total revenue as DOUBLE; use SUM(t.harga * t.jumlah * t.diskon)
            "(SELECT COALESCE(SUM(t.harga * t.jumlah * t.diskon), 0) FROM transaksi t WHERE t.produk_id = p.id) AS total_pendapatan " +
            "FROM produk p")
    List<ProdukSummary> findAllBy();

    @Query("SELECT p.id AS id, p.nama AS nama, p.deskripsi AS deskripsi, p.harga AS harga, p.image_url as image_url, " +
            "(SELECT COALESCE(SUM(ps.jumlah_masuk - ps.jumlah_keluar), 0) FROM pergerakan_stok ps WHERE ps.produk_id = p.id) AS stok_saat_ini, " +
            "(SELECT COALESCE(COUNT(t.id), 0) FROM transaksi t WHERE t.produk_id = p.id) AS total_transaksi, " +
            "(SELECT COALESCE(SUM(t.harga * t.jumlah * t.diskon), 0) FROM transaksi t WHERE t.produk_id = p.id) AS total_pendapatan " +
            "FROM produk p WHERE LOWER(p.nama) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProdukSummary> searchAllBy(@Param("keyword") String keyword);
}
