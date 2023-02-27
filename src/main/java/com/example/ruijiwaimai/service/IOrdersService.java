package com.example.ruijiwaimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruijiwaimai.entity.Orders;
import com.example.ruijiwaimai.utils.Result;

import java.time.LocalDateTime;

public interface IOrdersService extends IService<Orders> {
    Result getPages(Integer current, Integer pageSize, Long number, LocalDateTime beginTime, LocalDateTime endTime);

    Result submit(Orders orders);

    Result userPage(Integer current, Integer pageSize);
}
