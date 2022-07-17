package com.snake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.dto.SetmealDto;
import com.snake.pojo.Setmeal;
import com.snake.pojo.SetmealDish;
import com.snake.service.SetmealDishService;
import com.snake.service.SetmealService;
import com.snake.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author admin
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2022-07-15 12:08:38
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 保存套餐,套餐信息和里面的具体的菜品关系信息要分别存储到两张表
     * @param setmealDto
     * @return 返回是否成功
     */
    @Override
    @Transactional //开启事务
    public boolean saveSetmealWithDishs(SetmealDto setmealDto) {
        //1.保存套餐的基本信息,不包含菜品关系
        boolean save = this.save(setmealDto);
        //2.遍历菜品集合,设置套餐id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());
        //2.保存菜品关系
        setmealDishService.saveBatch(setmealDishes);
        return save;
    }
}




