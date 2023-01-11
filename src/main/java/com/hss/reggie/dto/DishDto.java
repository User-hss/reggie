package com.hss.reggie.dto;

import com.hss.reggie.pojo.Dish;
import com.hss.reggie.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于接收dish和dishFlavor两表联合数据
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors=new ArrayList<>();//接收口味数据
    private String categoryName;
    private Integer copies;
}
