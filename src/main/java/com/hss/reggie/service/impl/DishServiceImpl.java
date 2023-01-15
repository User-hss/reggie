package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.dto.DishDto;
import com.hss.reggie.exception.CustomException;
import com.hss.reggie.pojo.Dish;
import com.hss.reggie.pojo.DishFlavor;
import com.hss.reggie.service.DishFlavorService;
import com.hss.reggie.service.DishService;
import com.hss.reggie.mapper.DishMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
* @author master
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2023-01-03 21:53:09
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //将菜品信息保存
        this.save(dishDto);
        //保存菜品id到口味表
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        //保存口味信息
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        if (dish == null) {
            return null;
        }
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);//根据菜品id查询口味
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        //拷贝
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //修改菜品信息
        this.updateById(dishDto);
        //清理菜品关联的口味表
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //保存新的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void deleteWithFlavor(List<Long> ids) {
        //查询确认套餐出售状态
        List<Dish> dishes = this.listByIds(ids);
        for (Dish dish : dishes) {
            if (dish.getStatus()==1)throw new CustomException("有菜品处于出售状态，不可删除！");
        }
        this.removeByIds(ids);
        //清理关联的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper);
        //redisTemplate.delete("dish_"+dishes.get(0).getCategoryId());
    }

    @Override
    public List<DishDto> getListWithFlavor(Dish dish) {
        String key="dish_"+dish.getCategoryId();
        List<DishDto> dishDtoList=(List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList != null) {
            return dishDtoList;
        }
        dishDtoList=new ArrayList<>();//防止空指针异常
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.like(StringUtils.isNotEmpty(dish.getName()),Dish::getName,dish.getName());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = this.list(queryWrapper);
        for (Dish item : list) {
            DishDto dishDto = this.getByIdWithFlavor(item.getId());//根据菜品id查询DishDto
            dishDtoList.add(dishDto);
        }
        redisTemplate.opsForValue().set(key,dishDtoList);
        return dishDtoList;
    }
}




