package com.attendance.controller;


import com.attendance.common.Result;
import com.attendance.domain.User;
import com.attendance.system.sysUserService;
import com.attendance.utils.StringUtils;
import com.attendance.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private sysUserService sysUserService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * 获取用户列表
     *
     * @param Page 页面
     * @return {@link Result}
     */
    @GetMapping("/list")
    public Result GetUserList(@RequestParam(value = "Page") Integer Page, HttpServletRequest request){
        if(Objects.equals(tokenUtils.getLoginUser(request).getUser().getRoles(), "admin")
           || Objects.equals(tokenUtils.getLoginUser(request).getUser().getRoles(), "superadmin")){
            return Result.success(sysUserService.GetUserList(Page));
        }
        return Result.error();
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param user 用户
     * @return {@link Result}
     */
    @PostMapping
    public Result GetUserInfo(@RequestBody User user){
        if(StringUtils.isNotEmpty(user.getUsername())){
           return sysUserService.GetUserInfo(user.getUsername());
        }
        return Result.error("用户名不能为空");
    }

    /**
     * 修改用户信息
     *
     * @return {@link Result}
     */
    @PostMapping("/uptate")
    public Result uptateuserinfo(@RequestBody User user){
        return sysUserService.uptateuserinfo(user);
    }

}
