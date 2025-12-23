package org.ilmi.eposkuserver.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("transaksi")
public class TransaksiEntity implements Persistable<@NonNull Long> {
    @Id
    @Column("id")
    private Long id;

    @Column("jumlah")
    private Integer jumlah;

    @Column("diskon")
    private Double diskon;

    @Column("harga")
    private Double harga;

    @Column("tanggal")
    private LocalDate tanggal;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean isNew() {
        return this.id == null;
    }
}
