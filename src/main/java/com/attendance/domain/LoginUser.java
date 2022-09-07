package com.attendance.domain;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户唯一标识
     */
    private String token;


    /**
     * 用户信息
     */
    private User User;


    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    private  String userType;

    private studentUserInfo  studentUserInfo;

    public com.attendance.domain.studentUserInfo getStudentUserInfo() {
        return studentUserInfo;
    }

    public void setStudentUserInfo(com.attendance.domain.studentUserInfo studentUserInfo) {
        this.studentUserInfo = studentUserInfo;
    }

    public User getUser() {
        return User;
    }

    public void setUser(com.attendance.domain.User user) {
        User = user;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public LoginUser(){

    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSysUser(User User) {
        this.User = User;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public User getSysUser() {
        return User;
    }

    public LoginUser(User User) {
        this.User = User;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return User.getPassword();
    }

    @Override
    public String getUsername() {
        return User.getUsername();
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
