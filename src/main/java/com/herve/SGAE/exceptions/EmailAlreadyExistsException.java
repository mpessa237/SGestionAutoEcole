package com.herve.SGAE.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message)
    {
        super(message);
    }
}
