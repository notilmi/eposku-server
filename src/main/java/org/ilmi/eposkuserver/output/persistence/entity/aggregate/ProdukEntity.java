package org.ilmi.eposkuserver.output.persistence.entity.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ilmi.eposkuserver.output.persistence.entity.PergerakanStokEntity;
import org.ilmi.eposkuserver.output.persistence.entity.TransaksiEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("produk")
public class ProdukEntity implements Persistable<@NonNull Long> {
    @Id @Column("id")
    private Long id;

    @Column("nama")
    private String nama;

    @Column("deskripsi")
    private String deskripsi;

    @Column("harga")
    private Double harga;

    @MappedCollection(idColumn = "produk_id", keyColumn = "produk_key")
    private List<PergerakanStokEntity> pergerakanStok;

    @MappedCollection(idColumn = "produk_id", keyColumn = "produk_key")
    private List<TransaksiEntity> transaksi;

     @Override
    public boolean isNew() {
        return this.id == null;
    }
}
