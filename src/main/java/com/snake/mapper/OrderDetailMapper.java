package com.snake.mapper;

import com.snake.pojo.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author admin
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-07-17 13:19:05
* @Entity com.snake.pojo.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




