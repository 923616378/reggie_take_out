package com.snake.service;

import com.snake.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-07-15 11:07:08
*/
public interface CategoryService extends IService<Category> {
    boolean remove(Long id);
}
