package org.ilmi.eposkuserver;


import lombok.extern.slf4j.Slf4j;
import org.ilmi.eposkuserver.domain.User;
import org.ilmi.eposkuserver.output.persistence.adapter.UserPersistenceAdapter;
import org.ilmi.eposkuserver.service.AuthenticationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthHelper {

    public static String AUTH_HEADER = "Authorization";
    public static String BEARER = "Bearer ";
    private final AuthenticationService authenticationService;
    private final UserPersistenceAdapter userPersistenceAdapter;

    private static final String MOCK_USERNAME = "auth_helper_user";
    private static final String MOCK_PASSWORD = "auth_helper_password";
    private static final String MOCK_FULLNAME = "Test Helper User";
    private final BCryptPasswordEncoder passwordEncoder;

    public String sessionToken = null;

    public AuthHelper(AuthenticationService authenticationService, UserPersistenceAdapter userPersistenceAdapter, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.userPersistenceAdapter = userPersistenceAdapter;
        this.passwordEncoder = passwordEncoder;
    }

    public String getToken() {
        if (sessionToken == null) {
            // Initialize Token

            var existingUser = userPersistenceAdapter.existsByUsername(MOCK_USERNAME);

            if (!existingUser) {
                // Register mock user
                var user = new User(
                        MOCK_USERNAME,
                        MOCK_FULLNAME,
                        passwordEncoder.encode(MOCK_PASSWORD)
                );

                var registeredUser = userPersistenceAdapter.save(user);

                log.info("Registered mock user for AuthHelper: {}", registeredUser);
            }

            // Authenticate mock user
            sessionToken = authenticationService.login(
                    MOCK_USERNAME,
                    MOCK_PASSWORD
            ).getValue();

            return sessionToken;
        }

        return sessionToken;
    }

    public Header getAuthHeader() {
        return new Header(AUTH_HEADER, BEARER + getToken());
    }

}
