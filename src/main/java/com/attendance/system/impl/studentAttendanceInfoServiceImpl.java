package com.attendance.system.impl;

import com.attendance.common.Result;
import com.attendance.domain.AttendanceRecords;
import com.attendance.domain.studentAttendanceInfo;
import com.attendance.mapper.studentAttendanceInfoMapper;
import com.attendance.system.studentAttendanceInfoService;
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

    int CONVERSION=16;

    /**
     * 获取学生出勤信息
     * @param userId 用户id
     * @return {@link Result}
     */
    @Override
    public Result GetStudentAttendanceInfo(String userId) {
        //获取该用户的所有打卡信息
        List<studentAttendanceInfo> studentAttendanceInfoList=studentAttendanceInfoMapper.selectAll(userId);
        if(studentAttendanceInfoList.size()==0){
            return Result.error("暂无该用户信息");
        }
        Result result=new Result();
        result.put("learningTime",calculateLearningTime(attendanceRecordsList(studentAttendanceInfoList),16));
        result.put("list",attendanceRecordsList(studentAttendanceInfoList));
        return Result.success(result);
    }


    /**
     * 通过时间选择学生考勤信息
     *
     * @param userId     用户id
     * @param conversion 转换率
     * @param startDate  开始日期
     * @param EndDate    结束日期
     * @return {@link Result}
     */
    @Override
    public Result GetStudentAttendanceInfoByTime(String userId, int conversion, String startDate, String EndDate) {
        CONVERSION=conversion;
        return getResult(userId, startDate, EndDate);
    }

    @Override
    public Result GetStudentAttendanceInfoByTime(String userId, String startDate, String EndDate) {
        return getResult(userId, startDate, EndDate);
    }



    private Result getResult(String userId, String startDate, String EndDate) {
        String startTime= startDate+ " "+"23:59:59";
        String endTime =EndDate+" "+ "00:00:00";
        List<studentAttendanceInfo> studentAttendanceInfoList=studentAttendanceInfoMapper.searchAllByTime(userId,startTime,endTime);
        Result result=new Result();
        result.put("learningTime",calculateLearningTime(attendanceRecordsList(studentAttendanceInfoList),CONVERSION));
        result.put("list",attendanceRecordsList(studentAttendanceInfoList));
        return Result.success(result);
    }


    /**
     * 计算学习时间
     * 1学时 ==16工时,1学分==3学时
     *
     * @param attendanceRecordsList 考勤记录列表
     * @return {@link Integer}
     */
    public int calculateLearningTime(List<AttendanceRecords> attendanceRecordsList,int conversion ){
        float LearningTime = 0;
        for(AttendanceRecords attendanceRecords:attendanceRecordsList){
            float length= Float.parseFloat(attendanceRecords.getLength());
            LearningTime +=length;
        }
        return (int) (LearningTime/conversion);
    }


    /**
     * 考勤记录列表
     *
     * @param studentAttendanceInfoList 学生考勤信息列表
     * @return {@link List}<{@link AttendanceRecords}>
     */
    public List<AttendanceRecords> attendanceRecordsList(List<studentAttendanceInfo> studentAttendanceInfoList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        AttendanceRecords attendanceRecordsUp=new AttendanceRecords();
        List<AttendanceRecords> attendanceRecordsList=new ArrayList<>();
        String clockDateUp;
        int i = -1;
        for (int a=0;a<studentAttendanceInfoList.size();a++) {
            if(a == 0){
                clockDateUp = dateFormat.format(studentAttendanceInfoList.get(a).getUserCheckTime());
                attendanceRecordsUp.setClockDate(clockDateUp);
                attendanceRecordsUp.setGoWorkTime(dateFormats.format(studentAttendanceInfoList.get(a).getUserCheckTime()));
                attendanceRecordsUp.setFromWordTIme("");
                attendanceRecordsUp.setLength("0.0");
                attendanceRecordsList.add(attendanceRecordsUp);
                i++;
            }else {
                clockDateUp = dateFormat.format(studentAttendanceInfoList.get(a-1).getUserCheckTime());
                String clockDateLast = dateFormat.format(studentAttendanceInfoList.get(a).getUserCheckTime());
                if(clockDateLast.equals(clockDateUp)){
                    attendanceRecordsUp.setGoWorkTime(dateFormats.format(studentAttendanceInfoList.get(a).getUserCheckTime()));
                    attendanceRecordsUp.setFromWordTIme(dateFormats.format(studentAttendanceInfoList.get(a-1).getUserCheckTime()));
                    Date FromWordTIme;
                    Date GoWorkTime;
                    try{
                        FromWordTIme=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attendanceRecordsUp.getFromWordTIme());
                        GoWorkTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attendanceRecordsUp.getGoWorkTime());
                    }
                    catch (Exception e){
                        throw new RuntimeException("时长计算出错");
                    }
                    long stamp = getTimeMilliseconds(FromWordTIme,GoWorkTime);
                    long stampHour = stamp / (60 * 60 * 1000) % 24;
                    long stampMin = stamp / (60 * 1000) % 60;
                    String length = stampHour + "."+stampMin;
                    attendanceRecordsUp.setLength(length);
                    attendanceRecordsList.set(i, attendanceRecordsUp);
                }else {
                    AttendanceRecords attendanceRecordsLast = new AttendanceRecords();
                    attendanceRecordsLast.setClockDate(clockDateLast);
                    attendanceRecordsLast.setFromWordTIme(dateFormats.format(studentAttendanceInfoList.get(a).getUserCheckTime()));
                    attendanceRecordsLast.setLength("0.0");
                    attendanceRecordsUp = attendanceRecordsLast;
                    attendanceRecordsList.add(attendanceRecordsUp);
                    i++;
                }
            }
        }
        return attendanceRecordsList;
    }



    public static long getTimeMilliseconds(Date date1, Date date2){
        long rtnLong = 0;
        //判断两个参数是否为空，为空直接返回
        if (date1 == null || date1 == null) {
            return rtnLong;
        }
        //使用第一个参数减去第二个参数
        rtnLong = date1.getTime() - date2.getTime();
        //如果结果小于0
        if (rtnLong < 0){
            //再使用第二个参数减去第一个参数
            rtnLong = date2.getTime() - date1.getTime();
        }
        return rtnLong;
    }
}
