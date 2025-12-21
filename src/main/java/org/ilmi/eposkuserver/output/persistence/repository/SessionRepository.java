package org.ilmi.eposkuserver.output.persistence.repository;

import org.ilmi.eposkuserver.output.persistence.entity.aggregate.SessionEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<@NonNull SessionEntity, @NonNull Long> {
    @Query("SELECT * FROM sessions WHERE token = :token")
    Optional<SessionEntity> findByToken(@Param("token") String token);

    @Query("SELECT EXISTS(SELECT 1 FROM sessions WHERE token = :token)")
    boolean existsByToken(@Param("token") String token);
}
