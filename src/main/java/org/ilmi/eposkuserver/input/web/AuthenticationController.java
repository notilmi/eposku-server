package org.ilmi.eposkuserver.input.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.ilmi.eposkuserver.input.web.data.input.LoginRequestDTO;
import org.ilmi.eposkuserver.input.web.data.output.MessageResponseDTO;
import org.ilmi.eposkuserver.input.web.data.output.UserDTO;
import org.ilmi.eposkuserver.security.CustomUserDetails;
import org.ilmi.eposkuserver.service.AuthenticationService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API untuk autentikasi pengguna")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<@NonNull MessageResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO request
            ) {
        var sessionCookie = authenticationService.login(
                request.getUsername(),
                request.getPassword()
        );

        var responseHeaders = new HttpHeaders();

        responseHeaders.add(HttpHeaders.SET_COOKIE, sessionCookie.toString());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new MessageResponseDTO("Login successful"));
    }

    @GetMapping("/logout")
    public ResponseEntity<@NonNull MessageResponseDTO> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        var token = authorizationHeader.replace("Bearer ", "");

        var deleteCookie = authenticationService.logout(token);

        var responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new MessageResponseDTO("Logout successful"));
    }

    @GetMapping("/session")
    public ResponseEntity<@NonNull UserDTO> getSession(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        var userDTO = UserDTO.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .fullName(userDetails.getFullName())
                .build();

        return ResponseEntity.ok(userDTO);
    }
}
