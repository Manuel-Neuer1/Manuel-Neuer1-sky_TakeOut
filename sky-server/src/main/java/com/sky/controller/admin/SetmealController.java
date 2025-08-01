package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}",setmealDTO);

        //将前端传来的DTO数据保存到数据库（新增套餐）
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}",setmealPageQueryDTO);
        // SetmealPageQueryDTO 是一个封装分页查询条件的 DTO 类 --> page(当前页码) pageSize(每页显示条数)
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);

        log.info("分页查询结果：{}",pageResult);

        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启售、停售套餐")
    public Result startOrStop(@PathVariable Integer status,Long id) {
        log.info("启售、停售套餐：{},{}",status,id);

        setmealService.startOrStop(status,id);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("根据id批量删除套餐：{}",ids);

        setmealService.deleteBtach(ids);

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询套餐")
    public Result<SetmealVO> getBySetmealId(@PathVariable Long id) {
        log.info("根据id查询套餐：{}",id);

        SetmealVO setmealVOs = setmealService.getById(id);

        return Result.success(setmealVOs);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}",setmealDTO);

        setmealService.update(setmealDTO);

        return Result.success();
    }
}
