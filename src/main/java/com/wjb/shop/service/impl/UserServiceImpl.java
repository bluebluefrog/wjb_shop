package com.wjb.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wjb.shop.entity.User;
import com.wjb.shop.exception.BussinessException;
import com.wjb.shop.mapper.UserMapper;
import com.wjb.shop.service.UserService;
import com.wjb.shop.util.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User checkLogin(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BussinessException("U02", "用户不存在");
        }
        String npassword = MD5Utils.md5Digest(password, user.getSalt());
        if (!npassword.equals(user.getPassword())) {
            throw new BussinessException("U03", "密码错误");
        }
        return user;
    }
}
