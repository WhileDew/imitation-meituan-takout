package com.example.ruijiwaimai.controller;

import com.example.ruijiwaimai.entity.ShoppingCart;
import com.example.ruijiwaimai.service.IShoppingCartService;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private IShoppingCartService shoppingService;

    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart shoppingCart){
        return shoppingService.add(shoppingCart);
    }

    @GetMapping("/list")
    public Result getList(){
        List<ShoppingCart> shoppingCarts = shoppingService.list();
        return Result.success(shoppingCarts);
    }

}
