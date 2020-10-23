package org.demo.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * token 工具
 * @author Administrator
 */
public class TokenUtils {

    /**
     * 做成token
     * @param username 用户名
     * @param expire 过期时间 秒
     * @param key 加密key
     * @return
     */
    public static String generateToken(final String username, final long expire, final String key) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        String token = getToken(username, nowDate, expireDate, key);
        return token;
    }

    /**
     * 从token中取得信息
     * @param token
     * @return
     */
    public static Claims getClaimByToken(String token, final String key) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    private static String getToken(final String username, final Date nowDate, final Date expireDate, final String key) {
        return Jwts.builder()
                // header
                // 声明类型typ，表示这个令牌（token）的类型（type），JWT令牌统一写为JWT
                .setHeaderParam("typ", "JWT")
                // 声明加密的算法alg，通常直接使用HMACSHA256，就是HS256了，也可以使用RSA, 支持很多算法
                // (HS256、HS384、HS512、RS256、RS384、RS512、ES256、ES384、ES512、PS256、PS384)
                .setHeaderParam("alg", "hs512")

                // payload
                // jwt签发者
                .setIssuer("org.account.cl")
                .setSubject(username)
                .setIssuedAt(nowDate)
                // jwt的过期时间，这个过期时间必须要大于签发时间
                .setExpiration(expireDate)

                // signature
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }
}
