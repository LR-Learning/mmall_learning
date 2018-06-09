package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 16:09 2018/6/9
 * @Modified By:
 **/
public interface IUserService {
    ServerResponse<User> login(String username, String password);
}
