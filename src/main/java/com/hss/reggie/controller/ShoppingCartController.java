package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.reggie.common.BaseContext;
import com.hss.reggie.common.R;
import com.hss.reggie.pojo.ShoppingCart;
import com.hss.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    /**
     * 查询当前用户购物车
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    /**
     * 添加购物车
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getId());
        shoppingCart.setCreateTime(LocalDateTime.now());
        //查询是否有重复
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getId());
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        if(cart != null){
            cart.setNumber(cart.getNumber()+1);//数量+1
            shoppingCartService.updateById(cart);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cart=shoppingCart;
        }
        return R.success(cart);
    }
    /**
     * 减少购物车
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getId());
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        if(cart.getNumber()>1){
            cart.setNumber(cart.getNumber()-1);//数量-1
            shoppingCartService.updateById(cart);
        }else {
            shoppingCartService.remove(queryWrapper);
            cart=new ShoppingCart();
        }
        return R.success(cart);
    }
    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清理成功");
    }
}
