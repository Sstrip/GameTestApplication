package com.ss.gamesdk.bean;

import java.util.List;

/**
 * Copyright (C) 2019
 * GrowingWrapTask
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/7/4, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class GrowingWrapTask {
    private int interval;
    private List<Task> taskList;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
