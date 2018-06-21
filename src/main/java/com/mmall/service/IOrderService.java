package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 19:09 2018/6/21
 * @Modified By:
 **/
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);
}
