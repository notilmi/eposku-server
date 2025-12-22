package org.ilmi.eposkuserver.produk;

import org.ilmi.eposkuserver.AuthHelper;
import org.ilmi.eposkuserver.produk.data.*;
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
public class StorageApi {

    @Value("${local.server.port}")
    private int port;

    private static final String PRODUK_PATH = "/produk";

    private final AuthHelper authHelper;

    private final String sampleImageBase64 = ImageHelper.sampleImageBase64;

    @Autowired
    public StorageApi(AuthHelper authHelper) {
        this.authHelper = authHelper;
    }

    public WebTestClient client() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .defaultHeader(AuthHelper.AUTH_HEADER, AuthHelper.BEARER + authHelper.getToken())
                .build();
    }

    public URI uriForImageUrl(String imageUrl) {
        return URI.create("http://localhost:" + port + "/storage/" + imageUrl);
    }

    public WebTestClient.ResponseSpec getImage(String url) {
        return client()
                .get()
                .uri(uriForImageUrl(url))
                .exchange();
    }

}
