package com.attendance.exception;

public class UsernameNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UsernameNotFoundException(String msg) {
        super(msg);
    }

}
