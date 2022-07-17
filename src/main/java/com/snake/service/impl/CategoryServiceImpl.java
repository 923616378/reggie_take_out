package com.snake.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.Exception.CategoryException;
import com.snake.pojo.Category;
import com.snake.pojo.Dish;
import com.snake.pojo.Setmeal;
import com.snake.service.CategoryService;
import com.snake.mapper.CategoryMapper;
import com.snake.service.DishService;
import com.snake.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
* @author admin
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-07-15 11:07:08
*/
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 重载父类的删除方法
     *删除分类和套餐分类
     * @param id 被删除的id
     */
    public boolean remove(Long id) {
        //1.判断该分类里面是否有关联的菜品
        long dishCount = dishService.count(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId,id));
        if (dishCount > 0) //存在关联菜品,删除失败
        {
           //抛出自定义异常
            throw new CategoryException("该分类下包含菜品,无法删除!");
        }
        // 2.判断该套餐分类里面是否有关联的套餐
        long setmealCount = setmealService.count(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId,id));
        if (setmealCount >0){ //套餐分类中有关联的套餐, 参数失败
            //抛出自定义异常
            throw new CategoryException("该套餐分类下包含套餐,无法删除!");
        }
        //3.没有关联菜品或者套餐, 执行删除
        return this.removeById(id);
    }
}




