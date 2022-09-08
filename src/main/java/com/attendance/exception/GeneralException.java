package com.attendance.exception;

public class GeneralException extends ApiException{
    public GeneralException( int errCode,String msg) {
        super(errCode, msg);
    }
}
