package com.ss.gamesdk.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAd;
import com.ss.gamesdk.R;
import com.ss.gamesdk.base.GameSdk;
import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.http.NetApi;
import com.ss.gamesdk.utils.AdUtils;
import com.ss.gamesdk.utils.RewardVideoUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Copyright (C) 2019
 * GamelevelFinishDialog
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/2, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 游戏关卡完成弹窗
 */
public class GameLevelFinishDialog extends Dialog {
    /**
     * 标题
     */
    private TextView tvTitle;
    /**
     * 广告容器
     */
    private RelativeLayout rlAdContent;
    /**
     * 关闭按钮
     */
    private TextView tvClose;
    /**
     * 无广告的imageView
     */
    private ImageView ivNoAd;
    /**
     * 双倍奖励
     */
    private TextView tvDouble;
    /**
     * 广告平台
     */
    private String plateAd;
    /**
     * 激励视频的appid
     */
    private String appId;
    /**
     * 广告id
     */
    private String adId;
    /**
     * 任务code
     */
    private String taskCode;

    /**
     * 回调
     */
    private Subscription subscription;

    public GameLevelFinishDialog(@NonNull Context context) {
        super(context,R.style.BaseDialog);
        initView(context);
    }

    public GameLevelFinishDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    /**
     * 初始化view
     *
     * @param context
     */
    private void initView(Context context) {
        View content = View.inflate(context, R.layout.dialog_game_level_finish, null);
        setContentView(content);
        tvTitle = content.findViewById(R.id.tv_title);
        rlAdContent = content.findViewById(R.id.rl_ad_content);
        tvClose = content.findViewById(R.id.tv_close);
        ivNoAd = content.findViewById(R.id.iv_no_ad);
        tvDouble = content.findViewById(R.id.tv_button_double);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击双倍奖励播放激励视频广告
                if (AdConfigInfo.PLAT_GDT.equals(plateAd)) {
                    //广点通的激励视频广告
                    AdUtils.getInstance(getContext()).loadRewardVideoAD(appId, adId,
                            new RewardVideoUtils.OnVideoAdListener() {

                                @Override
                                public void onVideoFinish() {

                                }

                                @Override
                                public void onVideoClose() {
                                    //需要传入一个taskID
                                    getAward(true);
                                }

                                @Override
                                public void onVideoLoaded(Object ad) {
                                    if (ad != null && isShowing()) {
                                        ((ExpressRewardVideoAD) ad).showAD(null);
                                    }
                                }
                            });
                } else if (AdConfigInfo.PLAT_SGM.equals(plateAd)) {
                    AdUtils.getInstance(getContext()).loadOtherRewardVideoAD(getOwnerActivity(), adId, new RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd>() {
                        @Override
                        public void onVideoFinish() {

                        }

                        @Override
                        public void onVideoClose() {
                            //需要传入一个taskID
                            getAward(true);
                        }

                        @Override
                        public void onVideoLoaded(WindRewardedVideoAd ad) {
                            if (isShowing()) {
                                AdUtils.getInstance(getContext()).showOtherRewardVideoAd(getOwnerActivity());
                            }
                        }
                    });
                }
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //页面消失时调用领取奖励逻辑.
                getAward(false);
            }
        });
    }

    /**
     * 领取奖励
     */
    private void getAward(boolean isDouble) {
        if (!TextUtils.isEmpty(taskCode)) {
            NetApi.getReword(GameSdk.getInstance().getChannel(),
                    GameSdk.getInstance().getUserKey(),
                    taskCode,
                    isDouble)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ApiResultData<String>>() {
                        @Override
                        public void call(ApiResultData<String> stringApiResultData) {
                            Toast.makeText(getContext(), "已领取奖励", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }
    }


    /**
     * 展示游戏关卡结束的弹窗
     *
     * @param title
     * @param platem
     * @param style
     * @param adID
     * @param btnString
     */
    public void showGameLevelFinishDialog(String title, int platem, int style, String adID, String btnString,
                                          long cutDownTime, int plateAd, String appId, String rewardAdID, String taskCode) {
        GameLevelFinishDialog.super.show();
        this.plateAd = plateAd == 0 ? AdConfigInfo.PLAT_GDT : plateAd == 1 ? AdConfigInfo.PLAT_SGM : "";
        this.appId = appId;
        this.adId = rewardAdID;
        this.taskCode = taskCode;
        tvTitle.setText(title);
        if (platem == -1 || style == -1 || TextUtils.isEmpty(adID)) {
            ivNoAd.setVisibility(View.VISIBLE);
        } else {
            ivNoAd.setVisibility(View.GONE);
            AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(),250, adID, rlAdContent);
//            AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(), "9011684347965344", rlAdContent);
        }

        subscription = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        long time = cutDownTime - aLong;
                        if (time >= 0) {
                            tvClose.setText(String.valueOf(time));
                            tvClose.setClickable(false);
                        } else {
                            subscription.unsubscribe();
                            tvClose.setText("X");
                            tvClose.setClickable(true);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("打印异常", throwable.toString());
                    }
                });




//        if (TextUtils.isEmpty(btnString)) {
//            tvButton.setVisibility(View.GONE);
//        } else {
//            tvButton.setVisibility(View.VISIBLE);
//            tvButton.setText(btnString);
//        }
    }
}
