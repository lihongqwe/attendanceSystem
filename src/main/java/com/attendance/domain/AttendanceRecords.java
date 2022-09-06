package com.attendance.domain;

/**
 * 考勤记录
 *
 * @author lihong
 * @date 2022/09/03
 */
public class AttendanceRecords {
    private String clockDate;
    private String length;
    private String goWorkTime;
    private String fromWordTIme;

    public String getClockDate() {
        return clockDate;
    }

    public void setClockDate(String clockDate) {
        this.clockDate = clockDate;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getGoWorkTime() {
        return goWorkTime;
    }

    public void setGoWorkTime(String goWorkTime) {
        this.goWorkTime = goWorkTime;
    }

    public String getFromWordTIme() {
        return fromWordTIme;
    }

    public void setFromWordTIme(String fromWordTIme) {
        this.fromWordTIme = fromWordTIme;
    }

    @Override
    public String toString() {
        return "AttendanceRecords{" +
                "clockDate='" + clockDate + '\'' +
                ", length='" + length + '\'' +
                ", goWorkTime='" + goWorkTime + '\'' +
                ", fromWordTIme='" + fromWordTIme + '\'' +
                '}';
    }
}
