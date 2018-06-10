package com.mmall.common;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 18:23 2018/6/9
 * @Modified By:
 **/
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    // 使用内部接口类，把常量进行分组, 没有枚举重
    public interface Role{
        int ROLE_CUSTOMER = 0; // 普通用户
        int ROLE_ADMIN = 1; // 管理员
    }
}
