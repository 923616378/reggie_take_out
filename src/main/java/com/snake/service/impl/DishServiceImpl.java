package com.snake.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.common.R;
import com.snake.dto.DishDto;
import com.snake.pojo.Dish;
import com.snake.pojo.DishFlavor;
import com.snake.service.DishFlavorService;
import com.snake.service.DishService;
import com.snake.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author admin
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2022-07-15 12:08:38
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional //开启事务,因为有多个写操作
    public boolean saveWithFlavors(DishDto dishDto) {
        //1.保存菜品的数据,保存后,会自动将生成的id封装回 disDto的id里
        boolean save1 = this.save(dishDto);
        //2. 获取生成的id
        Long id = dishDto.getId();
        //3. 将id封装到口味里面
        //采用流的方式
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(id);
            return dishFlavor;
        }).collect(Collectors.toList());
        //2.保存菜品口味数据
        dishFlavorService.saveBatch(flavors);
        return save1;
    }

    /**
     * 查询菜品和它的口味
     * @param id 菜品id
     * @return 封装后的DishDto
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //1. 先根据菜品id,查询菜品
        Dish dish = this.getById(id);
        //判断dish是否为null
        if (dish == null)
        {
            return null;
        }
        //2.查询菜品的口味
        //创建条件构造器
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(lqw);
        //创建DishDto进行封装
        DishDto dishDto = new DishDto();
        //使用BeanUtils工具类封装
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }
    /**
     * 修改菜品数据,但是里面的口味我们进行先删除,再增加
     * 因为新增加的口味里面没有封装 菜品的id,所以我们要遍历口味数组,设置每个口味的菜品id
     */
    @PutMapping
    @Transactional //开启事务
    public boolean updateDishWithFlavor(DishDto dishDto){
        //1.修改dishDto里面dish信息
        boolean result = this.updateById(dishDto);
        //2.根据菜品id删除原来的口味
        //创建条件构造器
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lqw);
        //3.设置口味的菜品id为当前菜品
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        //3.添加新的口味
        dishFlavorService.saveBatch(flavors);
        return result;
    }
}




