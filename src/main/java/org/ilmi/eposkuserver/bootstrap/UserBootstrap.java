package org.ilmi.eposkuserver.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.ilmi.eposkuserver.domain.User;
import org.ilmi.eposkuserver.output.persistence.adapter.UserPersistenceAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserBootstrap implements CommandLineRunner {
    private final UserPersistenceAdapter userPersistenceAdapter;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserBootstrap(UserPersistenceAdapter userPersistenceAdapter, BCryptPasswordEncoder passwordEncoder) {
        this.userPersistenceAdapter = userPersistenceAdapter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(@NotNull String... args) throws Exception {
        log.info("[👷‍ Boostrap] Running UserBootstrap...");

        boolean userExists = userPersistenceAdapter.existsByUsername("admin");
        if (userExists) {
            log.info("[👷‍ Boostrap] Admin user already exists. Skipping bootstrap.");
            return;
        }

        User user = new User();
        user.setFullName("Admin User");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin123"));

        log.info("[👷‍ Boostrap] Creating user...");
        userPersistenceAdapter.save(user);
        log.info("[👷‍ Boostrap] Bootstrapped user: {}", user);
    }
}
