package org.ilmi.eposkuserver.input.web.data.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaksiDTO {
    private Long id;
    private Integer jumlah;
    private Double diskon;
    private Double harga;
    private LocalDate tanggal;
}
