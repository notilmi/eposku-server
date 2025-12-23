package org.ilmi.eposkuserver.produk.data.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaksiResponse {
    private Long id;
    private Integer jumlah;
    private Double diskon;
    private Double harga;
}
