package com.example.ruijiwaimai.config;

import com.example.ruijiwaimai.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

// @Configuration
public class MvcConfig implements WebMvcConfigurer{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .excludePathPatterns(
                        "/user/login",
                        "/user/code",
                        "/"
                ).order(1);
    }
}
