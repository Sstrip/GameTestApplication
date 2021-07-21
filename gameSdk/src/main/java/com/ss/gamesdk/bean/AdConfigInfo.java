package com.ss.gamesdk.bean;

/**
 * Copyright (C) 2019
 * AdConfigInfo
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/5, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class AdConfigInfo {
    /**
     * 广告位置
     */
    public static final int LOCATION_REWARD_VIDEO = 1;
    /**
     * banner广告位置
     */
    public static final int LOCATION_BANNER = 2;
    /**
     * 位置3默认为红包激励视频广告
     */
    public static final int LOCATION_RED_BAG = 3;
    /**
     * 两分钟的插屏广告
     */
    public static final int LOCATION_INSERT_AD = 9;
    /**
     * 常驻红包广告
     */
    public static final int LOCATION_PERMANENT_RED_PACKAGE = 10;
    /**
     * 红包领取结束弹窗广告
     */
    public static final int LOCATION_GET_RED_PACKAGE = 12;
    /**
     * 广告平台sgm
     */
    public static final String PLAT_SGM = "1";
    /**
     * 广点通广告平台
     */
    public static final String PLAT_GDT = "2";


    private String adContent;
    private String appChannel;
    private String appVersion;
    private String bundleid;
    /**
     * 1为 SGM广告 2为广点通平台
     */
    private String channel;
    private boolean isShow;
    private int location;
    private String shieldAdTime;
    private AdPlatformInfo adPlatformInfo;

    public String getAdContent() {
        return adContent;
    }

    public void setAdContent(String adContent) {
        this.adContent = adContent;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBundleid() {
        return bundleid;
    }

    public void setBundleid(String bundleid) {
        this.bundleid = bundleid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getShieldAdTime() {
        return shieldAdTime;
    }

    public void setShieldAdTime(String shieldAdTime) {
        this.shieldAdTime = shieldAdTime;
    }

    public AdPlatformInfo getAdPlatformInfo() {
        return adPlatformInfo;
    }

    public void setAdPlatformInfo(AdPlatformInfo adPlatformInfo) {
        this.adPlatformInfo = adPlatformInfo;
    }

    @Override
    public String toString() {
        return "AdConfigInfo{" +
                "adContent='" + adContent + '\'' +
                ", appChannel='" + appChannel + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", bundleid='" + bundleid + '\'' +
                ", channel='" + channel + '\'' +
                ", isShow=" + isShow +
                ", location=" + location +
                ", shieldAdTime='" + shieldAdTime + '\'' +
                ", adPlatformInfo=" + adPlatformInfo +
                '}';
    }
}
