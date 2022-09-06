package com.attendance.system;

import com.attendance.domain.AttendanceGroup;

import java.util.List;

public interface ScheduledTasks {


    public  List<AttendanceGroup>GetAttendanceGroup();

    public  List<String> getStudentIdList();


    public List<Long> GetStudentDeptId();

    public void getUserName();




}
