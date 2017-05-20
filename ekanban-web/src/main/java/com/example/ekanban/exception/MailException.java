package com.example.ekanban.exception;

/**
 * Created by mdzahidraza on 14/05/17.
 */
public class MailException extends RuntimeException{

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailException(String message) {
        super(message);
    }
}
