package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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


    void insertBatch(List<SetmealDish> setmealDishes);

    void deleteByDishIds(List<Long> ids);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
