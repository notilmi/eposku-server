package org.ilmi.eposkuserver.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.ilmi.eposkuserver.output.persistence.entity.aggregate.UserUploadedFile;
import org.ilmi.eposkuserver.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public StorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String getFileExtensionFromMime(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            return "";
        }

        return switch (mimeType.toLowerCase()) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "application/pdf" -> ".pdf";
            case "text/plain" -> ".txt";
            case "text/csv" -> ".csv";
            case "application/json" -> ".json";
            case "application/xml" -> ".xml";
            case "application/zip" -> ".zip";
            case "audio/mpeg" -> ".mp3";
            case "video/mp4" -> ".mp4";
            default -> "";
        };
    }

    @Override
    public String createUniqueFileName(
            String mimeType
    ) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileExtension = getFileExtensionFromMime(mimeType);

        return String.format("%s%s", timestamp, fileExtension);
    }

    @Override
    public byte[] parseBase64(String base64) {
        try {
            // Remove data URL prefix if present (e.g., "data:image/png;base64,")
            String cleanBase64 = base64;
            if (base64.contains(",")) {
                cleanBase64 = base64.substring(base64.indexOf(",") + 1);
            }

            return Base64.getDecoder()
                    .decode(cleanBase64);
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode base64 string: {}", e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public String getMimeTypeByByte(byte[] data) {
        if (data == null || data.length == 0) {
            return "application/octet-stream";
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            String mimeType = URLConnection.guessContentTypeFromStream(bais);
            return mimeType != null ? mimeType : "application/octet-stream";
        } catch (IOException e) {
            log.error("Error detecting MIME type from byte array: {}", e.getMessage());
            return "application/octet-stream";
        }
    }

    @Override
    public String getMimeTypeByBase64(String base64) {
        byte[] data = parseBase64(base64);
        return getMimeTypeByByte(data);
    }

    @Override
    public String getMimeTypeByName(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "application/octet-stream";
        }
        try {
            String mimeType = Files.probeContentType(Paths.get(fileName));
            if (mimeType != null) {
                return mimeType;
            }
        } catch (Exception e) {
            log.warn("Could not determine MIME type by file name: {}", e.getMessage());
        }
        // Fallback: simple extension mapping
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return switch (ext) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            case "csv" -> "text/csv";
            case "json" -> "application/json";
            case "xml" -> "application/xml";
            case "zip" -> "application/zip";
            case "mp3" -> "audio/mpeg";
            case "mp4" -> "video/mp4";
            default -> "application/octet-stream";
        };
    }

    @Override
    public UserUploadedFile uploadFile(
            byte[] bytes,
            String mime
    ) {
        try {
            // Membuat nama objek unik (termasuk path)
            String objectName = createUniqueFileName(mime);

            // Mengupload ke minio - Fixed: using proper part size instead of -1
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, 10485760) // 10MB part size
                            .contentType(mime)
                            .build()
            );

            UserUploadedFile fileObject = new UserUploadedFile();
            fileObject.setUrl(objectName);
            fileObject.setProvider("minio");

            return fileObject;
        } catch (Exception e) {
            log.error("[STORAGE SERVICE] Gagal mengupload file: {}", e.getMessage());
            throw new RuntimeException("Gagal mengupload file", e);
        }
    }

    @Override
    public byte[] getFile(String objectName) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

            // Read all bytes from the input stream
            byte[] fileData = inputStream.readAllBytes();
            inputStream.close();

            log.info("File retrieved successfully: {}", objectName);
            return fileData;
        } catch (Exception e) {
            log.error("[STORAGE SERVICE] Gagal mengambil file {} {} ", objectName, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve file from storage", e);
        }
    }


    @Override
    public void deleteFile(String fileUrl) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileUrl)
                            .build()
            );
            log.info("File deleted successfully: {}", fileUrl);
        } catch (Exception e) {
            log.error("[STORAGE SERVICE] Gagal menghapus file: {}", e.getMessage());
            throw new RuntimeException("Gagal menghapus file", e);
        }
    }
}
