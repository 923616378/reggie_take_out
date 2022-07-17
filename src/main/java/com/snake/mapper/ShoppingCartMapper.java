package com.snake.mapper;

import com.snake.pojo.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author admin
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2022-07-17 11:24:30
* @Entity com.snake.pojo.ShoppingCart
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




