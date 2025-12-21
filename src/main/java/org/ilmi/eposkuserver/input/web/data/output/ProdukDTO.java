package org.ilmi.eposkuserver.input.web.data.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdukDTO {
    private Long id;
    private String nama;
    @Nullable
    private String deskripsi;
    private Double harga;
    private List<PergerakanStokDTO> pergerakanStok;
    private List<TransaksiDTO> transaksi;
}
