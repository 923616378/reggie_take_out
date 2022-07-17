package com.snake.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.snake.common.R;
import com.snake.pojo.User;
import com.snake.service.UserService;
import com.snake.utils.SMSUtils;
import com.snake.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param user 封装了用户的手机号
     * @param request 获取session
     * @return 返回信息
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request){
        //1.获取用户手机号
        String phone = user.getPhone();
        log.info("用户手机号,{}",phone);
        //2.判断是否为null
        if (StringUtils.isNotEmpty(phone)){
            //生成四位验证码,阿里云测试模板支持4位纯数字
            String code = ValidateCodeUtils.generateValidateCode(4);
            log.info("生成验证码是:{}",code);
            //调用阿里云验证码
//            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code.toString());
            //将验证码存入session
            request.getSession().setAttribute("code",code);
        }
        //返回成功提示
        return R.success("验证码已经发送!");
    }
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,String> map, HttpSession httpSession){
            log.info("用户登录数据:{}",map);
        //获取手机号和验证码
        String phone = map.get("phone");
        String code = map.get("code");
        //判断手机号是否为空
        if (!StringUtils.isNotEmpty(phone)){
            return R.error("手机号不正确!");
        }
        //1.检测用户的验证码,与系统是否匹配
        if(!httpSession.getAttribute("code").equals(code)){ //如果用户输入验证码不等于实际验证码
             return R.error("验证码不正确!");
        }
        //2.验证码正确,检测用户数据是否已经在user表中,有的话说明已经注册
        //条件构造器
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhone,phone);
        User user = userService.getOne(lqw);
        if (user == null){ //用户不存在,注册账号
            user = new User();
            user.setPhone(phone); //其他属性暂时不设置,id由数据库生成
            userService.save(user);
        }
        //将用户保存到域中
        httpSession.setAttribute("user",user.getId());
        //用户存在,查询数据库用户数据,返回给前端
        return R.success(user);
    }
}
