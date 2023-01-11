package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.common.BaseContext;
import com.hss.reggie.exception.CustomException;
import com.hss.reggie.pojo.*;
import com.hss.reggie.service.*;
import com.hss.reggie.mapper.OrdersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @author master
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2023-01-10 19:57:43
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Order>
    implements OrderService {
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Override
    public void saveWithDetail(Order order) {
        order.setUserId(BaseContext.getId());
        //查询购物车
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getId());
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        if (list.size() == 0) {
            throw new CustomException("购物车为空");
        }
        //查询用户
        User user = userService.getById(BaseContext.getId());
        //查询地址
        AddressBook addressBook = addressBookService.getById(order.getAddressBookId());
        if (addressBook == null) {
            throw new CustomException("地址信息异常");
        }
        //使用IdWorker工具类生成订单号
        Long number = IdWorker.getId();
        //补充订单明细信息
        List<OrderDetail> orderDetailList = new ArrayList<>();
        AtomicInteger amount = new AtomicInteger(0);//原子整型（线程安全） -金额
        for (ShoppingCart cart : list) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(number);
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetailList.add(orderDetail);
            amount.addAndGet(cart.getAmount().multiply(BigDecimal.valueOf(cart.getNumber())).intValue());
        }

        //补充订单信息
        order.setId(number);
        order.setNumber(String.valueOf(number));
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setStatus(2);
        order.setAmount(new BigDecimal(amount.get()));//总金额
        order.setConsignee(addressBook.getConsignee());
        order.setPhone(addressBook.getPhone());
        order.setUserName(user.getName());
        order.setAddress((addressBook.getProvinceName()==null ? "":addressBook.getProvinceName())
                +(addressBook.getCityName()==null ? "":addressBook.getCityName())
                +(addressBook.getDistrictName()==null ? "":addressBook.getDistrictName())
                +(addressBook.getDetail()==null ? "":addressBook.getDetail())
        );
        //添加订单
        this.save(order);
        //添加订单明细
        orderDetailService.saveBatch(orderDetailList);
        //清空购物车
        shoppingCartService.remove(queryWrapper);
    }
}




