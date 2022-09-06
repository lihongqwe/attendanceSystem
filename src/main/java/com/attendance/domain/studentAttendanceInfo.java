package com.attendance.domain;

import java.util.Date;

public class studentAttendanceInfo {
    private Integer id;

    private String userId;


    private Date userCheckTime;

    private String timeResult;

    private String checkType;

    private Long groupid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getUserCheckTime() {
        return userCheckTime;
    }

    public void setUserCheckTime(Date userCheckTime) {
        this.userCheckTime = userCheckTime;
    }

    public String getTimeResult() {
        return this.timeResult;
    }

    public void setTimeResult(String timeResult) {
        this.timeResult = timeResult == null ? null : timeResult.trim();
    }

    public String getCheckType() {
        return this.checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType == null ? null : checkType.trim();
    }

    public Long getGroupid() {
        return groupid;
    }


    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    @Override
    public String toString() {
        return "studentAttendanceInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userCheckTime=" + userCheckTime +
                ", timeResult='" + timeResult + '\'' +
                ", checkType='" + checkType + '\'' +
                ", groupid=" + groupid +
                '}';
    }
}