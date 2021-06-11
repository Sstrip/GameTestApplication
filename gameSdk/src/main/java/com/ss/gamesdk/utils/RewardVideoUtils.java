package com.ss.gamesdk.utils;

import android.content.Context;
import android.util.Log;

import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAdListener;
import com.qq.e.comm.util.AdError;

import java.util.Map;

/**
 * Copyright (C) 2019
 * RewardVideoUtils
 * <p>
 * Description
 *
 * @author Administrator
 * @version 1.0
 * <p>
 * Ver 1.0, 2019/11/2, Administrator, Create file
 */
public class RewardVideoUtils {
    private static ExpressRewardVideoAD rewardVideoAD;
    /**
     * 已经展示过
     */
    private static boolean hasShow = false;

    private static OnVideoAdListener<ExpressRewardVideoAD> listener;

    /**
     * 设置视频广告的回调
     *
     * @param listener
     */
    public static void setOnVideoAdListener(OnVideoAdListener<ExpressRewardVideoAD> listener) {
        RewardVideoUtils.listener = listener;
    }


    public static void getRewardAd(final Context context, final String appID, String posID, boolean autoShow) {
        // 仅展示部分代码，完整代码请参考 Demo 工程
        // 1.加载广告，先设置加载上下文环境和条件
        //七书主体下 七书包名申请的appid和广告id
        if (null == rewardVideoAD) {
//            rewardVideoAD = new RewardVideoAD(context, "1109945546", "1070187975888407", new RewardVideoADListener() {
            rewardVideoAD = new ExpressRewardVideoAD(context, appID, posID, new ExpressRewardVideoAdListener() {

                @Override
                public void onAdLoaded() {

                }

                @Override
                public void onVideoCached() {
                    Log.e("打印videoCached", "********");
                    if (autoShow && !hasShow) {
                        hasShow = true;
                        show(context);
                    }
                    if (null != listener) {
                        listener.onVideoLoaded(rewardVideoAD);
                    }
                }

                @Override
                public void onShow() {

                }

                @Override
                public void onExpose() {

                }

                @Override
                public void onReward(Map<String, Object> map) {

                }

                @Override
                public void onClick() {
                    //点击下载
                }


                @Override
                public void onVideoComplete() {
                    //视频广告播放结束回调
                    if (null != listener) {
                        listener.onVideoFinish();
                    }
                }

                @Override
                public void onClose() {
                    //广告关闭
//                    rewardVideoAD.loadAD();
                    hasShow = false;
                    if (null != listener) {
                        listener.onVideoClose();
                    }
                }

                @Override
                public void onError(AdError adError) {
                    Log.e("打印广点通广告加载异常", adError.getErrorMsg() + "errorCode->" + adError.getErrorCode());
                }
            });
        }
        // 设置播放时静音状态
        rewardVideoAD.setVolumeOn(false);
        rewardVideoAD.loadAD();

    }

    public static void show(Context context) {
        // 展示广告
        // 在视频缓存成功后展示，以省去用户的等待时间，提升用户体验
        rewardVideoAD.showAD(null);
    }

    public interface OnVideoAdListener<T> {
        void onVideoFinish();

        void onVideoClose();

        void onVideoLoaded(T ad);
    }
}
