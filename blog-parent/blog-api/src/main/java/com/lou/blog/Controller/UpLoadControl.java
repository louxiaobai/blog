package com.lou.blog.Controller;

import cn.hutool.core.map.MapUtil;
import com.lou.blog.service.OssService;
import com.lou.blog.utils.QiniuUtils;
import com.lou.blog.vo.Result;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

/**
 * @author lqw
 * @date 2022/4/17-12:11 上午
 */

@RestController
@RequestMapping("upload")
public class UpLoadControl {


    @Autowired
    private OssService ossService;


    /**
     * 写文章的时候上传图片
     * @param file
     * @return
     */
//    @PostMapping
//    public Result upload(@RequestParam("image") MultipartFile file){
//
//        //原始文件名称
//        String originalFilename = file.getOriginalFilename();
//
//        //唯一的文件名称
//        String fileName =  UUID.randomUUID().toString()+"."+ StringUtils.substringAfterLast(originalFilename, ".");
//
//        //上传文件上传到那里呢？　七牛云　云服务器
//        //降低我们自身应用服务器的带宽消耗
//
//        boolean upload = qiniuUtils.upload(file, fileName);
//        if (upload) {
//
//            System.out.println(QiniuUtils.url+fileName);
//            return Result.success(QiniuUtils.url+fileName);
//        }
//        return Result.fail(20001,"上传失败");
//
//    }
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){


        //上传文件上传到那里呢？　阿里云oos　云服务器
        //降低我们自身应用服务器的带宽消耗

        String  dirPath="image";

        String url=ossService.upload(file, dirPath);


        Map<Object,Object> url1= MapUtil.builder().put("url",url).build();

        System.out.println(url1.get("url"));

        return Result.success(url1.get("url"));



//        if (upload) {
//
//            System.out.println(QiniuUtils.url+fileName);
//            return Result.success(QiniuUtils.url+fileName);
//        }
//        return Result.fail(20001,"上传失败");

    }

}