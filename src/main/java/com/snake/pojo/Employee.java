package com.snake.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    //插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //插入和修改时自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    //插入和修改时自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
