package com.example.ruijiwaimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.entity.Employee;
import com.example.ruijiwaimai.mapper.EmployeeMapper;
import com.example.ruijiwaimai.service.IEmployeeService;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Override
    public Result login(Employee userInfo, HttpServletRequest request) {
        // 获取用户名和密码
        String username = userInfo.getUsername();
        // 对密码进行MD5加密处理
        String  password = DigestUtils.md5DigestAsHex(userInfo.getPassword().getBytes());
        // 数据库查询
        Employee employee = query().eq("username", username).one();
        // 判断是否为空
        if (employee == null){
            return Result.error("登录失败");
        }
        // 判断密码是否正确
        if (!employee.getPassword().equals(password)){
            System.out.println(password);
            return Result.error("密码错误！");
        }
        // 判断管理员状态
        if (!employee.getStatus().equals(1)){
            return Result.error("管理员状态异常");
        }
        // 将employee存入session中
        request.getSession().setAttribute("employee", employee.getId());
        return Result.success(employee);
    }

    @Override
    public Result getPages(Integer current, Integer pageSize, String name) {
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotBlank(name), Employee::getName, name);
        Page<Employee> page = page(new Page<>(current, pageSize), queryWrapper);
        if (page.getRecords().isEmpty()){
            log.error("没有查询到与【" + name + "】匹配的用户名");
            return Result.error("没有查询到与" + name + "匹配的用户名");
        }
        page.setTotal(page.getRecords().size());
        return Result.success(page);
    }

    @Override
    public Result saveEmployee(Employee employee) {
        // 判断用户名是否冲突
        String username = employee.getUsername();
        if (query().eq("username", username).one() != null){
            return Result.error("用户名已经存在");
        }
        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 判断是否存储成功
        boolean success = save(employee);
        if (!success){
            return Result.error("保存失败");
        }
        return Result.success("保存成功");
    }

    @Override
    public Result updateEmployee(Employee employee) {
        Long id = employee.getId();
        // 数据库查询id是否存在
        Employee newEmployee = query().eq("id", id).one();
        if (newEmployee == null){
            return Result.error("该用户不存在");
        }
        // 更新用户信息
        update().eq("id", id).update(employee);
        return Result.success("修改成功");
    }
}
