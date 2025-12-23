package org.ilmi.eposkuserver.output.persistence.projection;

import jakarta.validation.constraints.NotNull;

public record ProdukSummary(
        @NotNull
        Long id,
        @NotNull
        String nama,
        String deskripsi,
        @NotNull
        Double harga,
        String imageUrl,
        @NotNull
        Integer stokSaatIni,
        @NotNull
        Integer totalTransaksi,
        @NotNull
        Long totalPendapatan
) {
}
