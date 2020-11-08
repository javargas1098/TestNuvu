package com.test.exceptions;

public class UserRuntimeException extends Exception {
    public UserRuntimeException(){

    }

    public UserRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
