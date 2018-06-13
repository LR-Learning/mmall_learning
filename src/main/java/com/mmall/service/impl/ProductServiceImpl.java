package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 19:17 2018/6/13
 * @Modified By:
 **/
@Service("iProductionService")
public class ProductServiceImpl implements IProductionService {
    @Autowired
    private ProductMapper productMapper;

    // 保存或更新商品
    public ServerResponse saveOrUpdateProduct(Product product){
        if (product != null) {
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getMainImage().split(",");
                if (subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }
        if(product.getId() != null){
            int rowCount = productMapper.updateByPrimaryKey(product);
            if (rowCount > 0){
                return ServerResponse.createBySuccessMessage("更新产品成功");
            }
            return ServerResponse.createByErrorMessage("更新产品失败");
        }else{
            int rowCount = productMapper.insert(product);
            if (rowCount > 0){
                return ServerResponse.createBySuccessMessage("新增产品成功");
            }
            return ServerResponse.createByErrorMessage("新增产品失败");
        }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    // 更新销售状态
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status){
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改销售状态失败");
    }
}
