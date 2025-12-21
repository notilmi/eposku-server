package org.ilmi.eposkuserver.output.persistence.projection;

public record ProdukSummary(
        String id,
        String nama,
        String deskripsi,
        String harga,
        Integer stokSaatIni,
        Integer totalTransaksi,
        Double totalPendapatan
) {
}
