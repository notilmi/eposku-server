package org.ilmi.eposkuserver.output.persistence.repository;

import org.ilmi.eposkuserver.output.persistence.entity.aggregate.ProdukEntity;
import org.ilmi.eposkuserver.output.persistence.projection.DailyStatsSummary;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProdukRepository extends CrudRepository<@NonNull ProdukEntity, @NonNull Long> {

    @Query("""
        SELECT p.id AS id, p.nama AS nama, p.deskripsi AS deskripsi, p.harga AS harga, p.image_url as image_url,
            (SELECT COALESCE(SUM(ps.jumlah_masuk - ps.jumlah_keluar), 0) FROM pergerakan_stok ps WHERE ps.produk_id = p.id) AS stok_saat_ini,
            (SELECT COALESCE(COUNT(t.id), 0) FROM transaksi t WHERE t.produk_id = p.id) AS total_transaksi,
            (SELECT COALESCE(SUM(t.harga * t.jumlah * t.diskon), 0) FROM transaksi t WHERE t.produk_id = p.id) AS total_pendapatan
            FROM produk p
                ORDER BY total_transaksi DESC
    """)
    List<ProdukSummary> findAllBy();

    @Query("""
        SELECT p.id AS id, p.nama AS nama, p.deskripsi AS deskripsi, p.harga AS harga, p.image_url as image_url,
            (SELECT COALESCE(SUM(ps.jumlah_masuk - ps.jumlah_keluar), 0) FROM pergerakan_stok ps WHERE ps.produk_id = p.id) AS stok_saat_ini,
            (SELECT COALESCE(COUNT(t.id), 0) FROM transaksi t WHERE t.produk_id = p.id) AS total_transaksi,
            (SELECT COALESCE(SUM(t.harga * t.jumlah * t.diskon), 0) FROM transaksi t WHERE t.produk_id = p.id) AS total_pendapatan
            FROM produk p WHERE LOWER(p.nama) LIKE LOWER(CONCAT('%', :keyword, '%'))
                ORDER BY total_transaksi DESC
    """)
    List<ProdukSummary> searchAllBy(@Param("keyword") String keyword);

    @Query("""
        SELECT
            COUNT(id) AS total_transaksi,
            COALESCE(SUM(harga * jumlah * diskon), 0) AS total_pendapatan,
            tanggal
        FROM transaksi
        WHERE tanggal = :today
    """)
    DailyStatsSummary findDailyStats(@Param("today") LocalDate today);

    @Query("""
        SELECT
            COUNT(id) AS total_transaksi,
            COALESCE(SUM(harga * jumlah * diskon), 0) AS total_pendapatan,
            tanggal
        FROM transaksi
        WHERE EXTRACT(MONTH FROM tanggal) = EXTRACT(MONTH FROM CURRENT_DATE)
          AND EXTRACT(YEAR FROM tanggal) = EXTRACT(YEAR FROM CURRENT_DATE)
        GROUP BY tanggal
        ORDER BY tanggal
    """)
    List<DailyStatsSummary> findMonthlyStats();
}
