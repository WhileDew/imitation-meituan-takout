package com.example.ruijiwaimai.controller;

import com.example.ruijiwaimai.dto.DishDto;
import com.example.ruijiwaimai.entity.Dish;
import com.example.ruijiwaimai.entity.DishFlavor;
import com.example.ruijiwaimai.service.IDishFlavorService;
import com.example.ruijiwaimai.service.IDishService;
import com.example.ruijiwaimai.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private IDishService dishService;

    /**
     * 新增菜品
     */
    @PostMapping
    public Result saveDish(@RequestBody Dish dish){
        return dishService.saveDish(dish);
    }

    @GetMapping("/{id}")
    public Result getDishById(@PathVariable Long id){
        return dishService.getDishById(id);
    }

    @GetMapping("/list")
    public Result getList(@RequestParam Long categoryId){
        return dishService.getList(categoryId);
    }

    /**
     * 批量删除
     * @param ids 批量id
     */
    @DeleteMapping
    public Result deleteDish(@RequestParam String ids){
        return dishService.deleteDish(ids);
    }

    /**
     * 停售菜品
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, @RequestParam String ids){
        return dishService.updateStatus(status, ids);
    }

    @PutMapping
    public Result updateDish(@RequestBody DishDto dishDto){
        return dishService.updateDish(dishDto);
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
        return dishService.getPages(current, pageSize, name);
    }
}
