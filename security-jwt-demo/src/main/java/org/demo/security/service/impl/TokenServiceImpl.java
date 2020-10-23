package org.demo.security.service.impl;

import org.demo.security.service.AbstractTokenService;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */
@Service
public class TokenServiceImpl extends AbstractTokenService implements EnvironmentAware {

    private static final String DEFAULT_TOKEN_HEADER = "Authorization";
    private static final String DEFAULT_TOKEN_PREFIX = "account ";
    private final static String DEFAULT_KEY = "9f6d5c6b4519c4bba50862006a18f493";

    /**
     * 默认为30分钟 单位是秒
     */
    private final static long DEFAULT_EXPIRE = 1800;

    private Environment environment;

    @Override
    public boolean isAuthenticationUrl(HttpServletRequest request, String username) {
        return false;
    }

    @Override
    public String getKey() {
        return environment.getProperty("spring.token.key", DEFAULT_KEY);
    }

    @Override
    public long getExpire() {
        return Long.valueOf(environment.getProperty("spring.token.expire", DEFAULT_EXPIRE + ""));
    }

    @Override
    public String getTokenHeader() {
        return environment.getProperty("spring.token.header", DEFAULT_TOKEN_HEADER);
    }

    @Override
    public String getTokenPrefix() {
        return environment.getProperty("spring.token.header.prefix", DEFAULT_TOKEN_PREFIX);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
