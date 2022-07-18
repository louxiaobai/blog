package com.lou.blog.service.impl;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.ObjectMetadata;

import com.lou.blog.service.OssService;
import com.lou.blog.utils.CustomException;
import com.lou.blog.utils.MineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.aliyun.oss.internal.OSSConstants.URL_ENCODING;


@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private MineUtils mineUtils;

    /**
     * Endpoint以杭州为例，其它Region请按实际情况填写。
     */
    @Value("${alioss.endpoint}")
    private String endpoint;
    /**
     * 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
     */
    @Value("${alioss.accessKeyId}")
    private String accessKeyId;
    @Value("${alioss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${alioss.bucketName}")
    private String bucketName;

    /**
     * 上传文件到oss上
     *
     * @param file 需要上传的文件
     * @return 上传后的文件名
     */
    @Override
    public String upload(MultipartFile file,String dirPath) {
        String uploadUrl = "";
        try {
            // 创建ossClient实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 获取上传的文件流
            InputStream inputStream = file.getInputStream();


            // 获取上传文件的文件名
            String originalFilename = file.getOriginalFilename();

            // 获取图片的类型
            String contentType = mineUtils.getcontentType(originalFilename.substring(originalFilename.lastIndexOf(".")));
            //String contentType = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 获取随机名
            String randomUUID = UUID.randomUUID().toString();

            // 生成文件名 sjblog/2021/08/08/8666a555feda406e9e369bd121f1b781.jpg
            String fileName = dirPath + "/" + randomUUID.replace("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));

            // 如果想要实现图片预览的效果,一定要设置以下几点
            // 1.设置文件的ACL(权限)  要么是公共读,要么是公共读写
            // 2.一定要设置文本类型(image/jpg)
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置公共读权限
            objectMetadata.setObjectAcl(CannedAccessControlList.PublicRead);
            // 设置图片类型
            objectMetadata.setContentType(contentType);
            // 上传图片
            ossClient.putObject(bucketName, fileName, inputStream, objectMetadata);

            // 关闭OSSClient。
            ossClient.shutdown();
            // 默认十年不过期
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);

            // bucket名称 文件名 过期时间
            uploadUrl = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();

        } catch (IOException e) {
            throw new CustomException("上传文件失败");
        }

        return uploadUrl.substring(0, uploadUrl.indexOf("?"));
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return
     */
    @Override
    public boolean deleteFile(String fileName) {
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, fileName);

            // 关闭OSSClient。
            ossClient.shutdown();
            return true;
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
            throw new CustomException("删除文件失败");
        }
    }

    @Override
    public boolean deleteInBatches(List<String> fileNames) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileNames).withEncodingType(URL_ENCODING));
        // 关闭OSSClient。
        ossClient.shutdown();
        return true;
    }

}