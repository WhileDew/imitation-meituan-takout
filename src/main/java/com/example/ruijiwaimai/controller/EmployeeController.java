package com.example.ruijiwaimai.controller;

import com.example.ruijiwaimai.entity.Employee;
import com.example.ruijiwaimai.service.IEmployeeService;
import com.example.ruijiwaimai.utils.OnlineHolder;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private IEmployeeService employeeService;

    /**
     * 新增员工
     * @param employee 表单上传的数据
     */
    @PostMapping
    public Result saveEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    /**
     * 修改用户信息
     */
    @PutMapping
    public Result updateEmployee(@RequestBody Employee employee){
        return employeeService.updateEmployee(employee);
    }

    /**
     * 按id获取员工信息
     */
    @GetMapping("/{id}")
    public Result getEmployeeById(@PathVariable Long id){
        Employee employee = employeeService.query().eq("id", id).one();
        return Result.success(employee);
    }

    /**
     * 员工登录
     * @param userInfo 用户名及密码
     */
    @PostMapping("/login")
    public Result login(@RequestBody Employee userInfo, HttpServletRequest request){
        return employeeService.login(userInfo, request);
    }

    /**
     * 员工登出
     */
    @PostMapping("/logout")
    public Result logout(){
        OnlineHolder.removeId();
        return Result.success("退出管理员成功");
    }

    /**
     * 员工信息分页
     * @param current 当前页
     * @param pageSize 页面大小
     */
    @GetMapping("/page")
    public Result getPages(@RequestParam(value = "page") Integer current,
                           @RequestParam Integer pageSize,
                           @RequestParam(required = false) String name){
        return employeeService.getPages(current, pageSize, name);
    }

}
