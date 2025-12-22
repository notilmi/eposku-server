package org.ilmi.eposkuserver.input.web;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.ilmi.eposkuserver.service.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
@Slf4j
@Tag(name = "Storage", description = "Modul API untuk mengelola penyimpanan file.")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Operation(summary = "Get File", description = "Retrieve a file from storage. ROLE yang diperlukan: admin, staff, karyawan.")
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getFile(
            @PathVariable String fileName) {

        try {
            // Get file data from storage
            byte[] fileData = storageService.getFile(fileName);


            // Determine content type based on file extension
            String contentType = storageService.getMimeTypeByName(fileName);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(fileData.length);

            // Add cache control headers for better performance
            headers.setCacheControl("max-age=3600"); // Cache for 1 hour

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);

        } catch (RuntimeException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
