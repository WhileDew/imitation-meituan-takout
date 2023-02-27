package com.example.ruijiwaimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruijiwaimai.entity.ShoppingCart;
import com.example.ruijiwaimai.utils.Result;

public interface IShoppingCartService extends IService<ShoppingCart> {
    Result add(ShoppingCart shoppingCart);

}
