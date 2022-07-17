package com.snake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.pojo.DishFlavor;
import com.snake.service.DishFlavorService;
import com.snake.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-07-15 16:48:43
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




