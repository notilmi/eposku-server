package org.ilmi.eposkuserver.output.persistence.repository;

import org.ilmi.eposkuserver.output.persistence.entity.aggregate.UserEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<@NonNull UserEntity, @NonNull Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
