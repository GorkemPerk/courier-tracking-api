package org.migros.one.couriertrackingapi.domain.exception;


import org.migros.one.couriertrackingapi.domain.constant.ErrorCodes;
import org.springframework.http.HttpStatus;

public class BusinessException extends BaseBusinessException {
    public BusinessException(String message) {
        super(ErrorCodes.BUSINESS, HttpStatus.BAD_REQUEST, message);
    }

    public BusinessException(ErrorCodes errorCodes, String message) {
        super(errorCodes, HttpStatus.BAD_REQUEST, message);
    }
}
