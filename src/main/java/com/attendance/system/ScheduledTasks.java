package com.attendance.system;

import com.attendance.domain.AttendanceGroup;
import com.dingtalk.api.response.OapiAttendanceGetAttendUpdateDataResponse;

import java.util.List;

public interface ScheduledTasks {

    public void GetAttendanceInfoFromDingTalk();

    public  List<AttendanceGroup>GetAttendanceGroup();

    public  List<String> getStudentIdList();

    public List<OapiAttendanceGetAttendUpdateDataResponse.AtAttendanceResultForOpenVo> getUserNowClockInfo();

    public List<Long> GetStudentDeptId();

    public void getUserName();




}
