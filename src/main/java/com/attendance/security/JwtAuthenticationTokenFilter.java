package com.attendance.security;

import com.attendance.domain.LoginUser;
import com.attendance.utils.SecurityUtils;
import com.attendance.utils.StringUtils;
import com.attendance.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        if(httpServletRequest.getRequestURI().equals("/login")
                || httpServletRequest.getRequestURI().equals("/register")
        )
        {
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        LoginUser loginUser = tokenUtils.getLoginUser(httpServletRequest);
        if(StringUtils.isNotNull(loginUser)&&StringUtils.isNull(SecurityUtils.getAuthentication())){
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
