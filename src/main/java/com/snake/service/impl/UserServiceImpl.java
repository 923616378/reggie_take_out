package com.snake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.pojo.User;
import com.snake.service.UserService;
import com.snake.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-07-16 14:45:47
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




