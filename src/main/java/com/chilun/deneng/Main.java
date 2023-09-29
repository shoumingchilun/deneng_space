package com.chilun.deneng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @auther 齿轮
 * @create 2023-09-28-10:32
 */
@SpringBootApplication
@MapperScan("com.chilun.deneng.dao")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
