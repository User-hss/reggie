package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.reggie.common.R;
import com.hss.reggie.dto.DishDto;
import com.hss.reggie.dto.SetmealDishDto;
import com.hss.reggie.dto.SetmealDto;
import com.hss.reggie.pojo.Dish;
import com.hss.reggie.pojo.DishFlavor;
import com.hss.reggie.pojo.Setmeal;
import com.hss.reggie.pojo.SetmealDish;
import com.hss.reggie.service.CategoryService;
import com.hss.reggie.service.DishService;
import com.hss.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page> getPage(Integer page, Integer pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage=new Page<>();//最终返回的页面
        LambdaQueryWrapper<Setmeal> wrapper=new LambdaQueryWrapper<>();
        //若name不为空则添加模糊查询条件
        wrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        //排序条件
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        //查询
        setmealService.page(pageInfo,wrapper);
        //拷贝(忽略records,只拷贝页面基本信息)
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        ArrayList<SetmealDto> list = new ArrayList<>();//用于接收records和分类名称信息
        for (Setmeal setmeal : records) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);//套餐信息拷贝
            Long categoryId = setmeal.getCategoryId();
            if(categoryId != null)setmealDto.setCategoryName(categoryService.getById(categoryId).getName());//分类名称
            list.add(setmealDto);
        }
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }
    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable("id") Long id){
        SetmealDto setmealDto=setmealService.getByIdWithFlavor(id);
        return R.success(setmealDto);
    }
    /**
     * 条件查询
     */
    @Cacheable(value = "setmeal",key = "#setmeal.categoryId")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
    /**
     * 根据套餐id查询菜品口味等信息
     */
    @GetMapping("/dish/{id}")
    public R<List<SetmealDishDto>> getDishById(@PathVariable("id") Long id){
        List<SetmealDishDto> dishDtoList = setmealService.getInfoByIdWithFlavor(id);
        return R.success(dishDtoList);
    }
    /**
     * 保存套餐
     */
    @CacheEvict(value = "setmeal",key = "#setmealDto.categoryId")
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("保存成功");
    }
    /**
     * 删除套餐
     */
    @CacheEvict(value = "setmeal",allEntries = true)//清理所有套餐缓存
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithFlavor(ids);
        return R.success("删除成功");
    }
    /**
     * 修改套餐销售状态
     */
    @CacheEvict(value = "setmeal",allEntries = true)//清理所有套餐缓存
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status){
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId,ids);//使用in条件
        updateWrapper.set(Setmeal::getStatus,status);//修改status字段
        setmealService.update(updateWrapper);
        return R.success("修改销售状态成功");
    }
    /**
     * 修改套餐
     */
    @CacheEvict(value = "setmeal",allEntries = true)//清理所有套餐缓存
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithFlavor(setmealDto);
        return R.success("修改成功");
    }
}
