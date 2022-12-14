package com.attendance.system.impl;

import com.attendance.domain.LoginUser;
import com.attendance.domain.User;
import com.attendance.domain.studentUserInfo;
import com.attendance.exception.UsernameNotFoundException;
import com.attendance.system.studentUserInfoService;
import com.attendance.system.sysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private sysUserService sysUserService;

    @Autowired
    private studentUserInfoService studentUserInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //登录用户是学生
        User user;
        if (username.length() == 11) {
            studentUserInfo studentUserInfo = studentUserInfoService.GetStudentUserInfoByPhoneNumber(username);
            if (studentUserInfo == null) {
                throw new UsernameNotFoundException("学生" + username + "信息不存在,请联系管理员");
            }
            user = new User();
            user.setUserId((studentUserInfo.getUserId()));
            user.setUsername(studentUserInfo.getPhonenumber());
            user.setRoles(studentUserInfo.getRoles());
            user.setPhonenumber(studentUserInfo.getPhonenumber());
            user.setPassword(studentUserInfo.getPassword());
        }else {
            user = sysUserService.selectUserByUserName(username);
        }
        //登录用户是管理员
        if (user == null) {
            throw new RuntimeException("用户" + username + "不存在");
        }
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(User user) {
        return new LoginUser(user);
    }


}
