package com.chilun.deneng.filter;

import com.chilun.deneng.tools.auth.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @auther 齿轮
 * @create 2023-10-01-16:43
 */

@Component
public class LoginFilter implements Filter {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 获取请求路径
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        // 判断请求路径是否为"/user/login"或"/user/register"
        if ("/user/login".equals(requestURI) ||
                "/user/register".equals(requestURI) ||
                "/user/logOut".equals(requestURI) ||
                "/index".equals(requestURI)) {
            // 直接放行
            chain.doFilter(request, response);
        } else {
            //判断session中有无user
            boolean sessionIsValued = false;
            if (((HttpServletRequest) request).getSession().getAttribute("user") != null) {
                sessionIsValued = true;
            }
            // 判断是否存在名为 "JWT" 的 Cookie
            boolean JWTisValued = false;
            Cookie[] cookies = ((HttpServletRequest) request).getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {
                    try {
                        System.out.println(cookie.getValue());
                        if (!jwtUtil.isExpiredJWT(jwtUtil.parseJWT(cookie.getValue()))) {//未过期，说明jwt有效，直接放行
                            JWTisValued = true;
                            break;
                        }
                    } catch (Exception ignored) {//过期或无效会报错，忽略
                    }
                }
            }
            if (JWTisValued || sessionIsValued) {//通过则放行
                chain.doFilter(request, response);
            } else {//否则报错
                response.getWriter().write("{\n" +
                        "    \"message\": \"Not Logged in!\",\n" +
                        "    \"code\": \"UN_AUTHOR\",\n" +
                        "    \"success\": false\n" +
                        "}");
                response.setContentType("text/plain");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
