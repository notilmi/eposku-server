package org.ilmi.eposkuserver.input.web.data.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PergerakanStokDTO {
    private Long id;
    private Integer jumlahMasuk;
    private Integer jumlahKeluar;
    private LocalDate tanggal;
}
