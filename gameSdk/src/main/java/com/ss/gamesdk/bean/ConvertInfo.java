package com.ss.gamesdk.bean;

import java.util.List;

/**
 * Copyright (C) 2019
 * ConvertInfo
 * <p>
 * Description
 * 兑换数据实体
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class ConvertInfo {
    /**
     * 提示
     */
    private String titleDown;
    /**
     * 弹窗标题
     */
    private String title;

    private List<ConvertListInfo> exchangeList;

    public String getTitleDown() {
        return titleDown;
    }

    public void setTitleDown(String titleDown) {
        this.titleDown = titleDown;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ConvertListInfo> getExchangeList() {
        return exchangeList;
    }

    public void setExchangeList(List<ConvertListInfo> exchangeList) {
        this.exchangeList = exchangeList;
    }
}
