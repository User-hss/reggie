package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.dto.SetmealDishDto;
import com.hss.reggie.dto.SetmealDto;
import com.hss.reggie.exception.CustomException;
import com.hss.reggie.pojo.Dish;
import com.hss.reggie.pojo.Setmeal;
import com.hss.reggie.pojo.SetmealDish;
import com.hss.reggie.service.DishService;
import com.hss.reggie.service.SetmealDishService;
import com.hss.reggie.service.SetmealService;
import com.hss.reggie.mapper.SetmealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author master
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2023-01-03 22:13:41
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private DishService dishService;
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);//保存套餐基本信息
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : list) {
            setmealDish.setSetmealId(setmealDto.getId());//将套餐id赋值给setmeal_dish表
        }
        setmealDishService.saveBatch(list);
    }

    @Override
    public void deleteWithFlavor(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        if(this.count(queryWrapper)>0)throw new CustomException("存在在售状态的套餐，删除失败！");
        this.removeByIds(ids);
        //删除关联菜品关系表
        LambdaQueryWrapper<SetmealDish> queryWrap=new LambdaQueryWrapper<>();
        queryWrap.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrap);
    }

    @Override
    public SetmealDto getByIdWithFlavor(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);//拷贝
        //菜品关系表查询
        LambdaQueryWrapper<SetmealDish> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrap);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateWithFlavor(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        //清理关联表旧数据
        LambdaQueryWrapper<SetmealDish> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrap);
        //存入新数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public List<SetmealDishDto> getInfoByIdWithFlavor(Long id) {
        LambdaQueryWrapper<SetmealDish> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrap);
        List<SetmealDishDto> listDto=new ArrayList<>();
        for (SetmealDish item : list) {
            //查询菜品图片
            Dish dish = dishService.getById(item.getDishId());
            SetmealDishDto dto = new SetmealDishDto();
            BeanUtils.copyProperties(item, dto);
            dto.setImage(dish.getImage());
            listDto.add(dto);
        }
        return listDto;
    }
}




