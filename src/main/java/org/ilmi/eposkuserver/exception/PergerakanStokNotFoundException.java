package org.ilmi.eposkuserver.exception;

import org.springframework.http.HttpStatus;

public class PergerakanStokNotFoundException extends StatusfulException {
    public PergerakanStokNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                "PERGERAKAN_STOK_NOT_FOUND",
                message
        );
    }

    public PergerakanStokNotFoundException() {
        this("Pergerakan stok not found");
    }
}
