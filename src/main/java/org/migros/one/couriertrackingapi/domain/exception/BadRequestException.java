package org.migros.one.couriertrackingapi.domain.exception;


import org.migros.one.couriertrackingapi.domain.constant.ErrorCodes;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseBusinessException {
    public BadRequestException(String message) {
        super(ErrorCodes.BUSINESS, HttpStatus.BAD_REQUEST, message);
    }

    public BadRequestException(ErrorCodes errorCodes, String message) {
        super(errorCodes, HttpStatus.BAD_REQUEST, message);
    }
}
