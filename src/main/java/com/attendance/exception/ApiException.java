package com.attendance.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ApiException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    protected int errCode;
    protected String msg;


    public ApiException(int errCode, String msg){
        this.errCode=errCode;
        this.msg = msg;
    }


    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }




    public String getErrMsg() {
        return msg;
    }

    public void setErrMsg(String errMsg) {
        this.msg = msg;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .append("errCode", getErrCode())
            .append("msg", getErrMsg())
            .toString();
    }
}
