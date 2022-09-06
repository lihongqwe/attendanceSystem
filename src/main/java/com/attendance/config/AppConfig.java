package com.attendance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${dingtalk.app_key}")
    private String appKey;

    @Value("${dingtalk.app_secret}")
    private String appSecret;

    @Value("${dingtalk.corp_id}")
    private String corpId;

    @Value("${dingtalk.agent_id}")
    private Long agentId;

    @Value("${dingtalk.SSOsecret}")
    private String SSOsecret;

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getSSOsecret() {
        return SSOsecret;
    }

    public void setSSOsecret(String SSOsecret) {
        this.SSOsecret = SSOsecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
