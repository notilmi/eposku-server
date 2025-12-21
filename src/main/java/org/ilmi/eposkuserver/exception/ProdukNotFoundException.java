package org.ilmi.eposkuserver.exception;

import org.springframework.http.HttpStatus;

public class ProdukNotFoundException extends StatusfulException {
    public ProdukNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                "PRODUK_NOT_FOUND",
                message
        );
    }

    public ProdukNotFoundException() {
        this("Produk not found");
    }
}
