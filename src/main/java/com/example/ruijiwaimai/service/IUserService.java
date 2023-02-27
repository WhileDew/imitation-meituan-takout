package com.example.ruijiwaimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruijiwaimai.dto.UserDto;
import com.example.ruijiwaimai.entity.User;
import com.example.ruijiwaimai.utils.Result;

import javax.servlet.http.HttpServletRequest;

public interface IUserService extends IService<User> {
    Result sendMsg(User user);

    Result login(UserDto userDto, HttpServletRequest request);
}
