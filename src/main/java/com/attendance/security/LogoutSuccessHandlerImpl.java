package com.attendance.security;

import com.attendance.utils.StringUtils;
import com.attendance.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
    {
        String userUUID=tokenUtils.getUUID(httpServletRequest);
        if(StringUtils.isNotNull(userUUID)) {
            tokenUtils.deleteLoginUser(userUUID);
        }
    }
}
