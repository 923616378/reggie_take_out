package com.snake.service;

import com.snake.dto.SetmealDto;
import com.snake.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-07-15 12:08:38
*/
public interface SetmealService extends IService<Setmeal> {

    boolean saveSetmealWithDishs(SetmealDto setmealDto);
}
