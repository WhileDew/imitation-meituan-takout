package com.example.ruijiwaimai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.dto.OrdersDto;
import com.example.ruijiwaimai.entity.AddressBook;
import com.example.ruijiwaimai.entity.OrderDetail;
import com.example.ruijiwaimai.entity.Orders;
import com.example.ruijiwaimai.entity.ShoppingCart;
import com.example.ruijiwaimai.mapper.OrdersMapper;
import com.example.ruijiwaimai.service.*;
import com.example.ruijiwaimai.utils.OnlineHolder;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Resource
    private IOrderDetailService orderDetailService;

    @Resource
    private IUserService userService;

    @Resource
    private IShoppingCartService shoppingCartService;

    @Resource
    private IAddressBookService addressBookService;

    @Override
    public Result getPages(Integer current, Integer pageSize, Long number, LocalDateTime beginTime, LocalDateTime endTime) {
        // 判断number是否存在
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(number != null, Orders::getNumber, number)
                .between(Orders::getOrderTime, beginTime, endTime);
        // 分页
        Page<Orders> page = page(new Page<>(current, pageSize), queryWrapper);
        // 获取当前页数据
        Page<OrdersDto> ordersDtoPage = new Page<>();
        BeanUtil.copyProperties(page, ordersDtoPage, "records");
        // 将orders转换为dto对象
        List<OrdersDto> ordersDtoList = page.getRecords().stream().map(
                item -> {
                    OrdersDto ordersDto = new OrdersDto();
                    BeanUtil.copyProperties(item, ordersDto);
                    // 设置用户名
                    String username = userService.getById(item.getUserName()).getName();
                    ordersDto.setUserName(username);
                    // 设置
                    List<OrderDetail> orderDetails = orderDetailService.query().eq("order_id", item.getId()).list();
                    ordersDto.setOrderDetails(orderDetails);
                    return ordersDto;
                }
        ).collect(Collectors.toList());
        // 设置dto属性
        ordersDtoPage.setRecords(ordersDtoList);
        page.setTotal(list().size());
        return Result.success(ordersDtoPage);
    }

    @Override
    @Transactional
    public Result submit(Orders orders) {
        // 获取当前用户id
        Long userId = OnlineHolder.getId();
        orders.setUserId(userId);
        Long orderId = IdWorker.getId();
        orders.setNumber(orderId.toString());
        // 查询购物车
        List<ShoppingCart> shoppingCarts = shoppingCartService.query().eq("user_id", userId).list();
        if (shoppingCarts.isEmpty()){
            return Result.error("购物车为空无法下单");
        }
        BigDecimal amount = new BigDecimal(0);
        for (ShoppingCart shoppingCart : shoppingCarts) {
            // 计算总价
            amount = amount.add(shoppingCart.getAmount());
            // 向订单详情插入数据
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(shoppingCarts.size());
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetailService.save(orderDetail);
        }
        orders.setAmount(amount);
        // 查询地址簿
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        orders.setUserName(userService.getById(userId).getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        // 设置订单属性
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        // 插入订单表
        save(orders);
        // 删除购物车中的内容
        shoppingCartService.remove(new QueryWrapper<ShoppingCart>().eq("user_id", userId));
        return Result.success("下单成功");
    }

    @Override
    public Result userPage(Integer current, Integer pageSize) {
        // 获取userId
        Long userId = OnlineHolder.getId();
        Page<Orders> ordersPage = new Page<>(current, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>(current, pageSize);
        BeanUtil.copyProperties(ordersPage, ordersDtoPage, "orderDetails");
        // 存储dto
        List<OrdersDto> ordersDtos = new ArrayList<>();
        // 遍历orders
        query().eq("user_id", userId).list().forEach(
                order -> {
                    OrdersDto ordersDto = BeanUtil.copyProperties(order, OrdersDto.class, "");
                    // 查询订单详情
                    List<OrderDetail> orderDetails = orderDetailService.query().eq("order_id", order.getId()).list();
                    // 存入
                    ordersDto.setOrderDetails(orderDetails);
                    ordersDtos.add(ordersDto);
                }
        );
        // 设置页面属性
        ordersDtoPage.setRecords(ordersDtos);
        ordersDtoPage.setTotal(ordersDtos.size());
        return Result.success(ordersDtoPage);
    }
}
