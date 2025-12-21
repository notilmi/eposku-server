package org.ilmi.eposkuserver.input.web.data.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProdukRequestDTO {
    private String nama;
    private String deskripsi;
    private Double harga;
}
