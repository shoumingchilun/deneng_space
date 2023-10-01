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
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/register")//只需要password即可
    public JwtResponse register(@RequestBody User user, HttpServletResponse response, HttpSession session) {
        if (user.getPassword() == null || user.getPassword().equals("")) {//判断有无密码
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setCode(ResultCode.FAILURE);
            jwtResponse.setMessage("缺少密码");
            return jwtResponse;
        }
        //忽略其他值
        user.setType(null);
        user.setChecked(null);
        user.setInfo(null);
        user.setTaskForceId(null);
        //密码加密
        user.setPassword(String.valueOf(hashToPositiveInt(user.getPassword())));
        //存入数据库
        boolean save = false;
        try {
            save = service.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!save) {//保存失败则返回“创建用户失败”
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setCode(ResultCode.FAILURE);
            jwtResponse.setMessage("创建用户失败");
            return jwtResponse;
        }
        //保存成功，开始生成JWT
        String Data = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            Data = jwtUtil.createJWT(
                    UUID.randomUUID().toString().replace("-", ""),
                    "UserController.register",
                    259200000,//过期时间为3天
                    map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwtData(Data);
        jwtResponse.setCode(ResultCode.SUCCESS);
        jwtResponse.setMessage(String.valueOf(user.getId()));
        // 创建一个 cookie对象
        Cookie cookie = new Cookie("JWT", Data);
        //将cookie对象加入response响应
        response.addCookie(cookie);
        session.setAttribute("user", user);//存入session
        return jwtResponse;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody User user, HttpServletResponse response, HttpSession session) {
        if (user.getId() == null || user.getPassword() == null) {//判断id和密码是否为空
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setCode(ResultCode.FAILURE);
            jwtResponse.setMessage("请输入id或密码");
            return jwtResponse;
        }
        //获得数据库中对应user1
        User user1 = service.getById(user.getId());
        if (user1 == null) {//判断user1是否存在
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setCode(ResultCode.FAILURE);
            jwtResponse.setMessage("用户不存在");
            return jwtResponse;
        }
        //判断密码
        if (hashToPositiveInt(user.getPassword()) == Integer.parseInt(user1.getPassword())) {
            //密码正确
            String Data = null;//尝试生成JWT
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("user", user1);
                Data = jwtUtil.createJWT(
                        UUID.randomUUID().toString().replace("-", ""),
                        "UserController.login",
                        259200000,//过期时间为3天
                        map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setJwtData(Data);
            jwtResponse.setCode(ResultCode.SUCCESS);
            jwtResponse.setMessage(JSON.toJSONString(user1));
            // 创建一个 cookie对象
            Cookie cookie = new Cookie("JWT", Data);
            //将cookie对象加入response响应
            response.addCookie(cookie);
            session.setAttribute("user", user1);//存入session
            return jwtResponse;
        } else {//密码错误
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setCode(ResultCode.FAILURE);
            jwtResponse.setMessage("密码错误");
            return jwtResponse;
        }
    }

    @PutMapping("/changePassword")
    public BaseResponse changePassword(@RequestBody User user,
                                       @CookieValue(name = "JWT", required = false) String jwt,
                                       @SessionAttribute(name = "user", required = false) User currentUser) {
        if (jwt == null && currentUser == null) {
            return new BaseResponse("未登录", ResultCode.UN_AUTHOR);
        }
        //判断有无更改权限
        boolean hasRight = false;
        if (currentUser != null) {//session不为空，从session中获得信息
            if (currentUser.getType() == 4 || currentUser.getId() == user.getId()) hasRight = true;
        } else {//JWT不为空，从JWT中获得信息
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(jwt);
            } catch (Exception e) {
                return new BaseResponse("JWT解析失败", ResultCode.UN_AUTHOR);
            }
            LinkedHashMap currentUser1 = claims.get("user", LinkedHashMap.class);
            if (currentUser1.get("type").equals(4) || currentUser1.get("id").equals(user.getId())) hasRight = true;
        }
        if (hasRight) {//有权限则进行修改
            user.setPassword(String.valueOf(hashToPositiveInt(user.getPassword())));//密码加密
            //忽略其他属性
            User changedUser = new User();
            changedUser.setId(user.getId());
            changedUser.setPassword(user.getPassword());
            //开始修改
            boolean update = false;
            try {
                update = service.updateById(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (update) return new BaseResponse("更改成功", ResultCode.SUCCESS);
            return new BaseResponse("修改失败", ResultCode.FAILURE);
        } else {//无权限修改
            return new BaseResponse("非管理员或原用户", ResultCode.UN_AUTHOR);
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
