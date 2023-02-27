package com.example.ruijiwaimai.controller;

import com.example.ruijiwaimai.dto.SetmealDto;
import com.example.ruijiwaimai.entity.Setmeal;
import com.example.ruijiwaimai.service.ISetmealService;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    private ISetmealService setmealService;

    /**
     * 新增菜品
     */
    @PostMapping
    public Result saveSetmeal(@RequestBody Setmeal setmeal){
        return setmealService.saveSetmeal(setmeal);
    }

    @GetMapping("/{id}")
    public Result getSetmealById(@PathVariable Long id){
        return setmealService.getSetmealById(id);
    }

    @GetMapping("/list")
    public Result getSetmealList(@RequestParam Long categoryId, @RequestParam(required = false) Integer type){
        return setmealService.getSetmealList(categoryId, type);
    }

    @GetMapping("/dish/{id}")
    public Result getDishById(@PathVariable Long id){
        return setmealService.getDishById(id);
    }

    /**
     * 批量删除
     * @param ids 批量id
     */
    @DeleteMapping
    public Result deleteSetmeal(@RequestParam String ids){
        return setmealService.deleteSetmeal(ids);
    }

    /**
     * 停售菜品
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, @RequestParam String ids){
        return setmealService.updateStatus(status, ids);
    }

    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDto setmealDto){
        return setmealService.updateSetmeal(setmealDto);
    }

    /**
     * 分类信息分页
     * @param current 当前页
     * @param pageSize 页面大小
     */
    @GetMapping("/page")
    public Result getPages(@RequestParam(value = "page") Integer current,
                           @RequestParam Integer pageSize,
                           @RequestParam(required = false) String name){
        return setmealService.getPages(current, pageSize, name);
    }

}
