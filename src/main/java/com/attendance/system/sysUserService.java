package com.attendance.system;


import com.attendance.common.Result;
import com.attendance.domain.Register;
import com.attendance.domain.User;
import java.util.Map;

public interface sysUserService {

    /**
     * 选择用户用户名
     *
     * @param username 用户名
     * @return {@link User}
     */
    public User selectUserByUserName(String username);

    /**
     * 注册用户
     *
     * @param register 注册
     * @return {@link Result}
     */
    public Result RegisteredUser(Register register);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return {@link Result}
     */
    public Result GetUserInfo(String username);

    /**
     * 获取用户列表
     *
     * @param Page 页面
     * @return {@link Result}
     */
    public Map<String,Object> GetUserList(Integer Page);

    /**
     * uptateuserinfo
     *
     * @param user 用户
     * @return {@link Result}
     */
    public Result uptateuserinfo( User user);


    /**
     * 修改个人信息
     * @param user
     * @return
     */
    public Result uptateprofile(User user);


    /**
     * 修改个人密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public Result resetPassword(String oldPassword, String newPassword);


    /**
     * 选择用户信息
     *
     * @return {@link Result}
     */
    public Result selectUserInfo();

}
