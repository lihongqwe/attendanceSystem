package com.attendance.exception;

public class UsernameNotFoundException extends ApiException{

    public UsernameNotFoundException(String msg) {
        super(400, msg);
    }

}
