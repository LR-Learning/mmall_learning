package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 19:17 2018/6/13
 * @Modified By:
 **/
public interface IProductionService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);
}
