package com.attendance.controller;

import com.attendance.common.Result;
import com.attendance.domain.LoginUser;
import com.attendance.system.studentAttendanceInfoService;
import com.attendance.utils.ServletUtils;
import com.attendance.utils.StringUtils;
import com.attendance.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考勤控制器
 *
 * @author lihong
 * @date 2022/09/03
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private studentAttendanceInfoService studentAttendanceInfoService;


    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/user/list")
    public Result getstudentattendanceinfo(@RequestParam(value = "userId") String  userId,
                                           String startDate, String EndDate,Integer conversion){
    if(StringUtils.isNull(startDate) || StringUtils.isNull(EndDate) || StringUtils.isNull(conversion)){
        return studentAttendanceInfoService.GetStudentAttendanceInfo(userId);
    }
        return studentAttendanceInfoService.GetStudentAttendanceInfoByTime(userId,conversion,EndDate,startDate);
    }


    @GetMapping("/conversion")
    public Result conversion(@RequestParam(value = "WorkingHours") String  WorkingHours,
                             @RequestParam(value = "LearnHours") String  LearnHours){
        Result result=new Result();
        result.put("WorkingHours",WorkingHours);
        result.put("LearnHours",LearnHours);
        return Result.success(result);

    }

    /**
     * 学生用户查看自己的考勤记录
     * @param startDate  开始日期
     * @param EndDate    结束日期
     * @return {@link Result}
     */
    @GetMapping("/user/getdatabyuserid")
    public Result getdatabyuserid(String startDate, String EndDate){
        LoginUser loginUser=tokenUtils.getLoginUser(ServletUtils.getRequest());
        if(StringUtils.isNull(startDate) || StringUtils.isNull(EndDate) )
        {
            return studentAttendanceInfoService.GetStudentAttendanceInfo(loginUser.getUser().getUserId());
        }
        return studentAttendanceInfoService.GetStudentAttendanceInfoByTime(loginUser.getUser().getUserId(),EndDate,startDate);
    }
}
