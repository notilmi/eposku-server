package org.ilmi.eposkuserver.input.web.data.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdukSummaryDTO {
    private String id;
    private String nama;
    private String deskripsi;
    private String harga;
    private Integer stokSaatIni;
    private Integer totalTransaksi;
    private Double totalPendapatan;
}
