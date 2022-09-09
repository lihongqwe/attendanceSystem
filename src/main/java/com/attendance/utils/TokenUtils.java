package com.attendance.utils;


import com.attendance.common.Constants;
import com.attendance.domain.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class TokenUtils {
    @Autowired
    private  RedisUtils redisUtils;
    // 令牌密钥
    @Value("${token.secret}")
    private String secret;


    //过期时间
    @Value("${token.expireTime}")
    private int expireTime;

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;


    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static Long MILLIS_MINUTE_TEN =120 * MILLIS_MINUTE;

    /**
     * 通过请求获取Token令牌
     * @param request 请求实例
     * @return 令牌
     */
    private String getToken(HttpServletRequest request)
    {
        return request.getHeader(header);
    }

    /**
     * 从缓存中取出登录的用户
     * @param request 请求
     * @return 登录的用户实体
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        String token = getToken(request);
        if(token!=null) {
            try {
                Claims claims = getClaims(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.CLAIMS_KEY);
                String userKey = getTokenKey(uuid);
                return redisUtils.getCacheObject(userKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 通过令牌获取Claims信息
     * @param token 令牌
     * @return Claims信息
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * 创建Token令牌
     * @return 令牌
     */
    public String createToken (LoginUser loginUser){
        String uuid = UUIDUtils.generateUUID();
        loginUser.setToken(uuid);
        setLoginUser(uuid,loginUser,30);
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.CLAIMS_KEY,uuid);
        return  createToken(claims);
    }

    /**
     * 删除缓存中的用户
     * @param uuid uuid
     */
    public void deleteLoginUser(String uuid){
        redisUtils.deleteObject(getTokenCacheKey(uuid));
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 刷新登录的用户缓存
     * @param loginUser 登录的用户实体
     */
    public void refreshLoginUser(LoginUser loginUser) {
        String uuid = getTokenKey(loginUser.getToken());
        redisUtils.setCacheObject(getTokenCacheKey(uuid),loginUser,expireTime,TimeUnit.MINUTES);
    }


    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 将登录的用户写入缓存
     * @param token token
     * @param loginUser 登录的用户
     * @param expireTime 缓存过期时间
     */
    public void setLoginUser(String token, LoginUser loginUser,int expireTime){
        redisUtils.setCacheObject(getTokenCacheKey(token), loginUser, expireTime, TimeUnit.MINUTES);
    }


    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisUtils.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取uuid信息
     * @param request 请求实例
     * @return uuid
     */
    public String getUUID(HttpServletRequest request) {
        String token = getToken(request);
        if(StringUtils.isNotEmpty(token)) {
            Claims claims = getClaims(token);
            return claims.get(Constants.CLAIMS_KEY).toString();
        }
        return null;
    }

    /**
     * 获取Token缓存键
     * @param key 原始键
     * @return 缓存键
     */
    public String getTokenCacheKey(String key) {
        return  key;
    }

    private String getTokenKey(String uuid)
    {
        return  uuid;
    }

}
