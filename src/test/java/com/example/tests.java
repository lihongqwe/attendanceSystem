package com.example;


import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static jdk.internal.org.objectweb.asm.Type.getType;

public class tests{

    @Test
    public void test(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date date = new Date();
        System.out.println(getType(dateFormat.format(date)));
    }
    @Test
    public void  test2(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Calendar startTime=Calendar.getInstance();
        startTime.add(Calendar.DATE,-1);
        System.out.println(dateFormat.format(startTime.getTime()));
        // 查询考勤打卡记录的起始工作日
//        requestAttendanceListRequest.setWorkDateFrom(dateFormat.format(startTime.getTime()));
        // 查询考勤打卡记录的结束工作日
        Calendar endTime=Calendar.getInstance();
        endTime.add(Calendar.DATE,-2);
        // 查询考勤打卡记录的结束工作日
        System.out.println(dateFormat.format(endTime.getTime()));
//        requestAttendanceListRequest.setWorkDateFrom(dateFormat.format(endTime.getTime()));
    }

    @Test
    public void test3(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date startdate = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(startdate);
        now.add(Calendar.HOUR, -49);
        // 查询考勤打卡记录的起始工作日
        // 查询考勤打卡记录的结束工作日
        Date Endtdate = new Date();
        Calendar sss = Calendar.getInstance();
        sss.setTime(Endtdate);
        sss.add(Calendar.HOUR, -14);//HOURs
        System.out.println(dateFormat.format(now.getTime()));
        System.out.println(dateFormat.format(sss.getTime()));

    }
    @Test
    public void test4(){
        Date FromWordTIme;
        Date GoWorkTime;
        try{
            FromWordTIme=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-09-03 12:11:11");
            GoWorkTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-09-02 12:22:11");
        }
        catch (Exception e){
            throw new RuntimeException("shdf");
        }
        System.out.println(FromWordTIme);
        System.out.println(GoWorkTime);

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

    @Test
    @Scheduled(cron = "0/10 * * * * ?")

    public void test6(){
        System.out.println(6666666);
    }
}
