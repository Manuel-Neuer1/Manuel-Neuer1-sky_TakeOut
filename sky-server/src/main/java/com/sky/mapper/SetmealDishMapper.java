package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 根据菜品得ids去查询菜品关联的套餐ids
     * @param dishIds
     * @return
     */
    //@Select("select setmeal_id from setmeal_dish where dish_id in (#{dishIds})")
    List<Long> getSetmealDishBySetmealId(List<Long> dishIds);

}
