package com.example.ruijiwaimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.entity.OrderDetail;
import com.example.ruijiwaimai.mapper.OrderDetailMapper;
import com.example.ruijiwaimai.service.IOrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {
}
