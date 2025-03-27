package com.sky.service;

import com.sky.entity.Category;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    void save(CategoryDTO categoryDTO);

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void startOrStop(Integer status, long id);

    void deleteById(Long id);

    void update(CategoryDTO categoryDTO);

    List<Category> list(Integer type);
}
