package com.ss.gamesdk.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.compliance.ApkDownloadComplianceInterface;
import com.qq.e.comm.compliance.DownloadConfirmCallBack;
import com.qq.e.comm.compliance.DownloadConfirmListener;
import com.qq.e.comm.util.AdError;

/**
 * Copyright (C) 2019
 * BannerAdUtil
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/5, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 加载banner广告
 */
public class BannerAdUtil {
    private static BannerAdUtil util;
    private UnifiedBannerView bv;

    public static BannerAdUtil getInstance() {
        if (util == null) {
            util = new BannerAdUtil();
        }
        return util;
    }

    /**
     * 加载banner广告
     *
     * @param adId
     * @param viewGroup
     */
    public void loadBannerAd(Activity context, String adId, ViewGroup viewGroup) {
        bv = new UnifiedBannerView(context, adId, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.e("打印广点通广告加载异常", adError.getErrorMsg() + "code->" + adError.getErrorCode());
            }

            @Override
            public void onADReceive() {
//                if (DownloadConfirmHelper.USE_CUSTOM_DIALOG) {
//                    bv.setDownloadConfirmListener(DownloadConfirmHelper.DOWNLOAD_CONFIRM_LISTENER);
//                }
                Log.e("打印广点通广告加载正常", "&*&(*&(*(*(");
            }

            @Override
            public void onADExposure() {

            }

            @Override
            public void onADClosed() {

            }

            @Override
            public void onADClicked() {

            }

            @Override
            public void onADLeftApplication() {

            }

            @Override
            public void onADOpenOverlay() {

            }

            @Override
            public void onADCloseOverlay() {

            }
        });

        viewGroup.removeAllViews();
        viewGroup.addView(bv, getUnifiedBannerLayoutParams(context));
        // 默认 30 秒轮播，可以不设置
        bv.setRefresh(20);
        //加载banner广告
        bv.loadAD();
    }


    /**
     * banner2.0规定banner宽高比应该为6.4:1 , 开发者可自行设置符合规定宽高比的具体宽度和高度值
     *
     * @return
     */
    private RelativeLayout.LayoutParams getUnifiedBannerLayoutParams(Activity activity) {
        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new RelativeLayout.LayoutParams(screenSize.x, Math.round(screenSize.x / 6.4F));
    }
}
