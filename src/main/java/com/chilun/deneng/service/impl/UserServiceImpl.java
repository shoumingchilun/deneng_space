package com.chilun.deneng.service.impl;

import com.chilun.deneng.pojo.User;
import com.chilun.deneng.dao.UserMapper;
import com.chilun.deneng.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
