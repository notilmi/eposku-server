package org.ilmi.eposkuserver.input.web.data.output;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PergerakanStokDTO {
    @NotNull
    private Long id;

    @NotNull
    private Integer jumlahMasuk;

    @NotNull
    private Integer jumlahKeluar;

    @NotNull
    private LocalDate tanggal;
}
