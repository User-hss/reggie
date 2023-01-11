package com.hss.reggie.service;

import com.hss.reggie.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author master
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2023-01-02 15:50:42
*/
@Transactional
public interface EmployeeService extends IService<Employee> {

}
