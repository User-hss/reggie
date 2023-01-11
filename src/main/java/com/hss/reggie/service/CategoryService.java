package com.hss.reggie.service;

import com.hss.reggie.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author master
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2023-01-03 20:38:42
*/
@Transactional
public interface CategoryService extends IService<Category> {

}
