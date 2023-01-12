package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.reggie.common.BaseContext;
import com.hss.reggie.common.R;
import com.hss.reggie.dto.OrderDto;
import com.hss.reggie.pojo.Order;
import com.hss.reggie.pojo.OrderDetail;
import com.hss.reggie.service.OrderDetailService;
import com.hss.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    /**
     * 创建订单
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Order order){
        orderService.saveWithDetail(order);
        return R.success("保存成功");
    }
    /**
     * 分页查询
     */
    @GetMapping("/userPage")
    public R<Page<OrderDto>> userPage(Integer page, Integer pageSize){
        Page<OrderDto> pageInfo=new Page<>();
        Page<Order> orderPage=new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, BaseContext.getId());
        queryWrapper.orderByDesc(Order::getOrderTime);
        orderService.page(orderPage,queryWrapper);
        //拷贝
        BeanUtils.copyProperties(orderPage,pageInfo,"records");
        List<OrderDto> list = new ArrayList<>();
        List<Order> records = orderPage.getRecords();
        for (Order record : records) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(record,orderDto);
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId,record.getId());
            List<OrderDetail> detailList = orderDetailService.list(wrapper);
            orderDto.setOrderDetails(detailList);
            orderDto.setSumNum(detailList.size());
            list.add(orderDto);
        }
        pageInfo.setRecords(list);
        return R.success(pageInfo);
    }
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize){
        Page<Order> orderPage=new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, BaseContext.getId());
        queryWrapper.orderByDesc(Order::getOrderTime);
        orderService.page(orderPage,queryWrapper);
        return R.success(orderPage);
    }
}
