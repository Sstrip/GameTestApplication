package com.ss.gamesdk.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.sigmob.windad.WindAdError;
import com.sigmob.windad.rewardedVideo.WindRewardAdRequest;
import com.sigmob.windad.rewardedVideo.WindRewardInfo;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAd;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAdListener;

/**
 * Copyright (C) 2019
 * OtherRewardVideoUtils
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/3, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class OtherRewardVideoUtils {

    private static OtherRewardVideoUtils utils;
    private WindRewardAdRequest request;
    /**
     * 是否自动播放
     */
    private boolean autoPlay;
    /**
     * 广告回调
     */
    private RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd> listener;

    public static OtherRewardVideoUtils getInstance() {
        if (utils == null) {
            utils = new OtherRewardVideoUtils();
        }
        return utils;
    }

    private void initAd(Activity activity) {
        WindRewardedVideoAd windRewardedVideoAd = WindRewardedVideoAd.sharedInstance();

        windRewardedVideoAd.setWindRewardedVideoAdListener(new WindRewardedVideoAdListener() {

            //仅sigmob渠道有回调，聚合其他平台无次回调
            @Override
            public void onVideoAdPreLoadSuccess(String placementId) {
                if (autoPlay) {
                    showVideo(activity);
                }
                if (null != listener) {
                    listener.onVideoLoaded(windRewardedVideoAd);
                }
            }

            //仅sigmob渠道有回调，聚合其他平台无次回调
            @Override
            public void onVideoAdPreLoadFail(String placementId) {

            }

            @Override
            public void onVideoAdLoadSuccess(String placementId) {

            }

            @Override
            public void onVideoAdPlayStart(String placementId) {

            }

            @Override
            public void onVideoAdPlayEnd(String s) {
                if (null != listener) {
                    listener.onVideoFinish();
                }
            }

            @Override
            public void onVideoAdClicked(String placementId) {

            }

            //WindRewardInfo中isComplete方法返回是否完整播放
            @Override
            public void onVideoAdClosed(WindRewardInfo windRewardInfo, String placementId) {
                if (null != listener) {
                    listener.onVideoClose();
                }
            }

            /**
             * 加载广告错误回调
             * WindAdError 激励视频错误内容
             * placementId 广告位
             */
            @Override
            public void onVideoAdLoadError(WindAdError windAdError, String placementId) {
                Log.e("打印sigmob激励视频错误", windAdError.getMessage() + "****" + windAdError.getErrorCode());
            }


            /**
             * 播放错误回调
             * WindAdError 激励视频错误内容
             * placementId 广告位
             */
            @Override
            public void onVideoAdPlayError(WindAdError windAdError, String placementId) {

            }
        });
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setAwardListener(RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd> listener) {
        this.listener = listener;
    }


    public void loadRewardVideo(String adID, Activity activity, boolean autoPlay) {
        initAd(activity);
        this.autoPlay = autoPlay;
        WindRewardedVideoAd windRewardedVideoAd = WindRewardedVideoAd.sharedInstance();

        //placementId 必填,USER_ID,OPTIONS可不填，

//        request = new WindRewardAdRequest("placementId", "", null);
        request = new WindRewardAdRequest(adID, "", null);
        windRewardedVideoAd.loadAd(activity, request);
    }

    public void showVideo(Activity activity) {
        WindRewardedVideoAd windRewardedVideoAd = WindRewardedVideoAd.sharedInstance();
        try {
            //检查广告是否准备完毕
            if (windRewardedVideoAd.isReady(request.getPlacementId())) {
                //广告播放
                windRewardedVideoAd.show(activity, request);
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
