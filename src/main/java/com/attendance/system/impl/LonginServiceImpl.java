package com.attendance.system.impl;


import com.attendance.common.Result;
import com.attendance.domain.LoginUser;
import com.attendance.exception.GeneralException;
import com.attendance.system.LonginService;
import com.attendance.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class LonginServiceImpl implements LonginService {

    @Autowired
     private TokenUtils tokenUtils;

    @Resource
    private AuthenticationManager authenticationManager;


    @Override
    public Result login(String username, String password) {
        // 用户验证
        Authentication authentication;
        try{
           //该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (Exception e){
           return Result.error("用户名或密码错误");
//           return Result.error("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        Result resul=new Result();
        String token=tokenUtils.createToken(loginUser);
        resul.put("token",token);
        String roles=loginUser.getUser().getRoles();
        resul.put("roles",roles);
        return Result.success(resul);
    }
}
