package com.attendance.domain;

public class studentUserInfo {

    private Integer id;

    private String userId;

    private String username;

    private String phonenumber;

    private String password;

    private String roles;

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName == null ? null : userName.trim();
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
        return "studentUserInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + username + '\'' +
                ", phonenumber=" + phonenumber +
                '}';
    }
}