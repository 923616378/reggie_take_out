package com.snake.ExceptionHandler;

import com.snake.Exception.CategoryException;
import com.snake.Exception.SetmealException;
import com.snake.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
//异常处理类, 只有包含RestController和Controller注解的类,出现了异常就,就匹配这个类的异常处理方法
@Slf4j
@RestControllerAdvice(annotations ={RestController.class, Controller.class})
public class SqlExceptionHandler {
    /**
     * 处理sql,唯一索引异常
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        //判断错误信息中的包含信息,分辨具体错误信息
        String message = exception.getMessage();
        if (message.contains("Duplicate entry")) //重复条目
        {
            //错误信息以空格分割,转换为数组
            String[] s = message.split(" ");
            //第三个字符串即为重复的字段
            log.error(s[2]+" 唯一字段重复");
            return R.error(s[2]+"已重复");
        }
        return R.error("未知错误");
    }

    /**
     * 处理Category业务的异常
     */
    @ExceptionHandler(CategoryException.class)
    public R<String> categoryExceptionHandler(CategoryException exception){
        //获取错误信息
        String message = exception.getMessage();
        //返回错误信息
        return R.error(message);
    }
    /**
     * 处理setmeal业务的异常
     */
    @ExceptionHandler(SetmealException.class)
    public R<String> categoryExceptionHandler(SetmealException exception){
        //获取错误信息
        String message = exception.getMessage();
        //返回错误信息
        return R.error(message);
    }
}
