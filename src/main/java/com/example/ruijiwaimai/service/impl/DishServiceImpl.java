package com.example.ruijiwaimai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.constans.RedisConstants;
import com.example.ruijiwaimai.dto.DishDto;
import com.example.ruijiwaimai.entity.Dish;
import com.example.ruijiwaimai.entity.DishFlavor;
import com.example.ruijiwaimai.mapper.DishMapper;
import com.example.ruijiwaimai.service.ICategoryService;
import com.example.ruijiwaimai.service.IDishFlavorService;
import com.example.ruijiwaimai.service.IDishService;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.ruijiwaimai.constans.RabbitMQConstants.*;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private IDishFlavorService dishFlavorService;

    @Resource
    private ICategoryService categoryService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result getPages(Integer current, Integer pageSize, String name) {
        // 构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotBlank(name), Dish::getName, name).orderByDesc(Dish::getUpdateTime);
        Page<Dish> page = page(new Page<>(current, pageSize), queryWrapper);
        // 获取当前页数据
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtil.copyProperties(page, dishDtoPage, "records");
        // 将dish转换为dto对象
        List<DishDto> dishDtoList = page.getRecords().stream().map(
                item -> {
                    DishDto dishDto = new DishDto();
                    BeanUtil.copyProperties(item, dishDto);
                    // 设置餐品类型
                    String categoryName = categoryService.getById(item.getCategoryId()).getName();
                    dishDto.setCategoryName(categoryName);
                    // 设置口味
                    List<DishFlavor> dishFlavors = dishFlavorService.query().eq("dish_id", item.getId()).list();
                    dishDto.setFlavors(dishFlavors);
                    return dishDto;
                }
        ).collect(Collectors.toList());
        // 设置dto属性
        dishDtoPage.setRecords(dishDtoList);
        page.setTotal(list().size());
        return Result.success(dishDtoPage);
    }

    @Override
    public Result saveDish(Dish dish) {
        boolean success = save(dish);
        if (!success){
            log.error("保存失败");
            return Result.error("保存失败");
        }
        Long categoryId = dish.getCategoryId();
        // 存入消息队列
        log.error("新增");
        rabbitTemplate.convertAndSend(DISH_EXCHANGE_KEY, DISH_INSERT_KEY, categoryId);

        return Result.success("保存成功");
    }

    @Override
    public Result deleteDish(String ids) {
        // 对ids进行分割
        List<Long> idList = Arrays.stream(ids.split(",")).map(
                Long::valueOf
        ).collect(Collectors.toList());
        // 判断数据库中是否有id
        for (Long id : idList) {
            Dish dish = getById(id);
            if (dish == null){
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
        // 存入消息队列
        rabbitTemplate.convertAndSend(DISH_EXCHANGE_KEY, DISH_INSERT_KEY);
        return Result.success("删除成功");
    }

    @Override
    public Result updateStatus(Integer status, String ids) {
        // 对ids进行分割
        List<Long> idList = Arrays.stream(ids.split(",")).map(
                Long::valueOf
        ).collect(Collectors.toList());
        // 判断数据库中是否有id
        for (Long id : idList) {
            Dish dish = getById(id);
            if (dish == null){
                log.error("更新状态错误");
                return Result.error("更新状态错误");
            }
            // 进行状态修改操作
            dish.setStatus(status);
            boolean success = updateById(dish);
            if (!success){
                log.error("更新状态错误");
                return Result.error("更新状态错误");
            }
        }
        log.error("修改");
        // 存入消息队列
        rabbitTemplate.convertAndSend(DISH_EXCHANGE_KEY, DISH_DELETE_KEY);
        return Result.success("更新状态成功");
    }

    @Override
    public Result getDishById(Long id) {
        Dish dish = getById(id);
        if (dish == null){
            return Result.error("暂时还没有这款菜");
        }
        // 转换为数据传输对象
        DishDto dishDto = BeanUtil.copyProperties(dish, DishDto.class, "");
        List<DishFlavor> dishFlavors = dishFlavorService.query().eq("dish_id", id).list();
        dishDto.setFlavors(dishFlavors);
        return Result.success(dishDto);
    }

    @Override
    public Result updateDish(DishDto dishDto) {
        Long dishId = dishDto.getId();
        // 判断dish是否存在
        if (getById(dishId) == null){
            log.error("修改异常");
            return Result.error("修改异常");
        }
        // 更新dish
        updateById(dishDto);
        // 更新dish_flavor,先进行删除操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(queryWrapper);
        // 再进行插入
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });
        log.error(dishDto.getFlavors().toString());
        dishFlavorService.saveBatch(flavors);
        // 更新缓存
        Long categoryId = dishDto.getCategoryId();
        // 存入消息队列
        rabbitTemplate.convertAndSend(DISH_EXCHANGE_KEY, DISH_INSERT_KEY, categoryId);
        return Result.success("插入成功");
    }

    @Override
    public Result getList(Long categoryId) {
        if (categoryId == null){
            return Result.error("查询错误");
        }
        // redis中查询
        String key = RedisConstants.CACHE_DISHDTO_KEY + categoryId;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (cache != null){
            // 转换为json数组
            JSONArray jsonArray = JSONUtil.parseArray(cache);
            List<DishDto> dishDtoList = jsonArray.toList(DishDto.class);
            return Result.success(dishDtoList);
        }
        ArrayList<DishDto> dishDtoList = new ArrayList<>();
        // 按照分类查询菜品
        // 查询dish口味
        query().eq("status", 1).eq( "category_id", categoryId).list()
                .forEach(dish -> {
                    DishDto dishDto = BeanUtil.copyProperties(dish, DishDto.class, "");
                    dishDto.setFlavors(dishFlavorService.query().eq("dish_id", dish.getId()).list());
                    dishDtoList.add(dishDto);
                });
        if (dishDtoList.isEmpty()){
            return Result.error("该分类暂时还没有菜品");
        }
        // 缓存到redis中,5分钟
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(dishDtoList), RedisConstants.CACHE_KEY_TTL, RedisConstants.CACHE_KEY_TIMEUNIT);
        return Result.success(dishDtoList);
    }
}
