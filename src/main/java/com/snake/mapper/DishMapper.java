package com.snake.mapper;

import com.snake.pojo.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author admin
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2022-07-15 12:08:38
* @Entity com.snake.pojo.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




