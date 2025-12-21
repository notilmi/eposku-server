package org.ilmi.eposkuserver.service;

import org.springframework.http.ResponseCookie;

public interface AuthenticationService {
    ResponseCookie login(String username, String password);
    ResponseCookie logout(String token);
}
