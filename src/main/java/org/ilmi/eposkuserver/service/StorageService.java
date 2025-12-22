package org.ilmi.eposkuserver.service;

import org.ilmi.eposkuserver.output.persistence.entity.aggregate.UserUploadedFile;

public interface StorageService {
    /**
     * Mendapatkan ekstensi file dari tipe mimeType
     *
     * @param mimeType Tipe mimeType
     * @return Ekstensi file
     */
    String getFileExtensionFromMime(String mimeType);

    /**
     * Membuat nama file unik berdasarkan nama file asli
     *
     * @param mimeType Tipe    mimeType
     * @return Nama file unik
     */
    String createUniqueFileName(
            String mimeType);

    /**
     * Parse base64 string ke binary
     *
     * @param base64 string base64
     * @return byte[] Binary file format: `byte[]`
     */
    byte[] parseBase64(
            String base64
    );

    /**
     * Mengambil data mime dari data binary
     *
     * @param data byte[] Binary file format: `byte[]`
     */
    String getMimeTypeByByte(byte[] data);

    /**
     * Mengambil data mime dari data base64
     *
     * @param base64 Base64 String
     */
    String getMimeTypeByBase64(String base64);

    /**
     * Mengambil data mime dari nama file
     *
     * @param fileName Nama file
     */
    String getMimeTypeByName(String fileName);

    /**
     * Mengupload file ke storage
     *
     * @param bytes        Binary file format: `byte[]`
     * @param mime         Tipe Mime file
     * @return Object Name atau URL file yang diupload
     */
    UserUploadedFile uploadFile(
            byte[] bytes,
            String mime
    );

    /**
     * Mengambil file berdasarkan url
     *
     * @param objectName url file
     * @return Binary file format: `byte[]`
     */
    byte[] getFile(
            String objectName
    );

    /**
     * Menghapus file berdasarkan url
     *
     * @param fileUrl url file
     */
    void deleteFile(
            String fileUrl
    );
}
