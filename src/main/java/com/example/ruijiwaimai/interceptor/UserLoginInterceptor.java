package com.example.ruijiwaimai.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断session中是否有用户id
        if (request.getSession().getAttribute("user") == null){
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
