package com.hss.reggie;

import com.hss.reggie.pojo.Employee;
import com.hss.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    private EmployeeService employeeService;
    @Test
    void contextLoads() {
        List<Employee> list = employeeService.list(null);
        System.out.println(list);
    }

}
