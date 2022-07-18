package com.lou.blog.utils;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MineUtils {

    /**
     * 合并多个集合
     *
     * @param lists
     * @return
     */
    public List concatList(List... lists) {
        List<Object> list = new ArrayList<>();
        for (List<Object> tempList : lists) {
            for (Object temp : tempList) {
                list.add(temp);
            }
        }
        return list;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".tif") ||
                FilenameExtension.equalsIgnoreCase(".tiff")) {
            return "image/tiff";
        }
        if (FilenameExtension.equalsIgnoreCase(".glb"))
                  {
            return "glb";
        }
        throw new CustomException("文件格式错误，请上传.jpg,.jpeg,.png,.gif,.tiff,.glb类型的图片文件");
    }
}