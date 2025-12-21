package org.ilmi.eposkuserver.input.web.data.output.mapper;

import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.input.web.data.output.ProdukDTO;
import org.ilmi.eposkuserver.input.web.data.output.ProdukSummaryDTO;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdukDTOMapper {
    private final ModelMapper modelMapper;

    public ProdukDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProdukDTO toDTO(Produk produk) {
        return modelMapper.map(produk, ProdukDTO.class);
    }

    public ProdukSummaryDTO toSummaryDTO(ProdukSummary produk) {
        return modelMapper.map(produk, ProdukSummaryDTO.class);
    }
}
