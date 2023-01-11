package com.hss.reggie.mapper;

import com.hss.reggie.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author master
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2023-01-02 15:50:42
* @Entity com.hss.reggie.pojo.Employee
*/
public interface EmployeeMapper extends BaseMapper<Employee> {

}




