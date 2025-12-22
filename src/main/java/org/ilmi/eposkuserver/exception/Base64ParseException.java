package org.ilmi.eposkuserver.exception;

import org.springframework.http.HttpStatus;

public class Base64ParseException extends StatusfulException {
    public Base64ParseException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                "BASE64_PARSE_ERROR",
                message
        );
    }

    public Base64ParseException() {
        this("Failed to parse base64 encoded string.");
    }
}
