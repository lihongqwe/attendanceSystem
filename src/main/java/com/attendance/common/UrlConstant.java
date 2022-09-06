package com.attendance.common;

public class UrlConstant {

    private static final String HOST = "https://oapi.dingtalk.com";
    /**
     * 获取access_token url
     */
    public static final String GET_ACCESS_TOKEN_URL = HOST + "/gettoken";
    public static final String GET_PUNCH_RESULTS = HOST + "/attendance/listRecord";
    public static final String GEt_USER_lISTID = HOST + "/topapi/attendance/group/memberusers/list";
    public static final String GET_USER_INFORMATION = HOST + "/topapi/v2/user/list";
    public static final String Get_DeptId = HOST + "/topapi/v2/department/listsub";
    public static final String Get = HOST + "/topapi/v2/department/listsubid";

    public static final String GET_USER = HOST + "/topapi/v2/user/getuserinfo";
    public static final String GET_USER_DETAILS = HOST + "/topapi/v2/user/get";
    public static final String GET_UPDATE_DATA = HOST + "/topapi/attendance/getupdatedata";
    public static final String GET_Attendance_Group = HOST + "/topapi/attendance/group/minimalism/list";

}
