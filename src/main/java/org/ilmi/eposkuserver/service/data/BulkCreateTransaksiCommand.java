package org.ilmi.eposkuserver.service.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkCreateTransaksiCommand {
    private Long produkId;
    private Integer jumlah;
    private Double diskon;
}
