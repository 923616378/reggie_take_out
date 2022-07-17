package com.snake.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snake.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
* @author admin
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2022-07-13 17:59:04
* @Entity com.snake.pojo.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




