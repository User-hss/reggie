package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.reggie.common.BaseContext;
import com.hss.reggie.common.R;
import com.hss.reggie.dto.OrderDto;
import com.hss.reggie.pojo.Order;
import com.hss.reggie.pojo.OrderDetail;
import com.hss.reggie.pojo.User;
import com.hss.reggie.service.OrderDetailService;
import com.hss.reggie.service.OrderService;
import com.hss.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @Autowired
    private UserService userService;
    /**
     * 创建订单
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Order order){
        orderService.saveWithDetail(order);
        return R.success("保存成功");
    }
    /**
     * 客户端分页查询
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

    /**
     * 后台分页查询
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String number, String beginTime,String endTime){
        Page<OrderDto> pageInfo=new Page<>();
        Page<Order> orderPage=new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number),Order::getNumber,number);
        queryWrapper.between(beginTime!=null&&endTime != null,Order::getOrderTime,beginTime,endTime);
        queryWrapper.orderByDesc(Order::getOrderTime);
        orderService.page(orderPage,queryWrapper);
        //拷贝
        BeanUtils.copyProperties(orderPage,pageInfo,"records");
        List<OrderDto> list = new ArrayList<>();
        List<Order> records = orderPage.getRecords();
        for (Order record : records) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(record,orderDto);
            //查询用户姓名
            User user = userService.getById(record.getUserId());
            orderDto.setUserName(user.getName());
            list.add(orderDto);
        }
        pageInfo.setRecords(list);
        return R.success(pageInfo);
    }
    /**
     * 订单状态
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Order order) {
        orderService.updateById(order);
        return R.success("订单状态更改成功");
    }
    /**
     * 再来一单
     */
    @PostMapping("again")
    public R<String> again(@RequestBody Order order){
        return R.success("功能未开发");
    }
}
