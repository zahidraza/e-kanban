package com.example.ekanban.exception;

/**
 * Created by mdzahidraza on 15/05/17.
 */
public class ScanException extends RuntimeException {

    public ScanException(String message) {
        super(message);
    }

    public ScanException(String message, Throwable cause) {
        super(message, cause);
    }

}
