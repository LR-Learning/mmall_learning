package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 19:09 2018/6/21
 * @Modified By:
 **/
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
