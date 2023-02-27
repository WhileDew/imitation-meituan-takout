package com.example.ruijiwaimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruijiwaimai.entity.Category;
import com.example.ruijiwaimai.utils.Result;

public interface ICategoryService extends IService<Category> {
    Result getPages(Integer current, Integer pageSize);

    Result saveCategory(Category category);

    Result updateCategory(Category category);

    Result deleteCategory(Long ids);

    Result getDishList(Integer type);
}
