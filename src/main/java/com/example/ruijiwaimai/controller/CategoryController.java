package com.example.ruijiwaimai.controller;

import com.example.ruijiwaimai.entity.Category;
import com.example.ruijiwaimai.entity.Employee;
import com.example.ruijiwaimai.service.ICategoryService;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 新增菜品/套餐
     */
    @PostMapping
    public Result saveCategory(@RequestBody Category category){
        return categoryService.saveCategory(category);
    }

    /**
     * 修改
     */
    @PutMapping
    public Result updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    /**
     * 删除
     */
    @DeleteMapping
    public Result deleteCategory(@RequestParam Long ids){
        return categoryService.deleteCategory(ids);
    }

    @GetMapping("/list")
    public Result getDishList(@RequestParam(required = false) Integer type){
        return categoryService.getDishList(type);
    }
    /**
     * 分类信息分页
     * @param current 当前页
     * @param pageSize 页面大小
     */
    @GetMapping("/page")
    public Result getPages(@RequestParam(value = "page") Integer current,
                           @RequestParam Integer pageSize){
        return categoryService.getPages(current, pageSize);
    }
}
