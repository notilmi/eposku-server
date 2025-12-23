package org.ilmi.eposkuserver.output.persistence.projection;

import java.time.LocalDate;

public record DailyStatsSummary(
        Long totalTransaksi,
        Double totalPendapatan,
        LocalDate tanggal
) {
}
