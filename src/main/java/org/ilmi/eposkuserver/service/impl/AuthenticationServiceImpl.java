package org.ilmi.eposkuserver.service.impl;

import org.ilmi.eposkuserver.exception.UserNotFoundException;
import org.ilmi.eposkuserver.security.CustomUserDetails;
import org.ilmi.eposkuserver.service.AuthenticationService;
import org.ilmi.eposkuserver.service.SessionService;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(SessionService sessionService, AuthenticationManager authenticationManager) {
        this.sessionService = sessionService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseCookie login(String username, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails == null) {
            throw new UserNotFoundException();
        }

        String sessionToken = sessionService.createSession(userDetails.getId());

        return ResponseCookie
                .from("session_token", sessionToken)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .build();
    }

    @Override
    public ResponseCookie logout(String token) {
        sessionService.invalidateSession(token);

        return ResponseCookie
                .from("session_token", "")
                .path("/")
                .maxAge(0)
                .build();
    }
}
