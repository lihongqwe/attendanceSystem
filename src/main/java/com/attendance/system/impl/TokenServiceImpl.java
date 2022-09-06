package com.attendance.system.impl;

import com.attendance.config.AppConfig;
import com.attendance.system.TokenService;
import com.attendance.utils.RedisUtils;
import com.attendance.utils.StringUtils;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.concurrent.TimeUnit;

import static com.attendance.common.UrlConstant.GET_ACCESS_TOKEN_URL;


/**
 * 令牌服务impl
 *
 * @author lihong
 * @date 2022/09/01
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String AccessToken() {
        String token=redisUtils.getCacheObject("storageToken");
        //判断是否redis中的token是否为空
        if(StringUtils.isNotEmpty(token)){
            return token;
        }
        //重新获取DingTalkToken,并存入redis
        getNewAccessToken();
        return redisUtils.getCacheObject("storageToken");
    }


    /**
     * 获得新访问令牌
     */
    @Override
    public void getNewAccessToken() {
        DingTalkClient client = new DefaultDingTalkClient(GET_ACCESS_TOKEN_URL);
        OapiGettokenRequest request = new OapiGettokenRequest();
        OapiGettokenResponse response = null;
        request.setAppkey(appConfig.getAppKey());
        request.setAppsecret(appConfig.getAppSecret());
        request.setHttpMethod("GET");
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            log.info("获取DingTalkToken失败", e);
        }
        if(response!=null){
            if(StringUtils.isNotEmpty(response.getAccessToken())){
                //存入redis; response.getExpiresIn()过期时间
                redisUtils.setCacheObject("storageToken",response.getAccessToken(),response.getExpiresIn().intValue(), TimeUnit.SECONDS);
                log.info("获取DingTalkToken,redis成功");
            }
        }
    }



}
