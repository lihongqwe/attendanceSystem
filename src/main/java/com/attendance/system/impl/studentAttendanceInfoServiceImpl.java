package com.attendance.system.impl;

import com.attendance.common.Result;
import com.attendance.domain.AttendanceRecords;
import com.attendance.domain.creditTransfer;
import com.attendance.domain.studentAttendanceInfo;
import com.attendance.mapper.creditTransferMapper;
import com.attendance.mapper.studentAttendanceInfoMapper;
import com.attendance.system.studentAttendanceInfoService;
import com.attendance.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class studentAttendanceInfoServiceImpl implements studentAttendanceInfoService {


    @Autowired
    private studentAttendanceInfoMapper studentAttendanceInfoMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private creditTransferMapper creditTransferMapper;


    /**
     * 获取学生出勤信息
     *
     * @param userId 用户id
     * @return {@link Result}
     */
    @Override
    public Result GetStudentAttendanceInfo(String userId) {
        List<studentAttendanceInfo> studentAttendanceInfoList = studentAttendanceInfoMapper.selectAll(userId);
        Result result = new Result();
        if (studentAttendanceInfoList.size() == 0) {
            result.put("list",studentAttendanceInfoList);
            result.put("credits", "");
           return Result.success(result);
        }
        List<AttendanceRecords> list=attendanceRecordsList(studentAttendanceInfoList);
        //获取该用户的所有打卡信息
        result.put("list", list);
        result.put("credits", CalculationCredits(list));
        return Result.success(result);
    }

    /**
     * 计算学分
     *
     * @param list 列表
     * @return {@link Result}
     */
    public float CalculationCredits(List<AttendanceRecords> list ) {
        // 计算学分
        creditTransfer creditTransfer  =creditTransferMapper.selectByPrimaryKey(1);
        Integer WorkingHours=creditTransfer.getWorkingHours();
        Integer LearnHours=creditTransfer.getLearnHours();
        float credits = 0;
        for (AttendanceRecords attendanceRecords:list){
            assert false;
            credits += Float.parseFloat(attendanceRecords.getLength());
        }
        return credits /(WorkingHours*LearnHours) ;
    }

    /**
     * 学生考勤信息时间
     *
     * @param userId    用户id
     * @param startDate 开始日期
     * @param EndDate   结束日期
     * @return {@link Result}
     */
    @Override
    public Result GetStudentAttendanceInfoByTime(String userId, String startDate, String EndDate) {
        return getResult(userId, startDate, EndDate);
    }


    /**
     * 通过时间选择学生考勤信息
     *
     * @param userId    用户id
     * @param startDate 开始日期
     * @param EndDate   结束日期
     * @return {@link Result}
     */
    private Result getResult(String userId, String startDate, String EndDate) {
        String startTime = startDate + " " + "23:59:59";
        String endTime = EndDate + " " + "00:00:00";
        List<studentAttendanceInfo> studentAttendanceInfoList = studentAttendanceInfoMapper.searchAllByTime(userId, startTime, endTime);
        Result result = new Result();
        if (studentAttendanceInfoList.size() == 0) {
            result.put("list", studentAttendanceInfoList);
            result.put("credits", "");
        }
        List<AttendanceRecords> list=attendanceRecordsList(studentAttendanceInfoList);
        result.put("credits", CalculationCredits(list));
        result.put("list", list);
        return Result.success(result);
    }

    /**
     * 考勤记录列表
     *
     * @param studentAttendanceInfoList 学生考勤信息列表
     * @return {@link List}<{@link AttendanceRecords}>
     */
    public List<AttendanceRecords> attendanceRecordsList(List<studentAttendanceInfo> studentAttendanceInfoList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AttendanceRecords attendanceRecordsUp = new AttendanceRecords();
        List<AttendanceRecords> attendanceRecordsList = new ArrayList<>();
        String clockDateUp;
        int i = -1;
        for (int a = 0; a < studentAttendanceInfoList.size(); a++) {
            if (a == 0) {
                clockDateUp = dateFormat.format(studentAttendanceInfoList.get(a).getUserCheckTime());
                attendanceRecordsUp.setClockDate(clockDateUp);
                attendanceRecordsUp.setGoWorkTime(dateFormats.format(studentAttendanceInfoList.get(a).getUserCheckTime()));
                attendanceRecordsUp.setFromWordTIme("");
                attendanceRecordsUp.setLength("0.0");
                attendanceRecordsList.add(attendanceRecordsUp);
                i++;
            } else {
                clockDateUp = dateFormat.format(studentAttendanceInfoList.get(a - 1).getUserCheckTime());
                String clockDateLast = dateFormat.format(studentAttendanceInfoList.get(a).getUserCheckTime());
                if (clockDateLast.equals(clockDateUp)) {
                    attendanceRecordsUp.setGoWorkTime(dateFormats.format(studentAttendanceInfoList.get(a).getUserCheckTime()));
                    attendanceRecordsUp.setFromWordTIme(dateFormats.format(studentAttendanceInfoList.get(a - 1).getUserCheckTime()));
                    Date FromWordTIme;
                    Date GoWorkTime;
                    try {
                        FromWordTIme = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attendanceRecordsUp.getFromWordTIme());
                        GoWorkTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attendanceRecordsUp.getGoWorkTime());
                    } catch (Exception e) {
                        throw new RuntimeException("时长计算出错");
                    }
                    long stamp = getTimeMilliseconds(FromWordTIme, GoWorkTime);
                    long stampHour = stamp / (60 * 60 * 1000) % 24;
                    long stampMin = stamp / (60 * 1000) % 60;
                    String length = stampHour + "." + stampMin;
                    attendanceRecordsUp.setLength(length);
                    attendanceRecordsList.set(i, attendanceRecordsUp);
                } else {
                    AttendanceRecords attendanceRecordsLast = new AttendanceRecords();
                    attendanceRecordsLast.setClockDate(clockDateLast);
                    attendanceRecordsLast.setFromWordTIme(dateFormats.format(studentAttendanceInfoList.get(a).getUserCheckTime()));
                    attendanceRecordsLast.setGoWorkTime("");
                    attendanceRecordsLast.setLength("0.0");
                    attendanceRecordsUp = attendanceRecordsLast;
                    attendanceRecordsList.add(attendanceRecordsUp);
                    i++;
                }
            }
        }
//        redisUtils.setCacheObject(userId, attendanceRecordsList, 120, TimeUnit.MINUTES);
        return attendanceRecordsList;
    }


    public static long getTimeMilliseconds(Date date1, Date date2) {
        long rtnLong = 0;
        //判断两个参数是否为空，为空直接返回
        if (date1 == null || date1 == null) {
            return rtnLong;
        }
        //使用第一个参数减去第二个参数
        rtnLong = date1.getTime() - date2.getTime();
        //如果结果小于0
        if (rtnLong < 0) {
            //再使用第二个参数减去第一个参数
            rtnLong = date2.getTime() - date1.getTime();
        }
        return rtnLong;
    }
}
