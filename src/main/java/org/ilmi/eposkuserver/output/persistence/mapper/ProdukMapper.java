package org.ilmi.eposkuserver.output.persistence.mapper;

import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.output.persistence.entity.aggregate.ProdukEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdukMapper {
    private final ModelMapper modelMapper;

    public ProdukMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProdukEntity toEntity(Produk domain) {
        return modelMapper.map( domain, ProdukEntity.class );
    }

    public Produk toDomain(ProdukEntity entity) {
        return modelMapper.map( entity, Produk.class );
    }
}
