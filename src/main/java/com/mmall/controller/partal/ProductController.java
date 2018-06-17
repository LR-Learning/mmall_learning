package com.mmall.controller.partal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductionService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 18:48 2018/6/16
 * @Modified By:
 **/
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductionService iProductionService;

    // 获取商品详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId){
        return iProductionService.getProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    // 分页查询
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageNum", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "")String orderBy) {
        return iProductionService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
