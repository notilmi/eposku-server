package org.ilmi.eposkuserver.produk;

import org.ilmi.eposkuserver.AuthHelper;
import org.ilmi.eposkuserver.produk.data.input.*;
import org.ilmi.eposkuserver.produk.data.output.DailyStatsResponse;
import org.ilmi.eposkuserver.produk.data.output.ProdukResponse;
import org.ilmi.eposkuserver.produk.data.output.ProdukSummaryResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Lazy
@Component
public class ProdukApi {

    @Value("${local.server.port}")
    private int port;

    private static final String PRODUK_PATH = "/produk";

    private final AuthHelper authHelper;

    private final String sampleImageBase64 = ImageHelper.sampleImageBase64;

    @Autowired
    public ProdukApi(AuthHelper authHelper) {
        this.authHelper = authHelper;
    }

    public WebTestClient client() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .defaultHeader(AuthHelper.AUTH_HEADER, AuthHelper.BEARER + authHelper.getToken())
                .build();
    }

    public URI uriForProdukId(Long id) {
        return URI.create("http://localhost:" + port + PRODUK_PATH + "/" + id);
    }

    public WebTestClient.ResponseSpec getProdukById(Long id) {
        return client()
                .get()
                .uri(uriForProdukId(id))
                .exchange();
    }

    public WebTestClient.ResponseSpec getAllProduk(int page, int size) {
        return client()
                .get()
                .uri(builder -> builder.path(PRODUK_PATH).queryParam("page", page).queryParam("size", size).build())
                .exchange();
    }

    public WebTestClient.ResponseSpec getDailyStats() {
        return client()
                .get()
                .uri("/produk/stats/daily")
                .exchange();
    }

    public WebTestClient.ResponseSpec getMonthlyStats() {
        return client()
                .get()
                .uri("/produk/stats/monthly")
                .exchange();
    }

    public DailyStatsResponse getDailyStatsFromResponse(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec
                .expectBody(DailyStatsResponse.class)
                .returnResult()
                .getResponseBody();
    }

    public List<DailyStatsResponse> getDailyStatsListFromResponse(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec
                .expectBody(new ParameterizedTypeReference<@NonNull List<DailyStatsResponse>>() {})
                .returnResult()
                .getResponseBody();
    }

    public WebTestClient.ResponseSpec searchProduk(String keyword) {
        return client()
                .get()
                .uri(builder -> builder.path(PRODUK_PATH).queryParam("keyword", keyword).build())
                .exchange();
    }

    public WebTestClient.ResponseSpec buatProduk(
            String nama,
            String deskripsi,
            Double harga,
            Integer stok
    ) {

        return client()
                .post()
                .uri(PRODUK_PATH)
                .bodyValue(new ProdukRequest(
                        nama,
                        deskripsi,
                        harga,
                        stok,
                        null
                ))
                .exchange();
    }

    public WebTestClient.ResponseSpec updateProduk(
            Long produkId,
            String nama,
            String deskripsi,
            Double harga
    ) {
        return client()
                .put()
                .uri(uriForProdukId(produkId))
                .bodyValue(new UpdateProdukRequest(
                        nama,
                        deskripsi,
                        harga
                ))
                .exchange();
    }

    public WebTestClient.ResponseSpec deleteProduk(Long produkId) {
        return client()
                .delete()
                .uri(uriForProdukId(produkId))
                .exchange();
    }

    public WebTestClient.ResponseSpec restokProduk(Long produkId, Integer jumlahMasuk, Integer jumlahKeluar) {
        return client()
                .post()
                .uri(uriForProdukId(produkId) + "/restok")
                .bodyValue(new RestokRequest(jumlahMasuk, jumlahKeluar, LocalDate.now()))
                .exchange();
    }

    public WebTestClient.ResponseSpec buatBulkTransaksi(List<BulkTransaksiRequest> request) {
        return client()
                .post()
                .uri("/produk/transaksi")
                .bodyValue(request)
                .exchange();
    }

    public WebTestClient.ResponseSpec buatTransaksi(Long produkId, Integer jumlah, Double diskon) {
        return client()
                .post()
                .uri(uriForProdukId(produkId) + "/transaksi")
                .bodyValue(new TransaksiRequest(
                        jumlah,
                        diskon
                ))
                .exchange();
    }

    public WebTestClient.ResponseSpec batalkanTransaksi(Long produkId, Long transaksiId) {
        return client()
                .delete()
                .uri(uriForProdukId(produkId) + "/transaksi/" + transaksiId)
                .exchange();
    }

    public WebTestClient.ResponseSpec batalkanRestok(Long produkId, Long pergerakanStokId) {
        return client()
                .delete()
                .uri(uriForProdukId(produkId) + "/restok/" + pergerakanStokId)
                .exchange();
    }

    public ProdukResponse getProdukFromResponse(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec
                .expectBody(ProdukResponse.class)
                .returnResult()
                .getResponseBody();
    }

    public List<ProdukSummaryResponse> getProdukSummaryFromResponse(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec
                .expectBody(new ParameterizedTypeReference<@NonNull List<ProdukSummaryResponse>>() {})
                .returnResult()
                .getResponseBody();
    }
}
