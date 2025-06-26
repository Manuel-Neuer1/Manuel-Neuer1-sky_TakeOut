package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal); //将setmealDTO中的属性复制到setmeal中

        //向套餐插入数据
        setmealMapper.insert(setmeal); //插入套餐，并且会将自动生成的id赋值给setmeal

        Long setmealId = setmeal.getId();

        //向菜品插入n条数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        // 套餐里每个 d菜品 的 setmealId 都要设置为当前插入的套餐的 id
        if (setmealDishes != null && setmealDishes.size() > 0 ){
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealId);
            }
            setmealDishMapper.insertBatch(setmealDishes);

        }
    }
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 调用了 PageHelper 的静态方法，告诉 MyBatis：
        //“我接下来这条 SQL 查询结果需要进行分页处理，页码是多少，每页多少条。”
        //这会影响紧接着执行的 第一次查询语句，PageHelper 会自动生成 LIMIT、OFFSET 子句。
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }

    @Override
    @Transactional
    public void deleteBtach(List<Long> ids) {
        //判断当前套餐是否在启售中，启售中不能删除
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //删除套餐表里的套餐
        setmealMapper.deleteByIds(ids);

        //删除套餐菜品表里的菜品
        setmealDishMapper.deleteByDishIds(ids);

    }

    @Override
    public SetmealVO getById(Long id) {
        //根据id查询套餐
        Setmeal setmeal = setmealMapper.getById(id);

        //根据id查询对应菜品

        List<SetmealDish> setmealDishs = setmealDishMapper.getBySetmealId(id);

        //将结果封装到VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishs);

        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //修改套餐
        setmealMapper.update(setmeal);

        List<Long> ids = new ArrayList<>(); //存放套餐id的集合，这样可以不用新定义一个根据单个id查询的方法
        ids.add(setmealDTO.getId());

        //删除原有的菜品
        setmealDishMapper.deleteByDishIds(ids);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        //插入套餐对应的菜品

        if (setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDTO.getId());
            });
        }

        //批量插入菜品
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
