package com.ss.gamesdk.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * Copyright (C) 2019
 * NativeExpressUtil
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/5, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 平台模板广告
 */
public class NativeExpressUtil {
    private static NativeExpressUtil util;

    private NativeExpressAD nativeExpressAD;

    private NativeExpressADView nativeExpressADView;

    private boolean isPreloadVideo;
    /***
     * 容器
     */
    private RelativeLayout relativeLayout;


    public static NativeExpressUtil getInstance() {
        if (util == null) {
            util = new NativeExpressUtil();
        }
        return util;
    }


    public void loadNativeExpressAd(Context context, int width,String adID, RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
        nativeExpressAD = new NativeExpressAD(context, new ADSize(width, ADSize.AUTO_HEIGHT), adID, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                if (relativeLayout.getVisibility() != View.VISIBLE) {
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                if (relativeLayout.getChildCount() > 0) {
                    relativeLayout.removeAllViews();
                }

                nativeExpressADView = list.get(0);
//                if (DownloadConfirmHelper.USE_CUSTOM_DIALOG) {
//                    nativeExpressADView.setDownloadConfirmListener(DownloadConfirmHelper.DOWNLOAD_CONFIRM_LISTENER);
//                }
//                Log.i(TAG, "onADLoaded, video info: " + getAdInfo(nativeExpressADView));
                if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                    nativeExpressADView.setMediaListener(mediaListener);
                    if (isPreloadVideo) {
                        // 预加载视频素材，加载成功会回调mediaListener的onVideoCached方法，失败的话回调onVideoError方法errorCode为702。
                        nativeExpressADView.preloadVideo();
                    }
                } else {
                    isPreloadVideo = false;
                }
                if (!isPreloadVideo) {
                    // 广告可见才会产生曝光，否则将无法产生收益。
                    relativeLayout.addView(nativeExpressADView);
                    ((RelativeLayout.LayoutParams) nativeExpressADView.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
                    nativeExpressADView.render();
                }
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                if (relativeLayout != null && relativeLayout.getChildCount() > 0) {
                    relativeLayout.removeAllViews();
                    relativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onNoAD(AdError adError) {
                Log.e("打印广点通广告异常", adError.getErrorMsg() + "***" + adError.getErrorCode());
            }
        });
        nativeExpressAD.setMinVideoDuration(1);
        nativeExpressAD.setMaxVideoDuration(30);
        nativeExpressAD.loadAD(1);
    }

    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoCached(NativeExpressADView adView) {
            // 视频素材加载完成，此时展示视频广告不会有进度条。
            if (isPreloadVideo && nativeExpressADView != null) {
                if (relativeLayout.getChildCount() > 0) {
                    relativeLayout.removeAllViews();
                }
                // 广告可见才会产生曝光，否则将无法产生收益。
                relativeLayout.addView(nativeExpressADView);
                nativeExpressADView.render();
            }
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {

        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {

        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {

        }
    };

}
