package com.lou.blog.vo.params;
/**
 * @author lqw
 * @date 2022/4/8-4:22 下午
 */
import lombok.Data;
@Data
public class PageParams {

    private int page=1;

    private int pageSize=3;

    private Long categoryId;

    private Long tagId;

    private String year;

    private String month;

    //传递6的话变成06
    public String getMonth(){
        if (this.month != null && this.month.length() == 1){
            return "0"+this.month;
        }
        return this.month;
    }
}