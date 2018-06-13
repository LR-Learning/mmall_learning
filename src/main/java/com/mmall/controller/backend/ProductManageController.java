package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductionService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 11:58 2018/6/13
 * @Modified By:
 **/

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductionService iProductionService;

    @RequestMapping("save.do")
    @ResponseBody
    // 保存商品的接口
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆，请登录管理员身份");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            // 填充增加产品的业务逻辑
            return iProductionService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作， 请登录");
        }
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    // 保存商品的接口
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆，请登录管理员身份");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            // 更新产品销售状态
            return iProductionService.setSaleStatus(productId, status);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作， 请登录");
        }
    }
}
