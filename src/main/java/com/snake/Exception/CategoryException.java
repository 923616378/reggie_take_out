package com.snake.Exception;

/**
 * 分类业务自定义异常
 */
public class CategoryException extends RuntimeException{
    public CategoryException(String message){
        super(message);
    }
}
