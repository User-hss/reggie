package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.reggie.common.R;
import com.hss.reggie.pojo.Category;
import com.hss.reggie.pojo.Dish;
import com.hss.reggie.pojo.Setmeal;
import com.hss.reggie.service.CategoryService;
import com.hss.reggie.service.DishService;
import com.hss.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 添加菜品种类
     */
    @PostMapping
    public R<String> add(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }
    /**
     * 分页查询菜品种类
     */
    @GetMapping("/page")
    public R<Page> getPage(Integer page,Integer pageSize){
        Page<Category> pageInfo=new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /**
     * 动态查询种类
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
    /**
     * 删除菜品种类
     */
    @DeleteMapping
    public R<String> deleteById(Long id){
        //检测菜品种类id是否与菜品关联
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        //套餐
        LambdaQueryWrapper<Setmeal> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId,id);
        if (dishService.count(queryWrapper) > 0) {
            return R.error("存在关联菜品，删除失败！");
        }
        if(setmealService.count(queryWrapper1) > 0){
            return R.error("存在关联套餐，删除失败！");
        }
        categoryService.removeById(id);
        return R.success("删除成功");
    }
    /**
     * 修改分类
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
}
