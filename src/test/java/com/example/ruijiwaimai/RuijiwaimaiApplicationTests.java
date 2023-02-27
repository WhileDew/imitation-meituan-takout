package com.example.ruijiwaimai;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ruijiwaimai.entity.Dish;
import com.example.ruijiwaimai.entity.User;
import com.example.ruijiwaimai.service.IDishService;
import com.example.ruijiwaimai.service.IEmployeeService;
import com.example.ruijiwaimai.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RuijiwaimaiApplicationTests {

    @Resource
    private IEmployeeService employeeService;

    @Resource
    private IDishService dishService;

    @Test
    void contextLoads() {
        Integer count = employeeService.query().eq("username", "admin").count();
        if (count != null){
            System.out.println("有");
        }
    }

    @Test
    void dishPages() {
        // Page<Dish> page = dishService.query().orderByDesc("update_time").page(new Page<>(1, 10));
        // System.out.println(page.getRecords());
        //
        // page = dishService.query().orderByDesc("update_time").page(new Page<>(2, 10));
        // System.out.println(page.getRecords());
        Page<Dish> page = new Page<>(1, 10);

        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time");

        System.out.println(dishService.page(page, queryWrapper).getRecords());

        page = new Page<>(2, 10);
        System.out.println(dishService.page(page, queryWrapper).getRecords());
    }

    @Test
    void name() {
        User user = new User();
        user.setName("zzzz");
        user.setAvatar("");
        user.setPhone("13333333333");
        user.setIdNumber("350722200209080017");
        user.setSex("1");
        user.setStatus(1);
        UserServiceImpl userService = new UserServiceImpl();
        userService.updateById(user);
    }
}
