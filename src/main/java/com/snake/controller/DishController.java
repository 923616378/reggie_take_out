package com.snake.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snake.common.R;
import com.snake.dto.DishDto;
import com.snake.pojo.Category;
import com.snake.pojo.Dish;
import com.snake.pojo.DishFlavor;
import com.snake.service.CategoryService;
import com.snake.service.DishFlavorService;
import com.snake.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;



    /**
     * 保存数据
     * @param dishDto 保存的数据
     * @return 返回是否保存成功提示
     *
     */
    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto){
        log.info("前端的菜品数据:{}",dishDto);
        boolean result = dishService.saveWithFlavors(dishDto);
        return result?R.success("保存成功"):R.error("保存失败");
    }

    /**
     * 分页查询菜品
     * 因为返回给前端的数据,需要CategoryName属性,而dish满足不了
     * 正好我们有一个DishDto继承了dish,并且里面有CategoryName属性,满足我们的需求
     */
    @GetMapping("/page")
    public R<Page<DishDto>> getPage(Integer page,Integer pageSize,String name)
    {
        //1.创建Dish的分页对象和DishDto对象,前者查询dish的属性数据,之后再封装到DishDto的分页对象
        Page<DishDto> dtoPage = new Page<>();
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        //2.创建条件构造器
        LambdaQueryWrapper<DishDto> lqw = new LambdaQueryWrapper<>();
        //菜品名字相等
        lqw.eq(StringUtils.isNotEmpty(name),Dish::getName,name);
        //根据修改时间降序查询
        lqw.orderByDesc(Dish::getUpdateTime);
        //3.查询dish表,将数据封装
        dishService.page(pageInfo);
        //4.将Dish分页对象的数据拷贝到DishDto的分页对象,忽略records属性
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        //5.查询的数据只有dish类的基础数据,再次查询数据库,根据菜品所属分类id获取菜品名字
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoRecords = records.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            //拷贝属性,使用Bean工具类.将dish的属性拷贝给dishDto
            BeanUtils.copyProperties(dish, dishDto);
            //查询category表
            Category category = categoryService.getById(dish.getCategoryId());
            //判断查询到的分类是否为null,即这个菜没有分类,如果没有就不设置
            if (category != null)
            {
                //将category里面的分类名,封装dishDto里面
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        //6. 将dishDtoRecords封装到dtoPage分页对象
        dtoPage.setRecords(dishDtoRecords);
        //7.返回最终的分页对象
        return R.success(dtoPage);
    }

    /**
     * 回显到修改菜品页面的数据
     * 查询菜品Dish和该菜品的品味,之后将菜品的集合和Dish封装到DishDto
     */
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable Long id){
      DishDto dishDto = dishService.getByIdWithFlavor(id);
        //返回结果
        return dishDto!=null?R.success(dishDto):R.error("查询不到该菜品!");
    }
    /**
     * 修改菜品
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        //输出菜品和口味信息
        log.info("菜品信息:{}",dishDto);
        //保存
        boolean result = dishService.updateDishWithFlavor(dishDto);
        return result? R.success("修改成功"):R.error("修改失败");
    }

    /**
     * 根据菜品的相关信息查询对应的菜品
     * @param dish 菜品分类
     * @return 返回菜品集合
     */
//    @GetMapping("/list")
//    public R<List<Dish>> getDishListByCategory(Dish dish){
//        log.info("根据菜品相关信息查询对应的菜品:{}",dish);
//        //1.创建条件构造器
//        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
//        // 菜品分类id等于 菜品里面的分类id
//        lqw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        //菜品状态必须是起售状态 status == 1
//        lqw.eq(Dish::getStatus,1);
//        //菜品按照sort值排序,相等时按照修改时间排序
//        lqw.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        //2.查询
//        List<Dish> list = dishService.list(lqw);
//        //返回结果
//        return R.success(list);
//
//    }

    /**
     * 查询菜品功能升级,支持前端查询菜品时扩展口味
     *  原来的dish满足不了,换成dishDto
     */
    @GetMapping("/list")
    public R<List<DishDto>> getDishListByCategory(Dish dish){
        log.info("根据菜品相关信息查询对应的菜品:{}",dish);
        //1.创建条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        // 菜品分类id等于 菜品里面的分类id
        lqw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //菜品状态必须是起售状态 status == 1
        lqw.eq(Dish::getStatus,1);
        //菜品按照sort值排序,相等时按照修改时间排序
        lqw.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //2.查询
        List<Dish> list = dishService.list(lqw);
        //封装dish里面的属性操作
        List<DishDto> collect = list.stream().map(dish1 -> {
            DishDto dishDto = new DishDto();
            //拷贝属性
            BeanUtils.copyProperties(dish1, dishDto);
            //创建条件构造器
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dish1.getId());
            //查询每个菜品的口味
            List<DishFlavor> list1 = dishFlavorService.list(lambdaQueryWrapper);
            //将口味集合添加到dishDto
            dishDto.setFlavors(list1);
            //返回结果
            return dishDto;
        }).collect(Collectors.toList());
        //返回结果
        return R.success(collect);

    }
}
