package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.pojo.Category;
import com.hss.reggie.service.CategoryService;
import com.hss.reggie.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author master
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2023-01-03 20:38:42
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




