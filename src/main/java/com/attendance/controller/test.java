package com.attendance.controller;

import com.attendance.common.Result;
import com.attendance.system.ScheduledTasks;
import com.attendance.system.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class test {

    @Autowired
    private TokenService tokenService;

    @Resource
    private ScheduledTasks scheduledTask;

    @GetMapping("test")
    public Result test(){
        scheduledTask.GetAttendanceInfoFromDingTalk();
//        scheduledTask.getStudentIdList();
//        scheduledTask.getUserName();
    return Result.success();
    }
}
