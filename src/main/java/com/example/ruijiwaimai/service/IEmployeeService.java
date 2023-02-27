package com.example.ruijiwaimai.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruijiwaimai.entity.Employee;
import com.example.ruijiwaimai.utils.Result;

import javax.servlet.http.HttpServletRequest;

public interface IEmployeeService extends IService<Employee> {
    Result login(Employee userInfo, HttpServletRequest request);

    Result getPages(Integer page, Integer pageSize, String name);

    Result saveEmployee(Employee employee);

    Result updateEmployee(Employee employee);
}
