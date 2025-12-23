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
public class PergerakanStok extends Entity {
    private Integer jumlahMasuk = 0;
    private Integer jumlahKeluar = 0;
    private LocalDate tanggal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
