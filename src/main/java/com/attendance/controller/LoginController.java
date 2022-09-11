package com.attendance.controller;

import com.attendance.common.Result;
import com.attendance.domain.LoginBody;
import com.attendance.domain.Register;
import com.attendance.system.LonginService;
import com.attendance.system.sysUserService;
import com.attendance.utils.StringUtils;
import com.attendance.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Autowired
    private LonginService longinService;

    @Autowired
    private sysUserService sysUserService;


    @PostMapping("/login")
    public Result login(@RequestBody LoginBody loginBody){
        return longinService.login(loginBody.getUsername(),loginBody.getPassword());
    }

    @PostMapping("/register")
    public  Result registered(@RequestBody Register register){
        if(StringUtils.isNull(register.getUsername())&&StringUtils.isNull(register.getPassword())){
            return Result.error("用户名和密码不能为空");
        }
        if(register.getPassword().equals(register.getDeterminePassword())){
            return sysUserService.RegisteredUser(register);
        }
        return Result.error(400,"两次密码不一致请重新事输入");
    }




}
