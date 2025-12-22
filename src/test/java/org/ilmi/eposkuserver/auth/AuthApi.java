package org.ilmi.eposkuserver.auth;

import org.ilmi.eposkuserver.AuthHelper;
import org.ilmi.eposkuserver.auth.data.LoginRequest;
import org.ilmi.eposkuserver.auth.data.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

@Lazy
@Component
public class AuthApi {

    @Value("${local.server.port}")
    private int port;

    private final AuthHelper authHelper;

    private static final String AUTH_PATH = "/auth";

    public AuthApi(AuthHelper authHelper) {
        this.authHelper = authHelper;
    }

    private WebTestClient client() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + AUTH_PATH)
                .build();
    }

    public WebTestClient.ResponseSpec login(
            String username,
            String password
    ) {
        return client()
                .post()
                .uri("/login")
                .bodyValue(new LoginRequest(
                        username, password
                ))
                .exchange();
    }

    public WebTestClient.ResponseSpec logout() {
        return client()
                .get()
                .uri("/logout")
                .header(AuthHelper.AUTH_HEADER, AuthHelper.BEARER + authHelper.getToken())
                .exchange();
    }

    public WebTestClient.ResponseSpec getSession() {
        return client()
                .get()
                .uri("/session")
                .header(AuthHelper.AUTH_HEADER, AuthHelper.BEARER + authHelper.getToken())
                .exchange();
    }

    public WebTestClient.ResponseSpec getSessionWithoutCookie() {
        return client()
                .get()
                .uri("/session")
                .exchange();
    }

    public UserResponse getUserFromResponse(
            WebTestClient.ResponseSpec responseSpec
    ) {
        return responseSpec
                .expectBody(UserResponse.class)
                .returnResult()
                .getResponseBody();
    }
}
