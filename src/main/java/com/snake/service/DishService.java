package com.snake.service;

import com.snake.dto.DishDto;
import com.snake.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-07-15 12:08:38
*/
public interface DishService extends IService<Dish> {

    boolean saveWithFlavors(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    boolean updateDishWithFlavor(DishDto dishDto);
}
