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
public class BulkCreateTransaksiRequestDTO {
    @NotNull(message = "produkId tidak boleh null")
    private Long produkId;

    @NotNull(message = "jumlah tidak boleh null")
    @Min(message = "jumlah harus lebih besar dari atau sama dengan 1", value = 1)
    private Integer jumlah;

    @Min(value = 0, message = "diskon harus lebih besar dari atau sama dengan 0")
    @Max(value = 1, message = "diskon harus kurang dari atau sama dengan 100%")
    private Double diskon;
}
