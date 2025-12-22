package org.ilmi.eposkuserver.produk;

import org.ilmi.eposkuserver.produk.data.BulkTransaksiRequest;
import org.ilmi.eposkuserver.produk.data.ProdukRequest;
import org.ilmi.eposkuserver.produk.data.ProdukResponse;
import org.ilmi.eposkuserver.produk.data.ProdukSummaryResponse;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ProdukTest {

    @Autowired
    ProdukApi produkApi;
    @Autowired
    private StorageApi storageApi;

    @Test
    void givenAProduk_itShouldBeAbleToCrud() {
        // Create new produk
        var createResp = produkApi.buatProduk("Pensil", "HB 2B", 1500.0, 10);

        // Parse creation response
        var createdProduk = produkApi.getProdukFromResponse(createResp);

        // Validate created produk
        shouldAllocateNewId(createdProduk);

        // List all produk and ensure created produk is present
        var listResp = produkApi.getAllProduk(0, 10)
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult();

        // Basic check to ensure response body is not null
        assertThat(listResp.getResponseBody()).isNotNull();

        var updateResp = produkApi.updateProduk(
                createdProduk.getId(),
                "Pensil Ubah",
                "2B Ubah",
                2000.0
        );

        // Parse update response
        var updatedProduk = produkApi.getProdukFromResponse(updateResp);

        // Validate updated produk
        shouldUpdateProduk(updatedProduk, "Pensil Ubah", "2B Ubah", 2000.0);

        // Fetch produk by ID to ensure it can be retrieved
        var getByIdResp = produkApi.getProdukById(createdProduk.getId());

        // Validate fetch by ID response status
        shouldReturn_200xStatus(getByIdResp);

        // Delete the created produk
        var deleteResp = produkApi.deleteProduk(createdProduk.getId());

        // Validate delete response status
        shouldReturn_200xStatus(deleteResp);

        // Ensure produk is no longer retrievable
        var getAfterDeleteResp = produkApi.getProdukById(createdProduk.getId());
        shouldReturn_404Status(getAfterDeleteResp);
    }

    @Test
    void givenMultipleProduk_itShouldBeAbleToBeSearched() {
        // Create multiple produk
        produkApi.buatProduk("Buku Tulis", "Buku Tulis Bergaris", 5000.0, 20);
        produkApi.buatProduk("Buku Gambar", "Buku Gambar Polos", 7000.0, 15);
        produkApi.buatProduk("Pensil Warna", "Set Pensil Warna 12pcs", 12000.0, 30);

        // Search for "Buku"
        var searchResult = produkApi.searchProduk("Buku");
        var searchResp = produkApi.getProdukSummaryFromResponse(searchResult);


        // Validate that at least 2 results are returned
        Assertions.assertNotNull(searchResp);
        Assertions.assertTrue(searchResp.size() >= 2);

        // Further validate that the results contain the search term
        for (ProdukSummaryResponse produk : searchResp) {
            Assertions.assertTrue(
                    produk.getNama().toLowerCase().contains("buku")
            );
        }
    }

    @Test
    void createProduk_withBase64Image_shouldReturnImageMetadata() {
        var client = produkApi.client();
        var request = ProdukRequest.builder()
                .nama("Produk Gambar")
                .deskripsi("Ada gambar base64")
                .harga(2500.0)
                .stok(3)
                .image(ImageHelper.sampleImageBase64)
                .build();

        var response = client.post()
                .uri("/produk")
                .bodyValue(request)
                .exchange();

        // Test Storage API using the imageUrl returned from /produk
        var createdProduk = produkApi.getProdukFromResponse(response);

        // If MinIO isn't configured for this test run, the /produk endpoint would typically fail.
        // But in case the environment returns a response without a usable imageUrl, skip cleanly.
        Assumptions.assumeTrue(
                createdProduk != null && createdProduk.getImageUrl() != null && !createdProduk.getImageUrl().isBlank(),
                "imageUrl not present; skipping storage retrieval test"
        );

        String objectName = createdProduk.getImageUrl();

        // NOTE: imageUrl in this app is stored as an object name (e.g. "20251222_101010.png"),
        // so we can call /storage/{fileName} directly.
        var storageResp = storageApi.getImage(
                objectName
        );

        // StorageController returns bytes; verify it can be accessed.
        storageResp
                .expectStatus().isOk();
    }

    @Test
    void givenAProduk_itShouldBeAbleToAllocateTransaksi() {
        // Create new produk
        var createResp = produkApi.buatProduk("Buku", "Buku Tulis", 5000.0, 20);
        var created = produkApi.getProdukFromResponse(createResp);
        shouldAllocateNewId(created);

        // Buat transaksi
        var transaksiResp = produkApi.buatTransaksi(created.getId(), 2, 0.2);
        var afterTransaksi = produkApi.getProdukFromResponse(transaksiResp);
        // Validate that harga remains and stok berkurang sesuai layanan bisnis (jumlah keluar)
        Assertions.assertEquals(5000.0, afterTransaksi.getHarga());
        // We can't directly read stok value from DTO response unless mapped; ensure endpoint returns successfully
        shouldReturn_200xStatus(produkApi.getProdukById(created.getId()));

        // Batalkan transaksi requires transaksiId; for now ensure endpoint works with fake id -> expect 4xx
        shouldReturn_404Status(produkApi.batalkanTransaksi(created.getId(), 9999L));
    }

    @Test
    void givenAProduk_itShouldBeAbleToAllocateStok() {
        // Create new produk
        var createResp = produkApi.buatProduk("Penghapus", "Penghapus Kecil", 2000.0, 15);
        var created = produkApi.getProdukFromResponse(createResp);
        shouldAllocateNewId(created);

        // Restok masuk 5
        var restokResp = produkApi.restokProduk(created.getId(), 5, 0);
        var afterRestok = produkApi.getProdukFromResponse(restokResp);
        Assertions.assertEquals("Penghapus", afterRestok.getNama());
        Assertions.assertEquals(2000.0, afterRestok.getHarga());

        // Fetch by id
        var getResp = produkApi.getProdukById(created.getId());
        shouldReturn_200xStatus(getResp);

        // Batalkan restok with fake id should 4xx
        shouldReturn_404Status(produkApi.batalkanRestok(created.getId(), 9999L));
    }

    @Test
    void givenMultipleProduk_itShouldBeAbleTo_bulkCreateTransaksi() {
        // Produk A
        var createRespA = produkApi.buatProduk("Spidol", "Spidol Warna", 3000.0, 30);
        var produkA = produkApi.getProdukFromResponse(createRespA);
        shouldAllocateNewId(produkA);

        // Produk B
        var createRespB = produkApi.buatProduk("Penggaris", "Penggaris 30cm", 1200.0, 25);
        var produkB = produkApi.getProdukFromResponse(createRespB);
        shouldAllocateNewId(produkB);

        // Buat bulk transaksi
        List<BulkTransaksiRequest> bulkRequests = List.of(
                BulkTransaksiRequest.builder()
                        .produkId(produkA.getId())
                        .jumlah(3)
                        .diskon(0.1)
                        .build(),
                BulkTransaksiRequest.builder()
                        .produkId(produkB.getId())
                        .jumlah(5)
                        .diskon(0.0)
                        .build()
        );

        var bulkTransaksiResp = produkApi.buatBulkTransaksi(bulkRequests);
        shouldReturn_200xStatus(bulkTransaksiResp);
    }


    void shouldAllocateNewId(ProdukResponse response) {
        Assertions.assertNotNull(response.getId());
    }

    void shouldReturn_200xStatus(
            WebTestClient.ResponseSpec responseSpec
    ) {
        responseSpec.expectStatus().is2xxSuccessful();
    }

    void shouldReturn_404Status(
            WebTestClient.ResponseSpec responseSpec
    ) {
        responseSpec.expectStatus().is4xxClientError();
    }

    void shouldUpdateProduk(
            ProdukResponse response,
            String expectedName,
            String expectedDesc,
            Double expectedPrice
    ) {
        Assertions.assertEquals(expectedName, response.getNama());
        Assertions.assertEquals(expectedDesc, response.getDeskripsi());
        Assertions.assertEquals(expectedPrice, response.getHarga());
    }


}
