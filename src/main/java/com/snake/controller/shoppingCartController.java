package com.snake.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.snake.common.R;
import com.snake.pojo.ShoppingCart;
import com.snake.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class shoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 用户添加菜品或者套餐到购物车表,
     *  购物车表 是 用户表和菜品表的中间关系表, 一个菜品对应对应多个购物车表项, 多个购物车对应一个用户表项
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        log.info("购物车表项信息:{}",shoppingCart);
        //1.查询shopping表,查看该套餐或者菜品是否已经被该用户选购
        //通过session获取当前移动端登录用户id
        Long id = (Long)session.getAttribute("user");
        //设置用户id
        shoppingCart.setUserId(id);
        //创建条件构造器
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(shoppingCart.getUserId()!=null,ShoppingCart::getUserId,shoppingCart.getUserId());
        //判断用户添加的是菜品还是套餐
        if (shoppingCart.getDishId() != null){
            //动态构造条件
            lqw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            //动态构造条件
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //查询数据库
        ShoppingCart one = shoppingCartService.getOne(lqw);
        //判断用户是否点过了该菜品或者套餐,如果one为null,表明用户没有点过
        if (one == null)
        {
            //添加这个菜品
            one = shoppingCart;
            //设置菜品个数为1
            one.setNumber(1);
            shoppingCartService.save(one);
        }else{
            //用户点过该菜品, 忽略用户这次点击菜品的口味,将原数据的number加1,即修改数据
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
        }
        //返回最新的购物车数据
        return R.success(one);
    }
    /**
     * 菜品数量减一
     */
    @PostMapping("/sub")
    public R<ShoppingCart>sub(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        //根据菜品id和用户id寻找对应的购物车表项,将数量修改为1
        //查询
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        Long id = (Long)session.getAttribute("user");
        lqw.eq(ShoppingCart::getUserId,id);
        lqw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        ShoppingCart one = shoppingCartService.getOne(lqw);
        //如果查询到1了,将数量减一,如果数量为0就执行删除
        if (one!=null && one.getNumber() == 1)
        {
            //删除
            shoppingCartService.removeById(one.getId());
        } else if (one!=null && one.getNumber()>1) {
            //修改数量减一
            one.setNumber(one.getNumber()-1);
            shoppingCartService.updateById(one);
        }else{
            return R.error("异常错误:菜品不在购物车中!");
        }
        return R.success(one);
    }
    /**
     * 获取list
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getList(HttpSession httpSession){
        //创建条件构造器
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        Long id = (Long)httpSession.getAttribute("user");
        lqw.eq(id!=null,ShoppingCart::getUserId,id);
        //降序通过数量
        lqw.orderByDesc(ShoppingCart::getNumber);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);
    }
    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public R<String> delete(HttpSession httpSession){
        //创建条件构造器
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        Long id = (Long)httpSession.getAttribute("user");
        lqw.eq(ShoppingCart::getUserId,id);
        //删除
        shoppingCartService.remove(lqw);
        return R.success("清空成功!");
    }
}
