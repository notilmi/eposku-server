package org.ilmi.eposkuserver.bootstrap;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MinioBootstrap implements CommandLineRunner {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioBootstrap(MinioClient minioClient, @Value("${minio.bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public void run(@NotNull String... args) throws Exception {
        log.info("[👷‍ Boostrap] Running MinioBootstrap...");

        boolean bucketExist = minioClient.bucketExists(
                BucketExistsArgs
                        .builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!bucketExist) {
            log.info("[👷‍ Boostrap] Bucket '{}' does not exist. Creating...", bucketName);
            minioClient.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(bucketName)
                            .build()
            );
            log.info("[👷‍ Boostrap] Bucket '{}' created successfully.", bucketName);
        } else {
            log.info("[👷‍ Boostrap] Bucket '{}' already exists.", bucketName);
        }
    }
}

