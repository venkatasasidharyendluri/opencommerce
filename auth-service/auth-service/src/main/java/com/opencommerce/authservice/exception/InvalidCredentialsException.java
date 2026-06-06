package com.opencommerce.authservice.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {

      super(message);
    }
}
