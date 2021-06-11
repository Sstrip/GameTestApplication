package com.ss.gamesdk.bean;

/**
 * Copyright (C) 2019
 * ApiResultData
 * <p>
 * Description
 *
 * @author Administrator
 * @version 1.0
 * <p>
 * Ver 1.0, 2019/10/27, Administrator, Create file
 */
public class ApiResultData<T> {
    /**
     * 信息提示
     */
    public String msg;
    /**
     * 状态码
     */
    public int status;
    /**
     * 数据
     */
    public T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
