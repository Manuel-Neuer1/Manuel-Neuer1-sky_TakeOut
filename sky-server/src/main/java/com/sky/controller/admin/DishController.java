package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping()
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        // 清理缓存数据
        String key = "category_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品得批量删除
     * @param ids
     * @return
     */
    @DeleteMapping()
    @ApiOperation("菜品批量删除")
    public Result delete(String ids)/*这里可以使用MVC框架(@RequestParam List<Long> ids)*/ {
        log.info("菜品批量删除: {}", ids);
        //将字符串转换为字符串数组
        List<String> list = Arrays.asList(ids.split(","));
        //将字符串数组转换为Long类型的数组
        List<Long> longList = new ArrayList<>();
        for (String id : list) {
            longList.add(Long.parseLong(id));
        }
        dishService.deleteBatch(longList);

        // 将所有的菜品缓存数据全部清理掉，所有以category_开头的key,因为可能涉及多个分类菜品的修改
        Set keys = redisTemplate.keys("category_*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id:[0-9]+}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getDishById(@PathVariable("id") Long id) {
        log.info("根据id ：{} 查询菜品",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        // 将所有的菜品缓存数据全部清理掉，所有以category_开头的key，因为可能涉及多个分类菜品的修改
        Set keys = redisTemplate.keys("category_*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("菜品起售停售：{}，{}",status,id);
        dishService.startOrStop(status, id);

        // 将所有的菜品缓存数据全部清理掉，所有以category_开头的key,因为可能涉及多个分类菜品的修改
        Set keys = redisTemplate.keys("category_*");
        redisTemplate.delete(keys);

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(@RequestParam Long categoryId) {
        log.info("根据分类id查询菜品");
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }
}
