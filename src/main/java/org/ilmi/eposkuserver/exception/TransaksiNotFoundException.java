package org.ilmi.eposkuserver.exception;

import org.springframework.http.HttpStatus;

public class TransaksiNotFoundException extends StatusfulException {
    public TransaksiNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                "TRANSAKSI_NOT_FOUND",
                message
        );
    }

    public TransaksiNotFoundException() {
        this("Transaksi not found");
    }
}
