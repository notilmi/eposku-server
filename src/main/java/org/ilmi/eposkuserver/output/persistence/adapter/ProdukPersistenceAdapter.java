package org.ilmi.eposkuserver.output.persistence.adapter;

import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProdukPersistenceAdapter {
    List<@NonNull ProdukSummary> findAllProduk();
    List<@NonNull ProdukSummary> findAllProduk(@NonNull String keyword);
    Optional<Produk> findById(Long produkId);
    List<@NonNull Produk> findAllByIds(List<Long> produkIds);

    Produk save(Produk produk);
    List<Produk> saveAll(List<Produk> produkList);
    void delete(Produk produk);
}
