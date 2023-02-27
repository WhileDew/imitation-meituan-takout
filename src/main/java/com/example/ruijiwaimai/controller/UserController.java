package com.example.ruijiwaimai.controller;

import cn.hutool.core.util.RandomUtil;
import com.example.ruijiwaimai.dto.UserDto;
import com.example.ruijiwaimai.entity.User;
import com.example.ruijiwaimai.service.IUserService;
import com.example.ruijiwaimai.utils.OnlineHolder;
import com.example.ruijiwaimai.utils.RegexUtils;
import com.example.ruijiwaimai.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto, HttpServletRequest request){
        return userService.login(userDto, request);
    }

    @PostMapping("/loginout")
    public Result login(HttpServletRequest request){
        OnlineHolder.removeId();
        request.getSession().removeAttribute("user");
        return Result.success("退出成功");
    }

    @PostMapping("/sendMsg")
    public Result sendMsg(@RequestBody User user){
        return userService.sendMsg(user);
    }

    @GetMapping
    public Result getUser(){
        Long userId = OnlineHolder.getId();
        User user = userService.getById(userId);
        if (user == null){
            return Result.error("该用户不存在");
        }
        return Result.success(user);
    }

}
