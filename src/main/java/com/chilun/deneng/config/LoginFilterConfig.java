package com.chilun.deneng.config;

import com.chilun.deneng.filter.LoginFilter;
import jakarta.servlet.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


/**
 * @auther 齿轮
 * @create 2023-10-01-16:41
 */
@Configuration
public class LoginFilterConfig {

    /**
     * 注册 过滤器 Filter
     */
    @Bean
    public FilterRegistrationBean<Filter> LoginFilterConfigRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new LoginFilter());
        registration.addUrlPatterns("/*"); // 设置过滤器要拦截的路径，这里设置为拦截所有路径
        //设置名称
        registration.setName("LoginFilter");
        //设置过滤器链执行顺序
        registration.setOrder(1);
        return registration;
    }
}

