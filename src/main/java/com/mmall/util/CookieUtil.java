package com.mmall.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 16:34 2018/8/6
 * @Modified By:
 **/

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMIAN = "www.happymmall.com";
    private final static String COOKIE_NAME = "mall_login_token";

    //读取Cookie
    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                log.info("read cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
                if(StringUtils.equals(ck.getName(), COOKIE_NAME)){
                    log.info("return cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    // 写入Cookie
    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMIAN);
        ck.setPath("/"); // 代表设置在根目录
        ck.setMaxAge(60*60*24*365); // 设置有效期 单位为秒， 如果不设置的话， cookie只会写会内存只对当前页面有效， 不会写入硬盘
        ck.setHttpOnly(true);

        log.info("write cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
    }

    // 删除Cookie
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                if(StringUtils.equals(ck.getName(), COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMIAN);
                    ck.setPath("/");
                    ck.setMaxAge(0); // 设置为0， 代表删除此cookie
                    log.info("del cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
                    return;
                }
            }
        }
    }
}
