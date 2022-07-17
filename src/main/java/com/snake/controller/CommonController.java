package com.snake.controller;

import com.snake.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Http2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    //注入文件保存路径
    @Value("${reggie.basePath}")
    private String basePath;
    /**
     * 上传图片
     * @param file
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //临时存储路径C:\Users\admin\AppData\Local\Temp\tomcat.80.4497724367913734924\work\Tomcat\localhost\ROOT
        //本次请求完毕后就会被删除
        log.info("接受到上传文件");
        //1. 随机生成一个UUID的文件名
        String fileName =UUID.randomUUID().toString();
        //2.截取上传文件原生名的后缀
        String originalFilename = file.getOriginalFilename();
        String substring = null;
        //如果不为空
        if(StringUtils.isNotEmpty(originalFilename)){
            substring = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }else{
            substring = ".jpg";
        }
        //3.组合成新文件名
        fileName = fileName + substring;
        //4.转存到对应的位置
        File dir = new File(basePath);
        if (!dir.exists()){ //如果不存在创建目录
            dir.mkdirs();
        }
        log.info("文件保存路径:{}",basePath+fileName);
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        //返回 上传的文件名
        return R.success(fileName);
    }
    /**
     * 下载文件
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //设置响应头中,返回数据的类型为图片
            response.setContentType("image/jpeg");
            //使用IOUtils的copy方法
            IOUtils.copy(Files.newInputStream(Paths.get(basePath + name)),response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
