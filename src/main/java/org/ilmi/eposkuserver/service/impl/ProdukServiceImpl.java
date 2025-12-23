package org.ilmi.eposkuserver.service.impl;

import jakarta.validation.constraints.Null;
import org.ilmi.eposkuserver.domain.Produk;
import org.ilmi.eposkuserver.domain.entity.PergerakanStok;
import org.ilmi.eposkuserver.domain.entity.Transaksi;
import org.ilmi.eposkuserver.exception.Base64ParseException;
import org.ilmi.eposkuserver.exception.ProdukNotFoundException;
import org.ilmi.eposkuserver.output.persistence.adapter.ProdukPersistenceAdapter;
import org.ilmi.eposkuserver.output.persistence.projection.DailyStatsSummary;
import org.ilmi.eposkuserver.output.persistence.projection.ProdukSummary;
import org.ilmi.eposkuserver.output.persistence.repository.UserUploadedFileRepository;
import org.ilmi.eposkuserver.service.ProdukService;
import org.ilmi.eposkuserver.service.StorageService;
import org.ilmi.eposkuserver.service.data.BulkCreateTransaksiCommand;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProdukServiceImpl implements ProdukService {
    private final ProdukPersistenceAdapter produkPersistenceAdapter;
    private final StorageService storageService;
    private final UserUploadedFileRepository userUploadedFileRepository;

    public ProdukServiceImpl(ProdukPersistenceAdapter produkPersistenceAdapter, StorageService storageService, UserUploadedFileRepository userUploadedFileRepository) {
        this.produkPersistenceAdapter = produkPersistenceAdapter;
        this.storageService = storageService;
        this.userUploadedFileRepository = userUploadedFileRepository;
    }

    @Override
    public List<@NonNull ProdukSummary> getAllProduk(@Nullable String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return produkPersistenceAdapter.findAllProduk(keyword);
        }

        return produkPersistenceAdapter.findAllProduk();
    }

    @Override
    public Produk getProdukById(Long produkId) {

        return produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);
    }

    @Override
    public DailyStatsSummary getDailyStats(LocalDate date) {
        return produkPersistenceAdapter.findDailyStats(date);
    }

    @Override
    public List<DailyStatsSummary> getMonthlyStats() {
        return produkPersistenceAdapter.findMonthlyStats();
    }

    @Override
    public Produk buatProduk(
            String nama,
            String deskripsi,
            Double harga,
            Integer stok,
            String image
    ) {
        var produk = new Produk();
        produk.setNama(nama);
        produk.setDeskripsi(deskripsi);
        produk.setHarga(harga);

        if (image != null) {
            byte[] byteFile = null;

            try {
                byteFile = storageService.parseBase64(image);

            } catch (Exception e) {
                throw new Base64ParseException("Gagal mengupload gambar produk: " + e.getMessage());
            }

            var uploadedFile = storageService.uploadFile(
                    byteFile,
                    storageService.getMimeTypeByByte(byteFile)
            );

            var savedFile = userUploadedFileRepository.save(uploadedFile);

            produk.setImageUrl(savedFile.getUrl());
        }

        var initialStok = new PergerakanStok();
        initialStok.setJumlahMasuk(stok);
        initialStok.setTanggal(LocalDate.now());

        produk.addPergerakanStok(initialStok);

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public Produk updateProduk(
            Long produkId, String nama,
            String deskripsi, Double harga,
            String image
    ) {
        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produk.setNama(nama);
        produk.setDeskripsi(deskripsi);
        produk.setHarga(harga);

        if (image != null) {
            byte[] byteFile = null;

            try {
                byteFile = storageService.parseBase64(image);

            } catch (Exception e) {
                throw new Base64ParseException("Gagal mengupload gambar produk: " + e.getMessage());
            }

            var uploadedFile = storageService.uploadFile(
                    byteFile,
                    storageService.getMimeTypeByByte(byteFile)
            );

            var savedFile = userUploadedFileRepository.save(uploadedFile);

            produk.setImageUrl(savedFile.getUrl());
        } else {
            // Delete old image
            if (produk.getImageUrl() != null) {
                userUploadedFileRepository.deleteByUrl(produk.getImageUrl());
            }

            produk.setImageUrl(null);
        }

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public void hapusProduk(Long produkId) {
        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produkPersistenceAdapter.delete(produk);
    }

    @Override
    public Produk restokProduk(
            Long produkId, Integer jumlahMasuk,
            Integer jumlahKeluar
    ) {
        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        var pergerakanStok = new PergerakanStok();
        pergerakanStok.setJumlahMasuk(jumlahMasuk == null ? 0 : jumlahMasuk);
        pergerakanStok.setJumlahKeluar(jumlahKeluar == null ? 0 : jumlahKeluar);
        pergerakanStok.setTanggal(LocalDate.now());

        produk.addPergerakanStok(pergerakanStok);

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public List<Produk> bulkCreateTransaksi(List<@NonNull BulkCreateTransaksiCommand> request) {
        List<Transaksi> transaksiList = request.stream()
                .map(cmd -> {
                    var transaksi = new Transaksi();
                    transaksi.setJumlah(cmd.getJumlah());
                    transaksi.setDiskon(cmd.getDiskon());
                    transaksi.setTanggal(LocalDate.now());
                    return transaksi;
                })
                .toList();

        List<Long> produkIds = request.stream()
                .map(BulkCreateTransaksiCommand::getProdukId)
                .toList();

        List<Produk> produkList = produkPersistenceAdapter.findAllByIds(produkIds);

        for (Transaksi transaksi : transaksiList) {
            var produk = produkList.stream()
                    .filter(p -> p.getId().equals(
                            request.get(transaksiList.indexOf(transaksi)).getProdukId()
                    ))
                    .findFirst();

            produk.ifPresent(value -> value.buatTransaksi(
                    transaksi.getJumlah(),
                    transaksi.getDiskon(),
                    transaksi.getTanggal()
            ));
        }

        return produkPersistenceAdapter.saveAll(produkList);
    }

    @Override
    public Produk buatTransaksi(Long produkId, Integer jumlah, Double diskon) {

        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produk.buatTransaksi(jumlah, diskon, LocalDate.now());

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public Produk batalkanTransaksi(Long produkId, Long transaksiId) {

        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produk.batalkanTransaksi(transaksiId);

        return produkPersistenceAdapter.save(produk);
    }

    @Override
    public Produk batalkanRestok(Long produkId, Long pergerakanStokId) {

        var produk = produkPersistenceAdapter.findById(produkId)
                .orElseThrow(ProdukNotFoundException::new);

        produk.batalkanRestok(pergerakanStokId);

        return produkPersistenceAdapter.save(produk);
    }
}
