package org.ilmi.eposkuserver.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ilmi.eposkuserver.domain.base.Entity;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaksi extends Entity {
    private Integer jumlah;
    private Double diskon;
    private Double harga;
    private LocalDate tanggal;
}
