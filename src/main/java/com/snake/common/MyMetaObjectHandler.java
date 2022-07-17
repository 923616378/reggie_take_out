package com.snake.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 *  公共字段填充
 */
@Slf4j
@Component //加入到spring容器
public class MyMetaObjectHandler implements MetaObjectHandler {
    //自动注入session对象,虽说Spring容器的Bean可能是单例的, 但是这里直接注入不会混乱
    @Autowired
    private HttpSession httpSession;
    private Long id;
    /**
     * 公共字段插入时自动执行
     * @param metaObject 保存了实体对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("待填充对象,设置插入: "+metaObject.getOriginalObject().toString());
        if (httpSession.getAttribute("employee") != null)
        {
            id = (Long) httpSession.getAttribute("employee");
        }else if (httpSession.getAttribute("user") != null){
            id = (Long) httpSession.getAttribute("user");
        }else {
            log.info("出现异常: 用户未登录,但是有插入或修改请求");
            return;
        }
        //填充创建时间和创建人
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("createUser", id);
        //填充创建时间和创建人
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", id);
    }

    /**
     * 公共字段修改时自动执行
     * @param metaObject 保存了实体对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (httpSession.getAttribute("employee") != null)
        {
            id = (Long) httpSession.getAttribute("employee");
        }else if (httpSession.getAttribute("user") != null){
            id = (Long) httpSession.getAttribute("user");
        }else {
            log.info("出现异常: 用户未登录,但是有插入或修改请求");
            return;
        }
        log.info("待填充对象,设置修改: "+metaObject.getOriginalObject().toString());
        //填充创建时间和创建人
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", id);
    }
}
