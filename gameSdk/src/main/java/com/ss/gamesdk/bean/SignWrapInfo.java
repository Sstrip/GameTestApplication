package com.ss.gamesdk.bean;

import java.util.List;

/**
 * Copyright (C) 2019
 * SignWrapInfo
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class SignWrapInfo {

    private List<SignDayBean> signTaskList;
    /**
     * 总的金币
     */
    private long coinTotal;


    public long getCoinTotal() {
        return coinTotal;
    }

    public void setCoinTotal(long coinTotal) {
        this.coinTotal = coinTotal;
    }

    public List<SignDayBean> getSignTaskList() {
        return signTaskList;
    }

    public void setSignTaskList(List<SignDayBean> signTaskList) {
        this.signTaskList = signTaskList;
    }
}
