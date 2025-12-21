package org.ilmi.eposkuserver.output.persistence.adapter;

import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProdukPersistenceAdapter {
    List<@NonNull ProdukSummary> findAllProduk(Integer page, Integer size);
    Optional<Produk> findById(Long produkId);

    Produk save(Produk produk);
    void delete(Produk produk);
}
