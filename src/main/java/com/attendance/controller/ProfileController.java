package com.attendance.controller;

import com.attendance.common.Result;
import com.attendance.domain.User;
import com.attendance.system.sysUserService;
import com.attendance.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class ProfileController {

    @Autowired
    private sysUserService sysUserService;

    /**
     * 更新个人信息
     *
     * @param user 用户
     * @return {@link Result}
     */
    @PostMapping("/profile")
    public Result uptateprofile(@RequestBody User user){
        return sysUserService.uptateprofile(user);
    }

    /**
     * 重置密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return {@link Result}
     */
    @GetMapping("profile/resetPassword")
    public Result resetPassword(
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword" ) String newPassword){
        if(StringUtils.isNull(oldPassword) || StringUtils.isNull(newPassword)){
            return Result.error("密码输入不合法");
        }
        return sysUserService.resetPassword(oldPassword,newPassword);
    }

    /**
     * 用户信息
     *
     * @return {@link Result}
     */
    @GetMapping("/info")
    public Result userinfo(){
        return sysUserService.selectUserInfo();
    }

}
