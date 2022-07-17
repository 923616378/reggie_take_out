package com.snake.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.snake.common.R;
import org.springframework.util.AntPathMatcher;

@Slf4j //设置日志
//拦截路径为所有
@WebFilter(urlPatterns = "/*",filterName = "LoginCheckFilter")
public class LoginCheckFilter implements Filter {
    //放行的url
    //  '/**' 包含该目录下的子文件的文件,'/*'只包含该目录下的文件
    String[] passUrls = {
        "/employee/login", //登录请求放行
        "/employee/logout", //注销请求放行
         "/user/sendMsg", //移动端发送验证码请求放行
            "/user/login", //移动端请求放行
        "/backend/**", //backend路径下的所有资源都放行.
        "/front/**",   //front路径下的所有资源都放行
    };
    //路径匹配器
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //强转为HttpServletRequest,使用里面的方法
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        //1.获取访问uri
        String requestURI = httpServletRequest.getRequestURI();
        //{}是占位符,后面的参数被输出到里面
        log.info("拦截资源路径: {}",requestURI);
        //2.判断是否在放行uri中
        if (isPass(requestURI)){
            log.info("请求url无需拦截");
            //放行资源
            chain.doFilter(request,response);
            return;
        }
        //3. 判断后台员工是否登录
        if (httpServletRequest.getSession().getAttribute("employee") != null) //登录放行
        {
            log.info("后台用户已登录,用户id为{}",httpServletRequest.getSession().getAttribute("employee"));
            //放行资源
            chain.doFilter(request,response);
            return;
        }
        //4.判断前台用户是否登录
        if (httpServletRequest.getSession().getAttribute("user") != null) //登录放行
        {
            log.info("前端用户已登录,用户id为{}",httpServletRequest.getSession().getAttribute("user"));
            //放行资源
            chain.doFilter(request,response);
            return;
        }
        //5.前后端都未登录,且不在放行请求uri中,获取输出流,发送R对象 NOTLOGIN
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 判断该请求是否在,放行url中
     * @param url 请求的url
     * @return 是否放行
     */
    public Boolean isPass(String url){
        boolean match = false;
        for (String passUrl : passUrls) {
            if (antPathMatcher.match(passUrl, url))
            {
                match = true;
                break;
            }
        }
        return match;
    }
}
