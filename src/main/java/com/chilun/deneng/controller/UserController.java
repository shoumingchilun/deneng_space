package com.chilun.deneng.controller;

import com.alibaba.fastjson.JSON;
import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.JwtResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.pojo.User;
import com.chilun.deneng.service.IUserService;
import com.chilun.deneng.tools.auth.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService service;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public JwtResponse register(@RequestBody User user, HttpServletResponse response) {
        try {
            user.setPassword(String.valueOf(hashToPositiveInt(user.getPassword())));
            boolean save = service.save(user);
            if (save == true) {
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                String Data = jwtUtil.createJWT(
                        UUID.randomUUID().toString().replace("-", ""),
                        "UserController.register",
                        259200000,
                        map);//过期时间为3天
                JwtResponse jwtResponse = new JwtResponse();
                jwtResponse.setJwtData(Data);
                jwtResponse.setCode(ResultCode.SUCCESS);
                jwtResponse.setMessage(String.valueOf(user.getId()));
                // 创建一个 cookie对象
                Cookie cookie = new Cookie("JWT", Data);
                //将cookie对象加入response响应
                response.addCookie(cookie);
                return jwtResponse;
            }
        } catch (Exception ignored) {
        }
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setCode(ResultCode.FAILURE);
        jwtResponse.setMessage("创建用户失败");
        return jwtResponse;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody User user, HttpServletResponse response) throws Exception {
        User user1 = service.getById(user.getId());
        if (user1 == null) {
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setCode(ResultCode.FAILURE);
            jwtResponse.setMessage("用户不存在");
            return jwtResponse;
        }
        if (hashToPositiveInt(user.getPassword()) == Integer.parseInt(user1.getPassword())) {
            //密码正确
            Map<String, Object> map = new HashMap<>();
            map.put("user", user1);
            String Data = jwtUtil.createJWT(
                    UUID.randomUUID().toString().replace("-", ""),
                    "UserController.login",
                    259200000,
                    map);//过期时间为3天
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setJwtData(Data);
            jwtResponse.setCode(ResultCode.SUCCESS);
            jwtResponse.setMessage("JWT");
            // 创建一个 cookie对象
            Cookie cookie = new Cookie("JWT", Data);
            //将cookie对象加入response响应
            response.addCookie(cookie);
            return jwtResponse;
        } else {
            System.out.println(hashToPositiveInt(user.getPassword()));
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setCode(ResultCode.FAILURE);
            jwtResponse.setMessage("密码错误");
            return jwtResponse;
        }
    }

    @PutMapping("/changePassword")
    public BaseResponse changePassword(@RequestBody User user, @CookieValue("JWT") String jwt) {
        try {
            Claims claims = jwtUtil.parseJWT(jwt);
            LinkedHashMap manager = claims.get("user", LinkedHashMap.class);
            if (manager.get("type").equals(4) || manager.get("id").equals(user.getId())) {
                user.setPassword(String.valueOf(hashToPositiveInt(user.getPassword())));
                boolean update = service.updateById(user);
                if (update) return new BaseResponse("更改成功", ResultCode.SUCCESS);
                return new BaseResponse(null, ResultCode.FAILURE);
            }
            return new BaseResponse("非管理员/原用户，ID：" + manager.get("id"), ResultCode.UN_AUTHOR);

        } catch (Exception ignored) {
            return new BaseResponse(ignored.getMessage(), ResultCode.FAILURE);
        }
    }

    @GetMapping
    public BaseResponse queryUserById(@RequestParam int id) {
        User user = service.getById(id);
        if (user == null) {
            return new BaseResponse("无该用户", ResultCode.FAILURE);
        }
        user.setPassword(null);
        return new BaseResponse(JSON.toJSONString(user), ResultCode.SUCCESS);

    }

    @PutMapping
    public BaseResponse updateUserById(@RequestBody User user) {
        user.setPassword(null);
        boolean update = service.updateById(user);
        if (update) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    private static int hashToPositiveInt(String input) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // 使用BigInteger将字节数组表示的哈希值转换为正整数
        BigInteger bigIntegerHash = new BigInteger(1, hashBytes);

        // 获取正整数表示的哈希值
        return bigIntegerHash.intValue();

    }
}
