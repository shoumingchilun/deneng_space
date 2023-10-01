package com.chilun.deneng.filter;

import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.tools.auth.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * @auther 齿轮
 * @create 2023-10-01-16:43
 */
public class LoginFilter implements Filter {

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
            // 判断是否存在名为 "JWT" 的 Cookie
            boolean isValued = false;
            JwtUtil jwtUtil = JwtUtil.createJWTUtil();
            for (Cookie cookie : ((HttpServletRequest) request).getCookies()) {
                if (cookie.getName().equals("JWT")) {
                    try {
                        System.out.println(cookie.getValue());
                        if (!jwtUtil.isExpiredJWT(jwtUtil.parseJWT(cookie.getValue()))) {//未过期，说明jwt有效，直接放行
                            isValued = true;
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (isValued) {//通过则放行
                chain.doFilter(request, response);
            } else {//否则报错
                response.getWriter().write("{\n" +
                        "    \"message\": \"Not Logged in!\",\n" +
                        "    \"code\": \"UN_AUTHOR\",\n" +
                        "    \"success\": true\n" +
                        "}");
                response.setContentType("text/plain");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
