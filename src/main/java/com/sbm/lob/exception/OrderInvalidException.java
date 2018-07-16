package com.sbm.lob.exception;

public class OrderInvalidException extends RuntimeException{
    public OrderInvalidException(String message) {
        super(message);
    }
}
