package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.pojo.Employee;
import com.hss.reggie.service.EmployeeService;
import com.hss.reggie.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author master
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-01-02 15:50:42
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




