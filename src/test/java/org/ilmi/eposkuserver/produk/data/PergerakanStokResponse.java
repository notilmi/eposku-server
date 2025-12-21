package org.ilmi.eposkuserver.produk.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PergerakanStokResponse {
    private Long id;
    private Integer jumlahMasuk;
    private Integer jumlahKeluar;
    private LocalDate tanggal;
}
