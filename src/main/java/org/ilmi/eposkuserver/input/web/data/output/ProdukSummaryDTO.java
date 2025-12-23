package org.ilmi.eposkuserver.input.web.data.output;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdukSummaryDTO {
    @NotNull private Long id;
    @NotNull private String nama;
    @Nullable private String deskripsi;
    @Nullable private String imageUrl;
    @NotNull private String harga;
    @NotNull private Integer stokSaatIni;
    @NotNull private Integer totalTransaksi;
    @NotNull private Double totalPendapatan;
}
