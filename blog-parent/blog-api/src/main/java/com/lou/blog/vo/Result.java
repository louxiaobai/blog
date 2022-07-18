package com.lou.blog.vo;

/**
 * @author lqw
 * @date 2022/4/8-4:25 下午
 */

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class Result {

    private boolean success;

    private int code;

    private String msg;

    private Object data;

    /**
     * 成功得到数据的请求
     * @param data
     * @return
     */
    public static Result success(Object data){

        return new Result(true, 200, "success", data);

    }

    /**
     *  失败请求
     * @param code
     * @param msg
     * @return
     */
    public static Result fail(int code,String msg){

        return new Result(false, code, msg, null);


    }




}