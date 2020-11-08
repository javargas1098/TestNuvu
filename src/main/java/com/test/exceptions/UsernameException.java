package com.test.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernameException extends UsernameNotFoundException {
    public UsernameException(String msg) {
        super(msg);
    }

    public UsernameException(String msg, Throwable t) {
        super(msg, t);
    }


}
