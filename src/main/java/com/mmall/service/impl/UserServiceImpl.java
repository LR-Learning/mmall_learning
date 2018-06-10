package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 16:15 2018/6/9
 * @Modified By:
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // todo 密码登录MD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        // 将密码设为空，防止别人抓包抓到密码
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    // 注册功能
    public ServerResponse<String> register(User user){
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if(resultCount > 0){
//            return ServerResponse.createByErrorMessage("用户名已存在");
//        }
//
//        resultCount = userMapper.checkEmail(user.getEmail());
//        if(resultCount > 0){
//            return ServerResponse.createByErrorMessage("email已存在");
//        }

        //复用
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);

        // MD 加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     *校验功能
     * @param str   value值
     * @param type  根据type传email还是password来判断str调用那个接口
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type){
        if(StringUtils.isNotBlank(type)){  // isNotBlank 认为" " 是false
            // 开始校验

            //校验用户名
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            // 校验邮箱
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    // 找回密码
    public ServerResponse<String> selectQuestion(String username){
        // 判断用户名是否存在
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()){
            //为true的时候用户名为空
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // 获取问题
        String question = userMapper.selectQuestionByUsername(username);
        if ( StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    // 校验问题密码是否正确
    public ServerResponse<String> checkAnswer(String username, String question, String answer){
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            // 说明问题和答案是这个用户的，并且答案正确
            // 生成一个Token
            String forgetToken = UUID.randomUUID().toString(); // 37b135e6-f3cc-40a1-8327-deceed12c8c7
            TokenCache.setKey("Token_" + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

}
