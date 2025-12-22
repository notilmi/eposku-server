package org.ilmi.eposkuserver.input.web.data.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuatProdukRequestDTO {
    @NotBlank(message = "Nama produk tidak boleh kosong")
    private String nama;
    private String deskripsi;

    @Min(value = 0, message = "Harga tidak boleh negatif")
    @NotNull(message = "Harga tidak boleh kosong")
    private Double harga;

    @NotNull(message = "Stok tidak boleh kosong")
    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stok;

    private String image;
}
