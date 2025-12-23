package org.ilmi.eposkuserver.input.web.data.output;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaksiDTO {
    @NotNull
    private Long id;
    @NotNull
    private Integer jumlah;
    @NotNull
    private Double diskon;
    @NotNull
    private Double harga;
    @NotNull
    private LocalDate tanggal;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @NotNull
    @JsonGetter("totalHarga")
    public Double getTotalHarga() {
        return (harga - diskon) * jumlah;
    }

}
