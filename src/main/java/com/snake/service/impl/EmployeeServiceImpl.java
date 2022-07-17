package com.snake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.pojo.Employee;
import com.snake.service.EmployeeService;
import com.snake.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-07-13 17:59:04
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




