package com.trade.exception;

public class RequestLimitedException extends RuntimeException {
    public RequestLimitedException(String msg) {
        super(msg);
    }
}
