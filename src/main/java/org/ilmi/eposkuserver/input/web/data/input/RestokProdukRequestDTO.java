package org.ilmi.eposkuserver.input.web.data.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestokProdukRequestDTO {
    @NotNull(message = "Jumlah masuk tidak boleh kosong")
    @Min(value = 0, message = "Jumlah masuk tidak boleh negatif")
    private Integer jumlahMasuk;

    @NotNull(message = "Jumlah keluar tidak boleh kosong")
    @Min(value = 0, message = "Jumlah keluar tidak boleh negatif")
    private Integer jumlahKeluar;
}
