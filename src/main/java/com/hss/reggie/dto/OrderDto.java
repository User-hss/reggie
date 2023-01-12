package com.hss.reggie.dto;

import com.hss.reggie.pojo.OrderDetail;
import com.hss.reggie.pojo.Order;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Order {
    private List<OrderDetail> orderDetails;
    private Integer sumNum;//商品数量
    private String userName;//用户姓名
}
