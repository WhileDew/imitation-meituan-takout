package com.example.ruijiwaimai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.dto.DishDto;
import com.example.ruijiwaimai.dto.SetmealDto;
import com.example.ruijiwaimai.entity.Dish;
import com.example.ruijiwaimai.entity.DishFlavor;
import com.example.ruijiwaimai.entity.Setmeal;
import com.example.ruijiwaimai.entity.SetmealDish;
import com.example.ruijiwaimai.mapper.SetmealMapper;
import com.example.ruijiwaimai.service.*;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

    @Resource
    private ISetmealDishService setmealDishService;

    @Resource
    private ICategoryService categoryService;

    @Resource
    private IDishService dishService;

    @Resource
    private IDishFlavorService dishFlavorService;

    @Override
    public Result getPages(Integer current, Integer pageSize, String name) {
        // 构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotBlank(name), Setmeal::getName, name).orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> page = page(new Page<>(current, pageSize), queryWrapper);
        // 获取当前页数据
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtil.copyProperties(page, setmealDtoPage, "records");
        // 将dish转换为dto对象
        List<SetmealDto> setmealDtoList = page.getRecords().stream().map(
                item -> {
                    SetmealDto setmealDto = new SetmealDto();
                    BeanUtil.copyProperties(item, setmealDto);
                    // 设置餐品类型
                    String categoryName = categoryService.getById(item.getCategoryId()).getName();
                    setmealDto.setCategoryName(categoryName);
                    // 设置口味
                    List<SetmealDish> setmealDishes = setmealDishService.query().eq("setmeal_id", item.getId()).list();
                    setmealDto.setSetmealDishes(setmealDishes);
                    return setmealDto;
                }
        ).collect(Collectors.toList());
        // 设置dto属性
        setmealDtoPage.setRecords(setmealDtoList);
        page.setTotal(list().size());
        return Result.success(setmealDtoPage);
    }

    @Override
    public Result saveSetmeal(Setmeal setmeal) {
        boolean success = save(setmeal);
        if (!success){
            log.error("保存失败");
            return Result.error("保存失败");
        }
        return Result.success("保存成功");
    }

    @Override
    public Result deleteSetmeal(String ids) {
        // 对ids进行分割
        List<Long> idList = Arrays.stream(ids.split(",")).map(
                Long::valueOf
        ).collect(Collectors.toList());
        // 判断数据库中是否有id
        for (Long id : idList) {
            Setmeal setmeal = getById(id);
            if (setmeal == null){
                log.error("删除错误");
                return Result.error("删除错误");
            }
            // 进行删除操作
            boolean success = removeById(id);
            if (!success){
                log.error("删除错误");
                return Result.error("删除错误");
            }
        }
        return Result.success("删除成功");
    }

    @Override
    public Result getSetmealById(Long id) {
        Setmeal setmeal = getById(id);
        if (setmeal == null){
            return Result.error("暂时还没有这款菜");
        }
        // 转换为数据传输对象
        SetmealDto setmealDto = BeanUtil.copyProperties(setmeal, SetmealDto.class, "");
        List<SetmealDish> setmealDishes = setmealDishService.query().eq("setmeal_id", id).list();
        setmealDto.setSetmealDishes(setmealDishes);
        return Result.success(setmealDto);
    }

    @Override
    public Result updateStatus(Integer status, String ids) {
        // 对ids进行分割
        List<Long> idList = Arrays.stream(ids.split(",")).map(
                Long::valueOf
        ).collect(Collectors.toList());
        // 判断数据库中是否有id
        for (Long id : idList) {
            Setmeal setmeal = getById(id);
            if (setmeal == null){
                log.error("更新状态错误");
                return Result.error("更新状态错误");
            }
            // 进行状态修改操作
            setmeal.setStatus(status);
            boolean success = updateById(setmeal);
            if (!success){
                log.error("更新状态错误");
                return Result.error("更新状态错误");
            }
        }
        return Result.success("更新状态成功");
    }

    @Override
    public Result updateSetmeal(SetmealDto setmealDto) {
        Long setmealId = setmealDto.getId();
        // 判断dish是否存在
        if (getById(setmealId) == null){
            log.error("修改异常");
            return Result.error("修改异常");
        }
        // 更新setmeal
        updateById(setmealDto);
        // 更新dish_flavor,先进行删除操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        setmealDishService.remove(queryWrapper);
        // 再进行插入
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(s -> s.setSetmealId(setmealId));
        setmealDishService.saveBatch(setmealDishes);
        return Result.success("插入成功");
    }

    @Override
    public Result getSetmealList(Long categoryId, Integer type) {

        // 获取setmealId
        List<Long> setmealId = query()
                .eq(type != null, "status", type)
                .eq(categoryId != null, "category_id", categoryId)
                .list().stream().map(Setmeal::getId).collect(Collectors.toList());
        List<SetmealDto> setmealDtos = new ArrayList<>();
        // 查询套餐
        listByIds(setmealId).forEach(
                setmeal -> {
                    List<SetmealDish> setmealDishes = setmealDishService.query().eq("setmeal_id", setmeal.getId()).list();
                    SetmealDto setmealDto = BeanUtil.copyProperties(setmeal, SetmealDto.class, "");
                    setmealDto.setSetmealDishes(setmealDishes);
                    setmealDtos.add(setmealDto);
                }
        );
        if (setmealDtos.isEmpty()){
            return Result.error("查询错误");
        }
        return Result.success(setmealDtos);
    }

    @Override
    public Result getDishById(Long id) {
        // 判断id是否为空
        if (id == null){
            return Result.error("id不能为空");
        }
        // 数据库查询
        Dish dish = dishService.getById(id);
        if (dish == null){
            return Result.error("暂时还没有这道菜品");
        }
        return Result.success(dish);
    }
}
