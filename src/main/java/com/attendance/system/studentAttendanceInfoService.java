package com.attendance.system;

import com.attendance.common.Result;

public interface studentAttendanceInfoService {
    /**
     * 获取学生出勤信息
     *
     * @param userId 用户id
     * @return {@link Result}
     */
    public Result GetStudentAttendanceInfo(String userId);

    public Result GetStudentAttendanceInfoByTime(String userId, int conversion, String startDate, String EndDate);

    public Result GetStudentAttendanceInfoByTime(String userId, String startDate, String EndDate);


}
