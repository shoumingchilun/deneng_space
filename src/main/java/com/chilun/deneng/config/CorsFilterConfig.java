package com.chilun.deneng.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @auther 齿轮
 * @create 2023-10-11-18:02
 */
@Configuration
public class CorsFilterConfig  {
    /**
     * @Description :跨域访问过滤器，设置执行顺序
     * @Date 19:55 2021/6/15 0015
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean<org.springframework.web.filter.CorsFilter>
     **/
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        //设置执行顺序，数字越小越先执行
        bean.setOrder(0);
        return bean;
    }
}
