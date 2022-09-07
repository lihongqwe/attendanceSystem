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


    /**
     * 学生考勤信息时间
     *
     * @param userId    用户id
     * @param startDate 开始日期
     * @param EndDate   结束日期
     * @return {@link Result}
     */
    public Result GetStudentAttendanceInfoByTime(String userId, String startDate, String EndDate);





}
