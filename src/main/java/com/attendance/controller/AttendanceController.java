package com.attendance.controller;

import com.attendance.common.Result;
import com.attendance.domain.LoginUser;
import com.attendance.domain.creditTransfer;
import com.attendance.mapper.creditTransferMapper;
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

    @Autowired
    private creditTransferMapper creditTransferMapper;

    @GetMapping("/user/list")
    public Result getstudentattendanceinfo(@RequestParam(value = "userId") String  userId,
                                           String startDate, String EndDate,Integer conversion){
    if(StringUtils.isNull(startDate) || StringUtils.isNull(EndDate) || StringUtils.isNull(conversion)){
        return studentAttendanceInfoService.GetStudentAttendanceInfo(userId);
    }
        return studentAttendanceInfoService.GetStudentAttendanceInfoByTime(userId,EndDate,startDate);
    }


    @GetMapping("/conversion")
    public Result conversion( Integer  WorkingHours, Integer  LearnHours){
            creditTransfer creditTransfer=new creditTransfer();
            creditTransfer.setId(1);
            creditTransfer.setWorkingHours(WorkingHours);
            creditTransfer.setLearnHours(LearnHours);
           if(creditTransferMapper.updateByPrimaryKey(creditTransfer)==1){
               return Result.success("修改成功");
           }
        return Result.error("修改失败");
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
