package com.ss.gamesdk.utils;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sigmob.windad.rewardedVideo.WindRewardedVideoAd;

/**
 * Copyright (C) 2019
 * AdUtils
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/3, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class AdUtils {
    private static AdUtils adUtils;
    /**
     * 上下文对象
     */
    private Context context;

    private AdUtils() {
    }

    /**
     * 单例
     *
     * @param context
     * @return
     */
    public static AdUtils getInstance(Context context) {
        if (adUtils == null) {
            adUtils = new AdUtils();
        }
        adUtils.context = context.getApplicationContext();
        return adUtils;
    }

    /**
     * 展示激励视频广告
     */
    public void loadRewardVideoAD(String appID, String adId, boolean autoShow) {
        //广点通激励视频广告
        RewardVideoUtils.getRewardAd(context, appID, adId, autoShow);
//        RewardVideoUtils.show(context);
    }

    /**
     * 添加激励视频回调
     *
     * @param appID
     * @param adId
     * @param listener
     */
    public void loadRewardVideoAD(String appID, String adId, RewardVideoUtils.OnVideoAdListener listener) {
        RewardVideoUtils.setOnVideoAdListener(listener);
        RewardVideoUtils.getRewardAd(context, appID, adId, false);
    }


    /**
     * 其他激励视频广告展示
     */
    public void loadOtherRewardVideoAD(Activity activity, String adId, boolean autoPlay) {
        OtherRewardVideoUtils.getInstance().loadRewardVideo(adId, activity, autoPlay);
//        OtherRewardVideoUtils.getInstance().showVideo(activity);
    }

    /**
     * 其他激励视频广告展示
     *
     * @param activity
     * @param adId
     * @param listener
     */
    public void loadOtherRewardVideoAD(Activity activity, String adId, RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd> listener) {
        OtherRewardVideoUtils.getInstance().setAwardListener(listener);
        OtherRewardVideoUtils.getInstance().loadRewardVideo(adId, activity, false);
    }

    /**
     * 展示其他激励视频广告
     *
     * @param activity
     */
    public void showOtherRewardVideoAd(Activity activity) {
        OtherRewardVideoUtils.getInstance().showVideo(activity);
    }


    /**
     * 加载插屏广告
     *
     * @param activity
     * @param adID
     */
    public void loadGdtInsertAd(Activity activity, String adID) {
        UnifiedInterstitialADUtil.getInstance().loadAd(activity, adID);
    }

    /**
     * 加载广点通平台模板广告
     *
     * @param context
     * @param adID
     * @param relativeLayout
     */
    public void loadGdtNativeExpressAd(Context context,int width, String adID, RelativeLayout relativeLayout) {
        NativeExpressUtil.getInstance().loadNativeExpressAd(context,width, adID, relativeLayout);
    }

    /**
     * 加载banner广告
     *
     * @param activity
     * @param adID
     * @param viewGroup
     */
    public void loadBannerAd(Activity activity, String adID, ViewGroup viewGroup) {
        BannerAdUtil.getInstance().loadBannerAd(activity, adID, viewGroup);
    }

}
