package org.ilmi.eposkuserver.produk.data.input;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RestokRequest {
    private Integer jumlahMasuk;
    private Integer jumlahKeluar;

    private LocalDate tanggal;
}
