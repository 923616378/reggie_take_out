package com.snake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.pojo.ShoppingCart;
import com.snake.service.ShoppingCartService;
import com.snake.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-07-17 11:24:30
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




