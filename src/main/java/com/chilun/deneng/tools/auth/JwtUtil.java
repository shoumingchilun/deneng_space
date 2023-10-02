package com.chilun.deneng.tools.auth;

import com.chilun.deneng.pojo.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @auther 齿轮
 * @create 2023-07-07-9:15
 * <p>
 * 使用方式：
 * 一、注入信息：
 * createJWT(
 * id=UUID.randomUUID().toString().replace("-", "")随机生成的id,
 * subject=发行者身份,
 * ttlMillis=有效时间,
 * map为公开存储的信息
 * )
 * 二、解析信息：
 * Claims claims = parseJWT(String JWT);
 * claims.get(map中的key,value的class)来获得map属性
 * 三、过期处理：
 * 调用parseJWT(String JWT)时，如果JWT过期，会抛出ExpiredJwtException异常，可使用catch处理
 */

public class JwtUtil {
    private static JwtUtil jwtUtil;

    public static JwtUtil createJWTUtil() {
        if (jwtUtil == null) {
            jwtUtil= new JwtUtil();
            jwtUtil.setSecretKey("nihao");
        }
        return jwtUtil;
    }

    private String secretKey;

    public static void main(String[] args) throws Exception {
        JwtUtil jwtUtil = JwtUtil.createJWTUtil();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "1");
        map.put("age", "ni");
        User user = new User();
        user.setId(1);
        user.setPassword("123");
        user.setType(4);
        map.put("user", user);
        String xmf = jwtUtil.createJWT(
                UUID.randomUUID().toString().replace("-", ""),
                "UserController.login",
                259200000,//过期时间为3天
                map);
        xmf = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTY0OTM3OTQsInVzZXIiOnsiaWQiOjEsIm5hbWUiOiLnlKjmiLczIiwicGFzc3dvcmQiOiI5ODc1MjQyNDIiLCJ0eXBlIjowLCJpbmZvIjpudWxsLCJjaGVja2VkIjowLCJ0YXNrRm9yY2VJZCI6bnVsbH19.7si52qFfcDCvnwWa6XaNhStGbiSVFpXcSY92PPxj52U";
        System.out.println(xmf);
        Claims claims = null;
        try {
            claims = jwtUtil.parseJWT(xmf);
            System.out.println(claims);
            System.out.println(jwtUtil.isExpiredJWT(claims));
            System.out.println(claims.get("name", String.class));
            System.out.println(claims.get("age", String.class));
        } catch (ExpiredJwtException e) {
            System.out.println("JWT has expired");
            e.printStackTrace();
        }
    }

    public String createJWT(String id, String subject, long ttlMillis, Map<String, Object> map) throws Exception {
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject) // 发行者
                .setIssuedAt(new Date()) // 发行时间
                .signWith(SignatureAlgorithm.HS256, secretKey); // 签名类型 与 密钥
//                .compressWith(CompressionCodecs.DEFLATE);// 对载荷进行压缩
        if (!CollectionUtils.isEmpty(map)) {
            builder.setClaims(map);
        }
        if (ttlMillis > 0) {
            builder.setExpiration(new Date(System.currentTimeMillis() + ttlMillis));
        }
        return builder.compact();
    }

    public Claims parseJWT(String jwtString) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtString)
                .getBody();
    }

    public boolean isExpiredJWT(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.before(new Date());
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}