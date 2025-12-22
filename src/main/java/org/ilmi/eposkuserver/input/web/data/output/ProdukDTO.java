package org.ilmi.eposkuserver.input.web.data.output;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ilmi.eposkuserver.output.persistence.entity.aggregate.UserUploadedFile;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdukDTO {
    @NotNull private Long id;
    @NotNull private String nama;
    private String deskripsi;
    private String imageUrl;
    @NotNull private Double harga;
    @NotNull private List<PergerakanStokDTO> pergerakanStok;
    @NotNull private List<TransaksiDTO> transaksi;
}
