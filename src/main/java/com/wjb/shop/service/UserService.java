package com.wjb.shop.service;

import com.wjb.shop.entity.User;

public interface UserService {

    public User checkLogin(String username, String password);
}
