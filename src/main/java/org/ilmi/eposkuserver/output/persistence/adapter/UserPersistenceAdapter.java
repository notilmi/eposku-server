package org.ilmi.eposkuserver.output.persistence.adapter;

import org.ilmi.eposkuserver.domain.User;

import java.util.Optional;

public interface UserPersistenceAdapter {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    User save(User user);
    void delete(User user);
}
