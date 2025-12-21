package org.ilmi.eposkuserver.security;

import org.ilmi.eposkuserver.domain.User;
import org.ilmi.eposkuserver.output.persistence.adapter.UserPersistenceAdapter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserPersistenceAdapter userPersistenceAdapter;

    public CustomUserDetailService(UserPersistenceAdapter userPersistenceAdapter) {
        this.userPersistenceAdapter = userPersistenceAdapter;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userPersistenceAdapter.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(@NonNull Long id) throws UsernameNotFoundException {
        User user = userPersistenceAdapter.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return new CustomUserDetails(user);
    }
}
