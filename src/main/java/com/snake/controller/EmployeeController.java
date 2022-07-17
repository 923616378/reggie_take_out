package com.snake.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snake.common.R;
import com.snake.pojo.Employee;
import com.snake.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 用户进行登录
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将密码加密成md5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        // 2.查询数据库用户名是否存在
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee one = employeeService.getOne(lqw);
        //3.不存在返回error
        if (one == null){
            return R.error("用户不存在!");
        }
        //4.判断密码是否正确,不正确返回error
        if (!one.getPassword().equals(password)){
            return R.error("密码不正确!");
        }
        //5.判断用户是否被封号, 封号返回error
        if (one.getStatus() == 0)
        {
            return R.error("该用户已经被禁用!");
        }
        //6.将用户id保存到session
        request.getSession().setAttribute("employee",one.getId());
        //返回结果
        return R.success(one);
    }
    /**
     * 用户进行退出
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清除session中的用户id
        request.getSession().removeAttribute("employee");
        //返回退出成功
        return R.success("退出成功");
    }

    /**
     * 添加用户请求
     */
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request,@RequestBody Employee employee){
        //输出用户信息
        log.info("新增用户,用户信息{}",employee);
        //设置初始密码为123456,并采用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建时间和创建用户
//        employee.setCreateTime(LocalDateTime.now()); //创建时间为现在
//        employee.setUpdateTime(LocalDateTime.now()); //修改时间为现在
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee")); //设置创建用户的id为当前登录用户id
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee")); //设置修改用户的id为当前登录用户id
        //调用service方法,保存到数据库
        boolean line = employeeService.save(employee);
        //返回退出成功
        return R.success("保存成功");
    }

    /**
     * 分页查询
     * @param page 当前页
     * @param pageSize 每页大小
     * @param name  查询姓名
     * @return 返回 分页查询结果
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(Integer page,Integer pageSize,String name){
        //1.输出参数内容
        log.info("分页查询, 当前页:{},每页条数:{},查询姓名:{}",page,pageSize,name);
        //2.创建条件构造器,设置条件
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<Employee>();
        //3.设置条件, 如果name不为空,才添加 name作为条件
        lqw.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //4.创建分页对象,传入当前页,和每页大小
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        //5.调用分页查询方法,查询的信息会封装到pageInfo里面
        employeeService.page(pageInfo, lqw);
        //6.返回结果
        return R.success(pageInfo);
    }

    /**
     * 修改员工,
     *  对应两种请求, 一个携带id和状态
     *   一种,携带id和其他属性
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        //1.输出员工数据
        log.info("员工数据:{}",employee);
        //2.设置修改时间,和修改用户
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        //2.调用修改方法
        boolean result = employeeService.updateById(employee);
        return result?R.success("修改成功"):R.error("修改失败");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("查询的id是{}",id);
        Employee employee = employeeService.getById(id);
        return employee != null? R.success(employee):R.error("该用户不存在");
    }
}
