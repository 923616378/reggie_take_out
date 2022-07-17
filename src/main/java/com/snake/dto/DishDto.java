package com.snake.dto;

import com.snake.pojo.Dish;
import com.snake.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 *  dto : 数据传输模型
 *  这个类是为了解决前端传来的数据复杂的问题
 *  继承Dish类,然后再加上一些其他的属性,才能接受完前端发送的数据
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
