package com.example.ruijiwaimai.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.constans.RedisConstants;
import com.example.ruijiwaimai.dto.UserDto;
import com.example.ruijiwaimai.entity.User;
import com.example.ruijiwaimai.mapper.UserMapper;
import com.example.ruijiwaimai.service.IUserService;
import com.example.ruijiwaimai.utils.RegexUtils;
import com.example.ruijiwaimai.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.example.ruijiwaimai.constans.RedisConstants.PHONE_CODE_TIMEUNIT;
import static com.example.ruijiwaimai.constans.RedisConstants.PHONE_CODE_TTL;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendMsg(User user) {
        Result result = regexPhone(user);
        if (result != null){
            return result;
        }
        // 生成六位随机码
        String code = RandomUtil.randomNumbers(6);
        log.info("验证码为：" + code);
        // 存入redis中,三分钟过期
        stringRedisTemplate.opsForValue().set(RedisConstants.PHONE_CODE_KEY + user.getPhone(), code, PHONE_CODE_TTL, PHONE_CODE_TIMEUNIT);
        return Result.success("发送成功");
    }

    @Override
    public Result login(UserDto userDto, HttpServletRequest request) {
        Result result = regexPhone(userDto);
        if (result != null){
            return result;
        }
        // 判断验证码是否正确
        String phone = userDto.getPhone();
        String code = stringRedisTemplate.opsForValue().get(RedisConstants.PHONE_CODE_KEY + phone);
        if (!userDto.getCode().equals(code)){
            return Result.error("验证码输入错误");
        }
        // 将用户id存入session中
        Long id = query().eq("phone", phone).one().getId();
        request.getSession().setAttribute("user", id);
        return Result.success(code);
    }

    private Result regexPhone(User user) {
        String phone = user.getPhone();
        // 判断手机号是否为空
        if (phone.isEmpty()){
            return Result.error("手机号不能为空");
        }
        // 判断手机号格式
        if (RegexUtils.isPhoneInvalid(phone)) {
            log.error("手机号格式错误");
            return Result.error("手机号格式错误");
        }
        return null;
    }
}
