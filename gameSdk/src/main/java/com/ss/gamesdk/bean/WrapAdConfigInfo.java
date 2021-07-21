package com.ss.gamesdk.bean;

import java.util.List;

/**
 * Copyright (C) 2019
 * WrapAdConfigInfo
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/29, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class WrapAdConfigInfo {
    /**
     * 位置
     */
    private int location;
    /**
     * 广告列表
     */
    private List<AdConfigInfo> adList;
    /**
     * 当前的位置索引
     */
    private int currentPosition;

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public List<AdConfigInfo> getAdList() {
        return adList;
    }

    public void setAdList(List<AdConfigInfo> adList) {
        this.adList = adList;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
