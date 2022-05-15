package com.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class JwtTokenUtils {
    //加载secjwt.jks文件
    private static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("secjwt.jks");
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, "123456".toCharArray());
            privateKey = (PrivateKey) keyStore.getKey("jwt", "123456".toCharArray());
            publicKey = keyStore.getCertificate("jwt").getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成令牌
     * @param subject 用户id，即登陆的用户名（工号）
     * @param expirationSeconds 过期时间（秒）
     * @return 生成的JWT令牌
     * 作者：张国永 2021年11月17日
     */
    public static String generateToken(String subject, int expirationSeconds) { //根据用户id(subject)和指定的有效时间生成令牌
        return Jwts.builder()
                .setClaims(null)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 从令牌读取用户名（工号）
     * @param token 前端传来的令牌
     * @return 返回用户名（工号）
     * 作者：张国永 2021年11月17日
     */
    public static String parseToken(String token) {
        String subject = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token).getBody();
            subject = claims.getSubject();
        } catch (Exception e) {
        }
        return subject;
    }
}
