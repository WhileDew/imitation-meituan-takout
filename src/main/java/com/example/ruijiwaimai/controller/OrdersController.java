package com.example.ruijiwaimai.controller;

import com.example.ruijiwaimai.entity.Orders;
import com.example.ruijiwaimai.service.IOrdersService;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Resource
    private IOrdersService ordersService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Orders orders){
        return ordersService.submit(orders);
    }

    @GetMapping("/userPage")
    public Result userPage(@RequestParam(value = "page") Integer current,
                           @RequestParam Integer pageSize){
        return ordersService.userPage(current, pageSize);
    }

    @GetMapping("/page")
    public Result getPages(@RequestParam(value = "page") Integer current,
                           @RequestParam Integer pageSize,
                           @RequestParam(required = false) Long number,
                           @RequestParam(required = false) LocalDateTime beginTime,
                           @RequestParam(required = false) LocalDateTime endTime){
        return ordersService.getPages(current, pageSize, number, beginTime, endTime);
    }

}
