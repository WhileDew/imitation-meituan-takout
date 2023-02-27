package com.example.ruijiwaimai.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.entity.Category;
import com.example.ruijiwaimai.entity.Dish;
import com.example.ruijiwaimai.entity.Setmeal;
import com.example.ruijiwaimai.mapper.CategoryMapper;
import com.example.ruijiwaimai.service.ICategoryService;
import com.example.ruijiwaimai.service.IDishService;
import com.example.ruijiwaimai.service.ISetmealService;
import com.example.ruijiwaimai.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Resource
    private IDishService dishService;

    @Resource
    private ISetmealService setmealService;

    @Override
    public Result getPages(Integer current, Integer pageSize) {
        // 分页查询
        Page<Category> page = query().orderByAsc("sort").page(new Page<>(current, pageSize));
        page.setTotal(list().size());
        if (page.getRecords().isEmpty()){
            return Result.error("暂时还没有分类");
        }
        return Result.success(page);
    }

    @Override
    public Result saveCategory(Category category) {
        boolean success = save(category);
        if (success){
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }

    @Override
    public Result updateCategory(Category category) {
        // 判断该分类是否存在
        Category newCategory = getById(category.getId());
        if (newCategory == null){
            return Result.error("修改错误");
        }
        // 更新
        updateById(category);
        return Result.success("修改成功");
    }

    @Override
    public Result deleteCategory(Long ids) {
        // 判断是否有此项分类
        Category category = getById(ids);
        if (category == null){
            return Result.error("删除错误");
        }
        // 判断是否有关联菜品/套餐
        Integer dish = dishService.query().eq("category_id", ids).count();
        Integer setmeal = setmealService.query().eq("category_id", ids).count();
        if (dish != null || setmeal != null){
            // 提醒用户
            return Result.error("该分类有关联菜品，无法删除");
        }
        // 无关联菜品
        boolean success = removeById(ids);
        if (!success){
            log.error("删除错误");
            return Result.error("删除错误");
        }
        return Result.success("删除成功");
    }

    @Override
    public Result getDishList(Integer type) {
        // 查询菜品列表
        List<Category> types = query().eq(type != null, "type", type).list();
        return Result.success(types);
    }
}
