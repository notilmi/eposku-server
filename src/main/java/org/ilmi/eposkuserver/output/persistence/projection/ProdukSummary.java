package org.ilmi.eposkuserver.output.persistence.projection;

public record ProdukSummary(
        Long id,
        String nama,
        String deskripsi,
        Double harga,
        String imageUrl,
        Integer stokSaatIni,
        Integer totalTransaksi,
        Long totalPendapatan
) {
}
