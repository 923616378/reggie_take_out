package com.snake.controller;


import com.snake.common.R;
import com.snake.pojo.*;
import com.snake.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;


@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    /**
     * 提交订单
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders, HttpSession session) {
        //获取id
        Long id = (Long)session.getAttribute("user");
        orderService.saveOrder(orders,id);
        return R.success("下单成功");
    }
}