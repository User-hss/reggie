package com.hss.reggie.service;

import com.hss.reggie.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author master
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-01-10 19:57:43
*/
@Transactional
public interface OrderService extends IService<Order> {
    /**
     * 创建订单
     * @param order
     */
    void saveWithDetail(Order order);
}
