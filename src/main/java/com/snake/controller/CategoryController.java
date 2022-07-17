package com.snake.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snake.common.R;
import com.snake.pojo.Category;
import com.snake.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

     //Spring现在不推荐使用字段直接注入, 目前处于学习阶段,暂时不考虑这个
    @Autowired
    private CategoryService categoryService;

    /**
     * 这个方法用于添加分类和套餐,因为这两个靠type区分,存在一张表中
     * @param category 实体
     * @return 添加是否成功信息
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        log.info("添加分类或者套餐: {}",category);
        //添加
        boolean save = categoryService.save(category);
        return save ? R.success("添加成功"):R.error("添加失败");
    }

    /**
     * 分页查询
     * @param page 当前页
     * @param pageSize 每页大小
     * @return 返回分页查询结果
     */
    @GetMapping("/page")
    public R<Page<Category>> getPage(Integer page, Integer pageSize){
        log.info("菜品分类分页查询参数: 当前页:{},每页大小:{}",page,pageSize);
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //创建条件构造器, 设置升序查询条件
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<Category>();
        lqw.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageInfo,lqw);
        return R.success(pageInfo);
    }

    /**
     * 删除分类或套餐
     * @param ids 删除的id
     * @return 是否删除成功的字符串提示
     */
    @DeleteMapping
    public R<String> removeCategory(Long ids){
        log.info("删除分类的id:{}",ids);
        boolean result = categoryService.remove(ids);
        return result ? R.success("删除成功"):R.error("删除失败");
    }

    /**
     * 分类修改
     */
    @PutMapping
    public R<String> Update(@RequestBody Category category){
        log.info("修改分类信息: {}",category);
        boolean result = categoryService.updateById(category);
        return result ? R.success("修改成功"):R.error("修改失败");
    }

    /**
     * 条件查询分类集合
     */
    @GetMapping("/list")
    public R<List<Category>> getList(Category category){
        log.info("条件查询分类:{}",category);
        //1.创建条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        //比较type
        lqw.eq(category.getType()!=null,Category::getType,category.getType());
        //降序条件, 如果sort相等,就继续比较更新时间
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //2.查询
        List<Category> list = categoryService.list(lqw);
        //返回结果
        return R.success(list);
    }
}
