package org.ilmi.eposkuserver.produk.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdukSummaryResponse {
    private Long id;
    private String nama;
    @Nullable
    private String deskripsi;
    private Double harga;
}
