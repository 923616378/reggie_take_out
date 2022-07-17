package com.snake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.pojo.OrderDetail;
import com.snake.service.OrderDetailService;
import com.snake.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-07-17 13:19:05
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




