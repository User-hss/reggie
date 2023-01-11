package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.pojo.DishFlavor;
import com.hss.reggie.service.DishFlavorService;
import com.hss.reggie.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author master
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-01-04 11:35:57
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




