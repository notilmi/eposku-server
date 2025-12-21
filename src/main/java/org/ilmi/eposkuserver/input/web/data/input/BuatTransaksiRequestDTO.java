package org.ilmi.eposkuserver.input.web.data.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuatTransaksiRequestDTO {
    @NotNull(message = "Jumlah tidak boleh kosong")
    @Min(value = 1, message = "Jumlah harus minimal 1")
    private Integer jumlah;

    @Max(value = 1, message = "Diskon maksimal 100%")
    @Min(value = 0, message = "Diskon minimal 0%")
    private Double diskon;
}
