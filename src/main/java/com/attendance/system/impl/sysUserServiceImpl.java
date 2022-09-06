package com.attendance.system.impl;

import com.attendance.common.Result;
import com.attendance.domain.LoginUser;
import com.attendance.domain.Register;
import com.attendance.domain.User;
import com.attendance.domain.studentUserInfo;
import com.attendance.mapper.UserMapper;
import com.attendance.mapper.studentUserInfoMapper;
import com.attendance.system.sysUserService;
import com.attendance.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Service
public class sysUserServiceImpl implements sysUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private studentUserInfoMapper studentUserInfoMapper;

    




    /**
     * 选择用户用户名
     *
     * @param username 用户名
     * @return {@link User}
     */
    @Override
    public User selectUserByUserName(String username) {
        return userMapper.selectUserByUserName(username);
    }


    /**
     * 注册用户
     *
     * @param register 注册
     * @return {@link Result}
     */
    @Override
    public Result RegisteredUser(Register register) {
        User user=userMapper.selectUserByUserName(register.getUsername());
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        User user1=new User();
        if(StringUtils.isNull(user)){
            user1.setUsername(register.getUsername());
            user1.setRoles("common");
            user1.setPassword(bCryptPasswordEncoder.encode(register.getPassword()));
            int result= userMapper.insertUser(user1);
            if(result!=1){
                return  Result.error(400,"注册失败！！！");
            }
            return Result.success(200,"注册成功,请登录！！！");
        }
        return Result.error(400,"该用户已存在,请重新输入！！！");
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return {@link Result}
     */
    @Override
    public Result GetUserInfo(String username) {
        Result result=new Result();
        if(username.length()==11){
            return  result.put("userInfo",studentUserInfoMapper.selectByPhoneNumber(username));
        }
        if(StringUtils.isNotNull(userMapper.selectUserByUserName(username))){
            result.put("userInfo",userMapper.selectUserByUserName(username));
            return Result.success(result);
        }
        return Result.error("没有该用户信息");
    }

    /**
     * 获取用户列表
     *
     * @param Page 页面
     * @return {@link Result}
     */
    @Override
    public Map<String,Object> GetUserList(Integer Page) {
        Integer pageSize=10;
        Map<String, Object> map=new HashMap<>();
        List<User> user =userMapper.selectAllUser();
        List<studentUserInfo> studentUserInfoList=studentUserInfoMapper.selectAll();
        for(studentUserInfo studentUserInfo:studentUserInfoList){
            User user1=new User();
            user1.setUserId(studentUserInfo.getUserId());
            user1.setRoles(studentUserInfo.getRoles());
            user1.setPassword(studentUserInfo.getPassword());
            user1.setUsername(studentUserInfo.getPhonenumber());
            user1.setNickName(studentUserInfo.getUserName());
            user1.setPhonenumber((int) Long.parseLong(studentUserInfo.getPhonenumber()));
            user.add(user1);
        }
        List list=ListUtils.startPage(user,Page,pageSize);
        map.put("list",list);
        map.put("total",user.size()/pageSize+1);
        return  map;
    }



    /**
     *更新用户信息
     *
     * @param user 用户
     * @return {@link Result}
     */
    @Override
    public Result uptateuserinfo(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        int a=userMapper.updateByPrimaryKey(user);
        if(a==1){
            return Result.success("修改用户信息成功");
        }
        return Result.error("修改用户信息失败");
    }


    /**
     *更新个人信息
     *
     * @param user 用户
     * @return {@link Result}
     */
    @Override
    public Result uptateprofile(User user) {
        user.setUserId((loginUser().getUser().getUserId()));
        user.setRoles(loginUser().getUser().getRoles());
        user.setPassword(loginUser().getPassword());
        if(userMapper.updateByPrimaryKey(user)==1){
            return Result.success("修改成功");
        }
        loginUser().setUser(user);
        // 刷新登录的用户缓存
        tokenUtils.refreshLoginUser(loginUser());
        return Result.error();
    }


    /**
     * 重置密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return {@link Result}
     */
    @Override
    public Result resetPassword(String oldPassword, String newPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(oldPassword,loginUser().getUser().getPassword()))
        {
            return  Result.error("旧密码错误!");
        }
        String newEncryptedPassword = encryptPassword(newPassword);
        User User = new User();
        User.setPassword(newEncryptedPassword);
        User.setUserId(loginUser().getUser().getUserId());
        User.setRoles(loginUser().getUser().getRoles());
        if(loginUser().getUser().getUserId().length()==11){
            studentUserInfoMapper.updateByPrimaryKey(loginUser().getUser().getUserId(),newPassword);
        }else {
            userMapper.updateByPrimaryKey(User);
        }
        loginUser().setSysUser(userMapper.selectByPrimaryKey(loginUser().getSysUser().getUserId()));
        tokenUtils.refreshLoginUser(loginUser());
        return Result.success("修改成功");
    }

    /**
     * 查看用户信息
     *
     * @return {@link Result}
     */
    @Override
    public Result selectUserInfo() {
        LoginUser loginUser = tokenUtils.getLoginUser(ServletUtils.getRequest());
        if(loginUser.getUser().getUsername().length()==11){
            return Result.success(studentUserInfoMapper.selectByPhoneNumber(loginUser.getUser().getUsername()));
        }
        return Result.success(userMapper.selectUserByUserName(loginUser.getUser().getUsername()));
    }


    /**
     * 加密密码
     *
     * @param password 密码
     * @return {@link String}
     */
    private String encryptPassword(String password){
        if(password.equals("")){
            throw new RuntimeException("密码不能为空");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private LoginUser loginUser(){
        return tokenUtils.getLoginUser(ServletUtils.getRequest());
    }


}
