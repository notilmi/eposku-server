package org.ilmi.eposkuserver.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ilmi.eposkuserver.domain.base.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaksi extends Entity {
    private Integer jumlah;
    private Double diskon = 0.0;
    private Double harga;
    private LocalDate tanggal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
