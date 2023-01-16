package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.reggie.common.R;
import com.hss.reggie.dto.DishDto;
import com.hss.reggie.pojo.Dish;
import com.hss.reggie.pojo.DishFlavor;
import com.hss.reggie.service.CategoryService;
import com.hss.reggie.service.DishFlavorService;
import com.hss.reggie.service.DishService;
import com.hss.reggie.service.impl.DishFlavorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page> getPage(Integer page, Integer pageSize, String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage=new Page<>();//最终返回的页面
        LambdaQueryWrapper<Dish> wrapper=new LambdaQueryWrapper<>();
        //若name不为空则添加模糊查询条件
        wrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        //排序条件
        wrapper.orderByDesc(Dish::getUpdateTime);
        //查询
        dishService.page(pageInfo,wrapper);
        //拷贝(忽略records,只拷贝页面基本信息)
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        ArrayList<DishDto> list = new ArrayList<>();//用于接收records和分类名称信息
        for (Dish dish : records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);//菜品信息拷贝
            Long categoryId = dish.getCategoryId();
            if(categoryId != null)dishDto.setCategoryName(categoryService.getById(categoryId).getName());//分类名称
            list.add(dishDto);
        }
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable("id") Long id){
        DishDto dishDto=dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    /**
     * 动态条件查询
     */
    @Cacheable(value = "dish",key = "#dish.categoryId",condition = "#dish.name==null")
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> list = dishService.getListWithFlavor(dish);
        return R.success(list);
    }
    /**
     * 添加菜品
     */
    @CacheEvict(value = "dish",key = "#dishDto.categoryId")
    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        //dishService.clear();
        return R.success("添加成功");
    }
    /**
     * 修改菜品
     */
    @CacheEvict(value = "dish",allEntries = true)
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        //dishService.clear();
        return R.success("修改成功");
    }
    /**
     * 修改菜品销售状态
     */
    @CacheEvict(value = "dish",allEntries = true)
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status){
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Dish::getId,ids);//使用in条件
        updateWrapper.set(Dish::getStatus,status);//修改status字段
        dishService.update(updateWrapper);
        //dishService.clear();
        return R.success("修改销售状态成功");
    }
    /**
     * 删除菜品
     */
    @CacheEvict(value = "dish",allEntries = true)
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        dishService.deleteWithFlavor(ids);
        //dishService.clear();
        return R.success("删除成功");
    }
}
