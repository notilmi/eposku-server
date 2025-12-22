package org.ilmi.eposkuserver.input.web.data.output;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdukSummaryDTO {
    @NotNull private Long id;
    @NotNull private String nama;
    private String deskripsi;
    @NotNull private String harga;
    @NotNull private Integer stokSaatIni;
    @NotNull private Integer totalTransaksi;
    @NotNull private Double totalPendapatan;
}
