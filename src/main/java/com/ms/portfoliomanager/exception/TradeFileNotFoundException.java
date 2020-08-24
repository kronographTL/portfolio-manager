package com.ms.portfoliomanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason =" Trade file is Missing please try again ")
public class TradeFileNotFoundException extends Throwable {
    public TradeFileNotFoundException(String message) {
        super(message);
    }
}
