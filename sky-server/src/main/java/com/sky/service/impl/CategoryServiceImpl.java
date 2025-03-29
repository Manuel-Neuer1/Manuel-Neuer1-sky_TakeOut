package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category); //把categoryDTO中的属性拷贝到category中
        //category.setCreateTime(LocalDateTime.now());
        //category.setUpdateTime(LocalDateTime.now());
        //category.setCreateUser(BaseContext.getCurrentId());
        //category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insert(category);
    }


    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        //开始分页查询（用pagehelper）
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page =  categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void startOrStop(Integer status, long id) {
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);
        //category.setUpdateTime(LocalDateTime.now());
        //category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        Integer dishCount = dishMapper.countByCategoryId(id);
        if (dishCount > 0) {
            //当前分类下关联了菜品，不能删除
            throw new DeletionNotAllowedException("当前分类下关联了菜品，不能删除");
        }
        Integer setmealCount = setmealMapper.countByCategoryId(id);
        if (setmealCount > 0) {
            //当前分类下关联了套餐，不能删除
            throw new DeletionNotAllowedException("当前分类下关联了套餐，不能删除");
        }
        categoryMapper.deleteById(id);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        // 将categoryDTO中的属性拷贝到category中
        BeanUtils.copyProperties(categoryDTO, category);
        // 设置修改时间和修改人
        //category.setUpdateTime(LocalDateTime.now());
        //category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    @Override
    public List<Category> list(Integer type) {
        List<Category> categoryList = categoryMapper.list(type);
//        List<CategoryDTO> categoryDTOS = new ArrayList<>();
//        for (Category category : categoryList) {
//            CategoryDTO categoryDTO = new CategoryDTO();
//            BeanUtils.copyProperties(category, categoryDTO);
//            categoryDTOS.add(categoryDTO);
//        }
        return categoryList;
    }
}
