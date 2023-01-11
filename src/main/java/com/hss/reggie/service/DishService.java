package com.hss.reggie.service;

import com.hss.reggie.dto.DishDto;
import com.hss.reggie.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author master
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2023-01-03 21:53:09
*/
@Transactional
public interface DishService extends IService<Dish> {
    /**
     * 新增菜品
     */
    void saveWithFlavor(DishDto dishDto);
    /**
     * 根据id查询菜品和口味信息
     */
    DishDto getByIdWithFlavor(Long id);
    /**
     * 修改菜品和口味
     */
    void updateWithFlavor(DishDto dishDto);
    /**
     * 删除菜品
     */
    void deleteWithFlavor(List<Long> ids);

    /**
     * 添加查询
     * @param dish
     * @return
     */
    List<DishDto> getListWithFlavor(Dish dish);
}
