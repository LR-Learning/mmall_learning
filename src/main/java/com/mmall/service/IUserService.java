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
    ServerResponse login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);
}
