package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.reggie.common.R;
import com.hss.reggie.pojo.Employee;
import com.hss.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    public R<Employee> login(HttpSession session, @RequestBody Employee employee){
        //根据用户名查询
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee one = employeeService.getOne(wrapper);
        //判断
        if (one == null) {
            return R.error("用户名不存在！");
        }
        //密码md5加密
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //密码对比
        if(!one.getPassword().equals(password)){
            return R.error("密码错误！");
        }
        //查看员工状态
        if(one.getStatus()==0){
            return R.error("该账号已封禁！");
        }
        //登录成功，员工id存入session
        session.setAttribute("employee",one.getId());
        return R.success(one);
    }
    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("employee");
        return R.success("登录成功!");
    }
    /**
     * 添加员工
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee){
        //将身份证号码后6位作为初始密码
        String idNumber = employee.getIdNumber();
        String pwd = idNumber.substring(idNumber.length() - 6);
        String md5=DigestUtils.md5DigestAsHex(pwd.getBytes());
        employee.setPassword(md5);
        employeeService.save(employee);
        return R.success("创建成功");
    }
    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page> getPage(Integer page,Integer pageSize,String name){
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper=new LambdaQueryWrapper<>();
        //若name不为空则添加模糊查询条件
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);
        //查询
        employeeService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
    /**
     * 根据id查询单个员工
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return R.error("没有查询到员工信息");
        }
        return R.success(employee);
    }
    /**
     * 根据id做员工修改
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }
}
