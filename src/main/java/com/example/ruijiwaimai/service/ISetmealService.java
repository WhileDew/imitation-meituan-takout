package com.example.ruijiwaimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruijiwaimai.dto.SetmealDto;
import com.example.ruijiwaimai.entity.Setmeal;
import com.example.ruijiwaimai.utils.Result;

public interface ISetmealService extends IService<Setmeal> {
    Result getPages(Integer current, Integer pageSize, String name);

    Result saveSetmeal(Setmeal setmeal);

    Result deleteSetmeal(String ids);

    Result getSetmealById(Long id);

    Result updateStatus(Integer status, String ids);

    Result updateSetmeal(SetmealDto setmealDto);

    Result getSetmealList(Long categoryId, Integer type);

    Result getDishById(Long id);
}
