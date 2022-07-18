package com.lou.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaoqi
 * Date: 2021-08-08
 * Time: 下午 16:30
 * Description: 操作oss接口
 *
 * @author xiaoqi
 * @since 2021-08-08
 */

public interface OssService {

    /**
     * 上传文件到oss上
     * @param file 需要上传的文件
     * @return 上传后的文件名
     */
    String upload(MultipartFile file,String dirPath);

    /**
     * 删除文件
     * @param fileName 文件名
     * @return
     */
    boolean deleteFile(String fileName);

    /**
     * 批量删除文件
     * @param fileNames 文件名
     * @return
     */
    boolean deleteInBatches(List<String> fileNames);

}
