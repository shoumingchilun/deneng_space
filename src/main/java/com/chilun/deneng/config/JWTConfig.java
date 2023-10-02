package com.chilun.deneng.config;

import com.chilun.deneng.tools.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @auther 齿轮
 * @create 2023-10-02-16:06
 */
@Configuration
public class JWTConfig {
    @Autowired
    Environment env;

    @Bean
    public JwtUtil getJwtUtil(){
        JwtUtil jwtUtil = JwtUtil.createJWTUtil();
        jwtUtil.setSecretKey(env.getProperty("JWT.key",String.class,"hello"));
        return jwtUtil;
    }

}
