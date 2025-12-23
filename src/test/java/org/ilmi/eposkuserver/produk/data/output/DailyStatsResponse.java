package org.ilmi.eposkuserver.produk.data.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatsResponse {
    private Long totalTransaksi;
    private Double totalPendapatan;
    private String tanggal;
}
