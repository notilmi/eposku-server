package org.ilmi.eposkuserver.produk.data.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProdukRequest {
    private String nama;
    private String deskripsi;
    private Double harga;
}
