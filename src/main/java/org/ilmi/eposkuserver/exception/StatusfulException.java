package org.ilmi.eposkuserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StatusfulException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public StatusfulException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }
}
