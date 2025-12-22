package org.ilmi.eposkuserver.auth;

import org.ilmi.eposkuserver.AuthHelper;
import org.ilmi.eposkuserver.auth.data.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTest {

    @Autowired
    private AuthApi authApi;

    @Autowired
    private AuthHelper authHelper;

    @BeforeEach
    void ensureTokenInitialized() {
        // Ensure a mock user exists and token can be generated
        authHelper.getToken();
    }

    @Test
    void getSession_shouldReturnAuthenticatedUser() {
        var responseSpec = authApi.getSession()
                .expectStatus().is2xxSuccessful();

        UserResponse user = authApi.getUserFromResponse(responseSpec);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("auth_helper_user");
        assertThat(user.getFullName()).isEqualTo("Test Helper User");
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void login_shouldReturnSessionCookie() {
        var resp = authApi.login("auth_helper_user", "auth_helper_password")
                .expectStatus().is2xxSuccessful()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .returnResult(String.class);

        var setCookie = resp.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(setCookie).isNotNull();
        assertThat(setCookie).contains("session_token");
    }

    @Test
    void logout_shouldInvalidateSessionCookie() {
        var response = authApi.logout()
                .expectStatus().is2xxSuccessful()
                .expectHeader().exists(HttpHeaders.SET_COOKIE)
                .returnResult(String.class);

        var setCookie = response.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(setCookie).isNotNull();
        // Many frameworks set Max-Age=0 or Expires in the delete cookie
        assertThat(setCookie).contains("session_token");

        authHelper.resetToken();
    }

    @Test
    void login_withInvalidCredentials_shouldFail() {
        authApi.login("unknown_user", "bad_password")
                .expectStatus().is4xxClientError();
    }

    @Test
    void getSession_withoutCookie_shouldFail() {
        authApi.getSessionWithoutCookie()
                .expectStatus().is4xxClientError();
    }
}
