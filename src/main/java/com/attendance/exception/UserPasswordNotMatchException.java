package com.attendance.exception;



public class UserPasswordNotMatchException extends ApiException {

    public UserPasswordNotMatchException(String msg)
    {
        super(400,msg);
    }

}
