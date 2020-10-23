package org.demo.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.demo.security.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

/**
 * 关于token的一些操作
 * @author Administrator
 */
public abstract class AbstractTokenService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 生成一个token
     * @param username
     * @return
     */
    public String generateToken(String username) {
        String token = TokenUtils.generateToken(username, getExpire(), getKey());
        return token;
    }

    /**
     * 是否权限校验能够通过
     * @param authHeader
     * @return
     */
    public boolean isAuthentication(HttpServletRequest request, String authHeader) {
        boolean isAuthentication = false;
        String username = getUsernameByHeader(authHeader);
        if (username != null) {
            // 用户信息或者角色信息是否能匹配上
            if (isAuthenticationUrl(request, username)) {
                isAuthentication = true;
            }
        }
        return isAuthentication;
    }

    /**
     * 主要是角色 鉴权
     * @param request 请求的信息
     * @param username 用户名 从token中解析出来的 并且已经保证么有过期
     * @return
     */
    protected abstract boolean isAuthenticationUrl(HttpServletRequest request, String username);

    /**
     * 取得加密的key
     * @return
     */
    protected abstract String getKey();

    /**
     * 取得token的过期时间
     * @return
     */
    protected abstract long getExpire();


    /**
     * 取得加密的key
     * @return
     */
    protected abstract String getTokenHeader();

    /**
     * 取得token的过期时间
     * @return
     */
    protected abstract String getTokenPrefix();

    /**
     * 根据header的信息取得用户名
     * @param authHeader
     * @return 用户名 没有的时候为""
     * @throws AuthenticationException
     */
    public String getUsernameByHeader(String authHeader) {
        // authHeader
        authHeader = authHeader.replace(getTokenPrefix(), "");

        // 这个token是否正确 并且是否过期
        Claims claims = null;
        try {
            claims = TokenUtils.getClaimByToken(authHeader, getKey());
        } catch (JwtException e) {
            logger.error(e.toString());
        }
        if (claims == null || TokenUtils.isTokenExpired(claims.getExpiration())) {
            return "";
        }
        return claims.getSubject();
    }
}
