package org.ilmi.eposkuserver.security;

import lombok.Getter;
import org.ilmi.eposkuserver.domain.User;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    @Getter
    private final Long id;
    private final String username;
    private final String password;
    @Getter
    private final String fullName;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(
            Long id,
            String username,
            String fullName,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.authorities = authorities;
    }

    public CustomUserDetails(Long id, String username, String fullName, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.fullName = user.getFullName();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public @NonNull String getUsername() {
        return username;
    }


}
