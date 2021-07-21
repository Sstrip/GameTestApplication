package com.ss.gamesdk.bean;

/**
 * Copyright (C) 2019
 * Task
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/1, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class Task {
    /**
     * 头像
     */
    private String avatar;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 描述
     */
    private String discription;

    /**
     *
     */
    private String endVersion;
    /**
     * 完成状态 0未完成1完成未领取2已领取
     */
    private String finish;
    private int limitNum;
    private int operationTime;
    private int rewardMax;
    private int rewardMin;
    private int showIndex;
    private int startVersion;
    private int status;
    private int taskCode;
    private String taskName;
    private int type;
    private String updateTime;
    private String url;
    /**
     * 广告数据相关配置
     */
    private AdConfigInfo adConfig;

    /**
     * 红包提示语
     */
    private String taskMsg;

    public String getTaskMsg() {
        return taskMsg;
    }

    public void setTaskMsg(String taskMsg) {
        this.taskMsg = taskMsg;
    }

    public AdConfigInfo getAdConfig() {
        return adConfig;
    }

    public void setAdConfig(AdConfigInfo adConfig) {
        this.adConfig = adConfig;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getEndVersion() {
        return endVersion;
    }

    public void setEndVersion(String endVersion) {
        this.endVersion = endVersion;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(int operationTime) {
        this.operationTime = operationTime;
    }

    public int getRewardMax() {
        return rewardMax;
    }

    public void setRewardMax(int rewardMax) {
        this.rewardMax = rewardMax;
    }

    public int getRewardMin() {
        return rewardMin;
    }

    public void setRewardMin(int rewardMin) {
        this.rewardMin = rewardMin;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public int getStartVersion() {
        return startVersion;
    }

    public void setStartVersion(int startVersion) {
        this.startVersion = startVersion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(int taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Task{" +
                "avatar='" + avatar + '\'' +
                ", createTime='" + createTime + '\'' +
                ", discription='" + discription + '\'' +
                ", endVersion='" + endVersion + '\'' +
                ", finish='" + finish + '\'' +
                ", limitNum=" + limitNum +
                ", operationTime=" + operationTime +
                ", rewardMax=" + rewardMax +
                ", rewardMin=" + rewardMin +
                ", showIndex=" + showIndex +
                ", startVersion=" + startVersion +
                ", status=" + status +
                ", taskCode=" + taskCode +
                ", taskName='" + taskName + '\'' +
                ", type=" + type +
                ", updateTime='" + updateTime + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
