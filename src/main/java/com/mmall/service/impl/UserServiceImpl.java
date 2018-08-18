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
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    // 重置密码
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken){

        // 判断Token是否为空
        if (StringUtils.isNotBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token值需要传递");
        }
        // 判断用户名是否存在
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()){
            //为true的时候用户名为空
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // 获取Token
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX  + username);
        // 判断Token
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("Token 无效或过期");
        }
        if (StringUtils.equals(forgetToken, token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            // 更新完之后要用MD5的密码
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("Token错误，请重新获取重置密码的Token");
        }
    return ServerResponse.createByErrorMessage("修改密码失败");
    }

    // 登录状态下重置密码
    public ServerResponse<String> restPassword(String passwordOld, String passwordNew, User user){
        // 防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户
        // 我们会查询一盒count(1),若果不指定id，则结果为true，count(1)>0
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        // 选择性的更新
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    //  更新个人用户信息
    public ServerResponse<User> updateInformation(User user){
        // username不能被更新
        // email也要进行校验，校验新的email是否已经存在，并且存在新的email如果相同的话，不能是我们当前这个用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0){
            return ServerResponse.createByErrorMessage("email以存在， 请更换email在重新尝试");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人用户信息失败");
    }

    // 获取用户个人用户信息
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("当前用户为空");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    // 校验是否为管理员
    public ServerResponse checkAdmin(User user){
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }



}
