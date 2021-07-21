package com.ss.gamesdk.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

/**
 * Copyright (C) 2019
 * UnifiedInterstitialADUtil
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/5, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 插屏广告工具
 */
public class UnifiedInterstitialADUtil {
    private static UnifiedInterstitialADUtil util;
    /**
     * 广告实体
     */
    private UnifiedInterstitialAD ad;


    public static UnifiedInterstitialADUtil getInstance() {
        if (util == null) {
            util = new UnifiedInterstitialADUtil();
        }
        return util;
    }

    private void setVideoOption() {
        ad.setMinVideoDuration(1);
        ad.setMaxVideoDuration(30);
    }

    /**
     * 加载广告
     *
     * @param activity
     * @param adID
     */
    public void loadAd(Activity activity, String adID) {
        if (ad != null) {
            ad.destroy();
        }
        ad = new UnifiedInterstitialAD(activity, adID, new UnifiedInterstitialADListener() {
            @Override
            public void onADReceive() {
                Log.e("打印广点通广告加载正常", "&&&&&");
                showAd(activity);
            }

            @Override
            public void onVideoCached() {

            }

            @Override
            public void onNoAD(AdError adError) {
                Log.e("打印广点通广告加载异常", adError.getErrorMsg() + "code->" + adError.getErrorCode());
            }

            @Override
            public void onADOpened() {

            }

            @Override
            public void onADExposure() {

            }

            @Override
            public void onADClicked() {

            }

            @Override
            public void onADLeftApplication() {

            }

            @Override
            public void onADClosed() {

            }
        });
        setVideoOption();
        ad.loadAD();
    }

    /**
     * 展示广告
     *
     * @param activity
     */
    public void showAd(Activity activity) {
        if (ad != null && ad.isValid()) {
            ad.show();
        } else {
            Toast.makeText(activity, "请加载广告后再进行展示 ！ ", Toast.LENGTH_LONG).show();
        }
    }

}
