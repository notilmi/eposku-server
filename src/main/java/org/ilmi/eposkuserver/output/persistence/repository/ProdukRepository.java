package org.ilmi.eposkuserver.output.persistence.repository;

import org.ilmi.eposkuserver.output.persistence.entity.aggregate.ProdukEntity;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProdukRepository extends CrudRepository<@NonNull ProdukEntity, @NonNull Long> {
    @Query("SELECT p.id AS id, p.nama AS nama, p.deskripsi AS deskripsi, p.harga AS harga, " +
            // Change alias to match Record component 'stokSaatIni'
            "(SELECT COALESCE(SUM(ps.jumlah_masuk - ps.jumlah_keluar), 0) " +
            " FROM pergerakan_stok ps WHERE ps.produk_id = p.id) AS stokSaatIni, " +

            // Change alias to match Record component 'totalTransaksi'
            "(SELECT COALESCE(COUNT(t.id), 0) " +
            " FROM transaksi t WHERE t.produk_id = p.id) AS totalTransaksi, " +

            // Change alias to match Record component 'totalPendapatan'
            "(SELECT COALESCE(SUM((p.harga * t.jumlah) * t.diskon), 0) " +
            " FROM transaksi t WHERE t.produk_id = p.id) AS totalPendapatan " +

            "FROM produk p")
    List<ProdukSummary> findAllBy(Pageable pageable);
}
