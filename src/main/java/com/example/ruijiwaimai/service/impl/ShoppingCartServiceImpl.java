package com.example.ruijiwaimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.entity.ShoppingCart;
import com.example.ruijiwaimai.mapper.ShoppingCartMapper;
import com.example.ruijiwaimai.service.IShoppingCartService;
import com.example.ruijiwaimai.utils.OnlineHolder;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

    @Override
    public Result add(ShoppingCart shoppingCart) {
        // 获取用户id
        Long userId = OnlineHolder.getId();
        shoppingCart.setUserId(userId);
        // 查询数据库中是否已经添加菜品/套餐
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        // 绑定当前的用户
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        // 添加的是菜品
        if (dishId != null){
            // 查看数据库中是否有该条记录(口味相同)
            queryWrapper.eq(ShoppingCart::getDishId,dishId)
                    .eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
        }else {
            // 添加的是套餐
            // 查看数据库中是否有该条记录
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        handlerUpdate(shoppingCart, queryWrapper);
        return Result.success("添加成功");
    }

    private void handlerUpdate(ShoppingCart shoppingCart, LambdaQueryWrapper<ShoppingCart> queryWrapper) {
        ShoppingCart one = getOne(queryWrapper);
        if (one != null) {
            // 同一商品数量+1
            one.setNumber(one.getNumber() + 1);
            // 价格++
            one.setAmount(one.getAmount().add(shoppingCart.getAmount()));
            update(queryWrapper);
        } else {
            // 不存在新增
            save(shoppingCart);
        }
    }
}
