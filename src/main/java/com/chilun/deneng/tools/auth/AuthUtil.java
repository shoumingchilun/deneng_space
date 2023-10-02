package com.chilun.deneng.tools.auth;

import com.chilun.deneng.pojo.User;
import com.chilun.deneng.tools.constant.UserConstant;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @auther 齿轮
 * @create 2023-10-02-17:30
 */
@Component
public class AuthUtil {
    @Autowired
    JwtUtil jwtUtil;

    public boolean isManager(User user, String JWT) {
        if (user != null && user.getType() == UserConstant.MANAGER_USER_TYPE) return true;
        if (JWT != null && !JWT.equals("")) {
            Claims claims = jwtUtil.parseJWT(JWT);
            LinkedHashMap user1 = claims.get("user", LinkedHashMap.class);
            return ((Integer) user1.get("type")) == UserConstant.MANAGER_USER_TYPE;
        }
        return false;
    }

    public boolean isWantedUser(User user, String JWT, int id) {
        if (user != null && user.getId() == id) return true;
        if (JWT != null && !JWT.equals("")) {
            Claims claims = jwtUtil.parseJWT(JWT);
            LinkedHashMap user1 = claims.get("user", LinkedHashMap.class);
            return ((Integer) user1.get("id")) == id;
        }
        return false;
    }
}
