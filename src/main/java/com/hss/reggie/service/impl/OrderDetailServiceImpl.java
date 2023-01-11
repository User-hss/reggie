package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.pojo.OrderDetail;
import com.hss.reggie.service.OrderDetailService;
import com.hss.reggie.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author master
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-01-10 19:57:47
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




