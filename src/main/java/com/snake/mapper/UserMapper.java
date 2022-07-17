package com.snake.mapper;

import com.snake.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author admin
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2022-07-16 14:45:47
* @Entity com.snake.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




