package org.ilmi.eposkuserver.produk.data.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkTransaksiRequest {
    private Long produkId;
    private Integer jumlah;
    private Double diskon;
}
