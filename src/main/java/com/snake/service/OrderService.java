package com.snake.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.pojo.Orders;

public interface OrderService extends IService<Orders> {
    void saveOrder(Orders orders, Long id);
}
