package org.ilmi.eposkuserver.produk.data.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdukRequest {
    private String nama;
    private String deskripsi;
    private Double harga;
    private Integer  stok;
    private String image;
}
