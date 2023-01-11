package com.hss.reggie.service;

import com.hss.reggie.dto.SetmealDishDto;
import com.hss.reggie.dto.SetmealDto;
import com.hss.reggie.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author master
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-01-03 22:13:41
*/
@Transactional
public interface SetmealService extends IService<Setmeal> {
    /**
     * 保存套餐
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐
     * @param ids
     */
    void deleteWithFlavor(List<Long> ids);

    /**
     * id查询
     * @param id
     */
    SetmealDto getByIdWithFlavor(Long id);

    /**
     * 修改套餐
     * @param setmealDto
     */
    void updateWithFlavor(SetmealDto setmealDto);

    /**
     * 查询套餐信息
     *
     * @param id
     * @return
     */
    List<SetmealDishDto> getInfoByIdWithFlavor(Long id);
}
