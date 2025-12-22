package org.ilmi.eposkuserver.output.persistence.repository;

import org.ilmi.eposkuserver.output.persistence.entity.aggregate.UserUploadedFile;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface UserUploadedFileRepository extends CrudRepository<@NonNull UserUploadedFile, @NonNull Long> {
    Object findByUrl(String url);

    Object deleteByUrl(String url);
}
