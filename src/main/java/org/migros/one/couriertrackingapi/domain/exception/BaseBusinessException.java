package org.migros.one.couriertrackingapi.domain.exception;

import lombok.Getter;
import org.migros.one.couriertrackingapi.domain.constant.ErrorCodes;
import org.springframework.http.HttpStatus;

@Getter
public class BaseBusinessException extends RuntimeException {
    private final ErrorCodes errorCode;
    private final HttpStatus httpStatus;

    public BaseBusinessException(ErrorCodes errorCode, HttpStatus httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
