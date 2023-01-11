package com.hss.reggie.dto;

import com.hss.reggie.pojo.Setmeal;
import com.hss.reggie.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
