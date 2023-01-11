package com.hss.reggie.service;

import com.hss.reggie.pojo.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author master
* @description 针对表【shopping_cart(购物车)】的数据库操作Service
* @createDate 2023-01-10 15:01:46
*/
@Transactional
public interface ShoppingCartService extends IService<ShoppingCart> {

}
