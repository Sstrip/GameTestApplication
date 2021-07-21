package com.ss.gamesdk.bean;

import java.util.List;

/**
 * Copyright (C) 2019
 * SignDayBean
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class SignDayBean {
    /**
     * 每日的红包任务
     */
    private List<Task> redTaskList;
    /**
     * 状态  0为锁定状态 其他为正常状态
     */
    private int finish;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否被选中的状态标识
     */
    private boolean selected;



    public List<Task> getRedTaskList() {
        return redTaskList;
    }

    public void setRedTaskList(List<Task> redTaskList) {
        this.redTaskList = redTaskList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return redTaskList;
    }

    public void setTasks(List<Task> tasks) {
        this.redTaskList = tasks;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }
}
