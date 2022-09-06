package com.attendance.system;

import com.attendance.common.Result;
import com.attendance.domain.studentUserInfo;

public interface studentUserInfoService {

    public studentUserInfo GetStudentUserInfoByPhoneNumber(String PhoneNumber);
}
