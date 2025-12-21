package org.ilmi.eposkuserver.output.persistence.adapter.impl;

import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.output.persistence.adapter.ProdukPersistenceAdapter;
import org.ilmi.eposkuserver.output.persistence.mapper.ProdukMapper;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.ilmi.eposkuserver.output.persistence.repository.ProdukRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdukPersistenceAdapterImpl implements ProdukPersistenceAdapter {
    private final ProdukRepository produkRepository;
    private final ProdukMapper produkMapper;

    public ProdukPersistenceAdapterImpl(ProdukRepository produkRepository, ProdukMapper produkMapper) {
        this.produkRepository = produkRepository;
        this.produkMapper = produkMapper;
    }

    @Override
    public List<@NonNull ProdukSummary> findAllProduk(Integer page, Integer size) {
        return produkRepository.findAllBy(PageRequest.of(page, size));
    }

    @Override
    public Optional<Produk> findById(Long produkId) {
        return produkRepository.findById(produkId)
                .map(produkMapper::toDomain);
    }

    @Override
    public Produk save(Produk produk) {

        return produkMapper.toDomain(
                produkRepository.save(
                        produkMapper.toEntity(produk)
                )
        );
    }

    @Override
    public void delete(Produk produk) {
        produkRepository.delete(
                produkMapper.toEntity(produk)
        );
    }
}
