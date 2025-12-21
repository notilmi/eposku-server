package org.ilmi.eposkuserver.produk.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdukResponse {
    private Long id;
    private String nama;
    @Nullable
    private String deskripsi;
    private Double harga;
    private List<PergerakanStokResponse> pergerakanStok;
    private List<TransaksiResponse> transaksi;
}
