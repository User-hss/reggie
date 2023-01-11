package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.pojo.ShoppingCart;
import com.hss.reggie.service.ShoppingCartService;
import com.hss.reggie.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author master
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2023-01-10 15:01:46
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




