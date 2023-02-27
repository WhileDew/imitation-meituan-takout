package com.example.ruijiwaimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruijiwaimai.dto.DishDto;
import com.example.ruijiwaimai.entity.Dish;
import com.example.ruijiwaimai.utils.Result;

public interface IDishService extends IService<Dish> {
    Result getPages(Integer current, Integer pageSize, String name);

    Result saveDish(Dish dish);

    Result deleteDish(String ids);

    Result updateStatus(Integer status, String ids);

    Result getDishById(Long id);

    Result updateDish(DishDto dishDto);

    Result getList(Long categoryId);

}
