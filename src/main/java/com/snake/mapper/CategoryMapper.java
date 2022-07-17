package com.snake.mapper;

import com.snake.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author admin
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2022-07-15 11:07:08
* @Entity com.snake.pojo.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




