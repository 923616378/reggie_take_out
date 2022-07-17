package com.snake.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snake.Exception.SetmealException;
import com.snake.common.R;
import com.snake.dto.SetmealDto;
import com.snake.pojo.Category;
import com.snake.pojo.Setmeal;
import com.snake.pojo.SetmealDish;
import com.snake.service.CategoryService;
import com.snake.service.SetmealDishService;
import com.snake.service.SetmealService;
import kotlin.jvm.internal.Lambda;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * 保存套餐
     * @param setmealDto 套餐信息对象
     * @return 返回是否成功
     */
    @PostMapping
    public R<String> save(@RequestBody  SetmealDto setmealDto){
        //1.输出套餐信息
        log.info("套餐信息:{}",setmealDto);
        //2.存储套餐信息
        boolean result = setmealService.saveSetmealWithDishs(setmealDto);
        //返回结果
        return result?R.success("保存套餐成功"):R.error("保存套餐失败");
    }

    /**
     * 套餐分页查询,根据name参数模糊查询
     * 1. 查询出来的套餐信息,只有对应的套餐分类的id,而前端还需要该套餐分类的名字,
     *      而Setmeal没有这个字段,所以我们需要封装到SetmealDto里面.
     * 2. 执行查询时,现将分页查询数据封装到Setmeal的page,之后只拷贝分页的一些参数到SetmealDto的page
     *      之后,遍历记录中Setmeal集合的每一个套餐,去查询对应套餐的名字,之后封装到SetmealDto里面,
     *      收集成一个SetmealDto集合, 最后再讲集合封装到SetmealDto的page里面
     */
    @GetMapping("/page")
    public R<Page> getPage(Integer page,Integer pageSize,String name){
        //1.创建Setmeal的分页对象
        Page<Setmeal> setMealpage = new Page<>(page,pageSize);
        //创建条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(name!=null,Setmeal::getName,name);
        //分页查询
        setmealService.page(setMealpage,lqw);
        //拷贝对象, 将SetMealPage拷贝到setMealDtoPage,忽略 records
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setMealpage,setmealDtoPage,"records");
        //遍历records
        List<Setmeal> records = setMealpage.getRecords();
        List<SetmealDto> collect = records.stream().map(setmeal -> {
            //创建SetmealDto
            SetmealDto setmealDto = new SetmealDto();
            //拷贝对象属性
            BeanUtils.copyProperties(setmeal, setmealDto);
            //查询每一个套餐对应套餐分类的名字
            Category category = categoryService.getById(setmeal.getCategoryId());
            //将套餐的名字封装到setmealDto里面
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        //将集合封装到records
        setmealDtoPage.setRecords(collect);
        //返回page
        return R.success(setmealDtoPage);
    }


    /**
     * 批量删除
     */
    @DeleteMapping
    public R<String> deleteByIds(@RequestParam List<Long> ids){
        log.info("待删除套餐id列表:{}",ids);
        //1.查询数据库是否有套餐在这些id中,并且是售卖状态
        //创建条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        //是否有套餐在这些id中,并且是售卖状态
        lqw.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        long count = setmealService.count(lqw);
        if (count > 0) //存在正在售卖的套餐,禁止删除
        {
            throw new SetmealException("存在正在售卖的套餐,禁止删除");
        }
        //2.先删除套餐
        setmealService.removeByIds(ids);
//        setmealService.removeByIds(Arrays.asList(ids));
        //3.删除套餐与菜品的管理信息
        //创建条件构造器
        LambdaQueryWrapper<SetmealDish> lqw2 = new LambdaQueryWrapper<>();
        //当菜品关联信息中,如果套餐id在ids里面就进行删除
        lqw2.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lqw2);
        return R.success("删除成功");
    }

    /**
     * 根据套餐分类id和状态查询对应的套餐
     */
    @GetMapping("/list")
    public R<List<Setmeal>> getSetmeals(Setmeal setmeal){
        log.info("查询的套餐分类和售卖状态:{}",setmeal);
        //1.创建条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lqw.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        //查询
        List<Setmeal> list = setmealService.list(lqw);
        return R.success(list);
    }
}
