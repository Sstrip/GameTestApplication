package com.ss.gamesdk.bean;

/**
 * Copyright (C) 2019
 * ConvertListInfo
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class ConvertListInfo {
    /**
     * 展示的内容
     */
    private String showMsg;
    /**
     * 顺序
     */
    private int sort;
    /**
     * 状态 1为选中状态 0为未选中状态
     */
    private int status;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShowMsg() {
        return showMsg;
    }

    public void setShowMsg(String showMsg) {
        this.showMsg = showMsg;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
