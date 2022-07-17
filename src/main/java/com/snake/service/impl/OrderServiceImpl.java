package com.snake.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.Exception.SetmealException;
import com.snake.mapper.OrderMapper;
import com.snake.pojo.*;
import com.snake.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Override
    @Transactional
    public void saveOrder(Orders orders, Long id) {
        //2.查询当前用户的购物车数据
        //创建条件表达式,条件是 用户id
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,id);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        //判断购物车是否含有菜品
        if (list == null || list.size() == 0)
        {
            //抛出异常,懒得自定义异常了,直接拿过来用一个
            throw new SetmealException("异常操作:购物车没有菜品,无法下单!");
        }
        //查询用户的数据
        User user = userService.getById(id);
        //查询用户的下单id,是否在数据库中存在
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null)
        {
            //用户地址不存在不允许下单
            //抛出异常,懒得自定义异常了,直接拿过来用一个
            throw new SetmealException("异常操作:用户地址不存在不允许下单!");
        }
        //idWorker工具类生成id
        long orderId = IdWorker.getId();//订单号
        //原子整数类,在多线程下,保证数据安全,计算金额正确
        AtomicInteger amount = new AtomicInteger(0);
        //计算购物车总金额的同时, 生成订单明细数据集合
        List<OrderDetail> orderDetails = list.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            //addAndGet是相加, 单价*数量
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        //封装订单杂乱的数据
        orders.setId(orderId); //设置订单id
        orders.setOrderTime(LocalDateTime.now()); //订单时间
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2); //设置状态, 派送中
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(id); //下单用户id
        orders.setNumber(String.valueOf(orderId)); //订单号
        orders.setUserName(user.getName()); //用户名
        orders.setConsignee(addressBook.getConsignee()); //收货人
        orders.setPhone(addressBook.getPhone()); //手机号
        //拼接收货地址, 省 城市 区级名称 详细 地址
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //3.向订单表插入数据,一条数据
        this.save(orders);
        //4.向订单明细表插入多条数据
        orderDetailService.saveBatch(orderDetails);
        //5.清空购物车,根据用户id
        shoppingCartService.remove(lqw);
    }
}
