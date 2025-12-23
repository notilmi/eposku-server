package org.ilmi.eposkuserver.input.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.ilmi.eposkuserver.input.web.data.input.*;
import org.ilmi.eposkuserver.input.web.data.output.ProdukDTO;
import org.ilmi.eposkuserver.input.web.data.output.mapper.ProdukDTOMapper;
import org.ilmi.eposkuserver.output.persistence.projection.DailyStatsSummary;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.ilmi.eposkuserver.service.ProdukService;
import org.ilmi.eposkuserver.service.data.BulkCreateTransaksiCommand;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/produk")
@Tag(name = "Produk", description = "API untuk mengelola produk")
public class ProdukController {

    private final ProdukService produkService;
    private final ProdukDTOMapper produkDTOMapper;

    public ProdukController(ProdukService produkService, ProdukDTOMapper produkDTOMapper) {
        this.produkService = produkService;
        this.produkDTOMapper = produkDTOMapper;
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProdukSummary> getAllProduk(
            @RequestParam(required = false) String keyword
    ) {
        return produkService.getAllProduk(keyword);
    }

    @GetMapping("/stats/daily")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DailyStatsSummary getMonthlyStats(
            @RequestParam(required = false) String date
    ) {
        // Parse date
        var localDate = (date != null && !date.isBlank()) ?
                LocalDate.parse(date) : LocalDate.now();

        return produkService.getDailyStats(localDate);
    }

    @GetMapping("/stats/monthly")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<DailyStatsSummary> getMonthlyStats(
    ) {

        return produkService.getMonthlyStats();
    }

    @GetMapping("/{produkId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ProdukDTO getProdukById(
            @PathVariable Long produkId
    ) {
        var produk = produkService.getProdukById(produkId);
        return produkDTOMapper.toDTO(produk);
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ProdukDTO buatProduk(
            @RequestBody @Valid BuatProdukRequestDTO request
    ) {
        var createdProduk = produkService.buatProduk(
                request.getNama(),
                request.getDeskripsi(),
                request.getHarga(),
                request.getStok(),
                request.getImage()
        );

        return produkDTOMapper.toDTO(createdProduk);
    }

    @PutMapping("/{produkId}")
    @ResponseBody
    public ProdukDTO updateProduk(
            @PathVariable Long produkId,
            @RequestBody @Validated UpdateProdukRequestDTO request
    ) {
        var updatedProduk = produkService.updateProduk(
                produkId,
                request.getNama(),
                request.getDeskripsi(),
                request.getHarga(),
                request.getImage()
        );

        return produkDTOMapper.toDTO(updatedProduk);
    }

    @DeleteMapping("/{produkId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void hapusProduk(
            @PathVariable Long produkId
    ) {
        produkService.hapusProduk(produkId);
    }

    @PostMapping("/{produkId}/restok")
    @ResponseBody
    public ProdukDTO restokProduk(
            @PathVariable Long produkId,
            @RequestBody @Valid RestokProdukRequestDTO request
    ) {
        var restockedProduk = produkService.restokProduk(
                produkId,
                request.getJumlahMasuk(),
                request.getJumlahKeluar()
        );

        return produkDTOMapper.toDTO(restockedProduk);
    }

    @PostMapping("/{produkId}/transaksi")
    @ResponseBody
    public ProdukDTO buatTransaksi(
            @PathVariable Long produkId,
            @RequestBody @Valid BuatTransaksiRequestDTO request
    ) {
        var updatedProduk = produkService.buatTransaksi(
                produkId,
                request.getJumlah(),
                request.getDiskon()
        );

        return produkDTOMapper.toDTO(updatedProduk);
    }

    @PostMapping("/transaksi")
    @ResponseBody
    public List<ProdukDTO> buatTransaksiBulk(
            @RequestBody @Valid List<BulkCreateTransaksiRequestDTO> request
    ) {
        var updatedProduk = produkService.bulkCreateTransaksi(
                request.stream()
                        .map(cmd ->
                                BulkCreateTransaksiCommand.builder()
                                        .produkId(cmd.getProdukId())
                                        .jumlah(cmd.getJumlah())
                                        .diskon(cmd.getDiskon())
                                        .build()
                        )
                        .toList()
        );

        return updatedProduk.stream()
                .map(produkDTOMapper::toDTO)
                .toList();
    }

    @DeleteMapping("/{produkId}/transaksi/{transaksiId}")
    @ResponseBody
    public ProdukDTO batalkanTransaksi(
            @PathVariable Long produkId,
            @PathVariable Long transaksiId
    ) {
        var updatedProduk = produkService.batalkanTransaksi(produkId, transaksiId);

        return produkDTOMapper.toDTO(updatedProduk);
    }

    @DeleteMapping("/{produkId}/restok/{pergerakanStokId}")
    @ResponseBody
    public ProdukDTO batalkanRestok(
            @PathVariable Long produkId,
            @PathVariable Long pergerakanStokId
    ) {
        var updatedProduk = produkService.batalkanRestok(produkId, pergerakanStokId);
        return produkDTOMapper.toDTO(updatedProduk);
    }

}
