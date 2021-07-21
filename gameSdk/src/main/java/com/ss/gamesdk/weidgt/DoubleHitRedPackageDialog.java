package com.ss.gamesdk.weidgt;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.ss.gamesdk.R;
import com.ss.gamesdk.base.GameSdk;
import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.RewardInfo;
import com.ss.gamesdk.http.NetApi;
import com.ss.gamesdk.utils.AdUtils;
import com.ss.gamesdk.utils.EventUtil;
import com.ss.gamesdk.utils.RewardVideoUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Copyright (C) 2019
 * DoubleHitRedPackageDialog
 * <p>
 * Description
 * 连击红包弹窗
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class DoubleHitRedPackageDialog extends Dialog {
    private View content;
    /**
     * 金币的提示
     */
    private TextView tvCoinTips;
    /**
     * 开启红包
     */
    private ImageView ivOpen;
    /**
     * 上下文页面
     */
    private Activity activity;

    /**
     * 广告类型
     */
    private int style;
    /**
     * 广告id
     */
    private String adID;
    /**
     * 广告平台
     */
    private int platem;
    /**
     * 获取红包金币的view
     */
    private LinearLayout linGetCoin;
    /**
     * 红包view
     */
    private RelativeLayout rlRedPackage;
    /**
     * 获取金币奖励的提示
     */
    private TextView tvGetCoinTips;
    /**
     * 领取金币按钮
     */
    private TextView tvGetCoin;
    /**
     * 获取随机金币的提示
     */
    private String randomCoinTips;
    /**
     * 任务id
     */
    private String taskCode;
    /**
     * 动画
     */
    private AnimatorSet animatorSet;
    /**
     * 关闭按钮
     */
    private TextView tvClose;
    private Subscription subscription;


    public DoubleHitRedPackageDialog(@NonNull Activity context) {
        super(context, R.style.BaseDialog);
        this.activity = context;
        initView();
    }

    public DoubleHitRedPackageDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_double_hit_red_package, null, false);
        setContentView(content);
        setCanceledOnTouchOutside(false);
        tvCoinTips = content.findViewById(R.id.tv_coin_tips);
        ivOpen = content.findViewById(R.id.iv_open);
        linGetCoin = content.findViewById(R.id.lin_get_coin);
        rlRedPackage = content.findViewById(R.id.rl_red_package);
        tvGetCoinTips = content.findViewById(R.id.tv_get_coin_tips);
        tvGetCoin = content.findViewById(R.id.tv_get_coin);
        tvClose = content.findViewById(R.id.iv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        startAnim(ivOpen);

        ivOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 点击开启红包
                EventUtil.get().addEvent("点击「3连击红包开」按钮");
                if (platem == -1 || style == -1 || TextUtils.isEmpty(adID)) {


                } else if (AdConfigInfo.PLAT_SGM.equals(String.valueOf(platem))) {
                    //优先移除掉所有的view
                    //TODO 需要显示广告`
                    AdUtils.getInstance(getContext()).loadOtherRewardVideoAD(activity, adID, new RewardVideoUtils.OnVideoAdListener() {
                        @Override
                        public void onVideoFinish() {

                        }

                        @Override
                        public void onVideoClose() {
                            showGetCoinDialog();
                        }

                        @Override
                        public void onVideoLoaded(Object ad) {
                            AdUtils.getInstance(activity).showOtherRewardVideoAd(activity);
                        }
                    });
                } else if (AdConfigInfo.PLAT_GDT.equals(String.valueOf(platem))) {
                    //广点通广告
                    AdUtils.getInstance(getContext()).loadRewardVideoAD(GameSdk.getInstance().getRewardVideoAdInfo().getAdPlatformInfo().getYlhAppid(), adID, new RewardVideoUtils.OnVideoAdListener() {
                        @Override
                        public void onVideoFinish() {

                        }

                        @Override
                        public void onVideoClose() {
                            showGetCoinDialog();
                        }

                        @Override
                        public void onVideoLoaded(Object ad) {
                            if (ad != null) {
                                ((ExpressRewardVideoAD) ad).showAD(null);
                            }
                        }
                    });
                }
            }
        });
    }


    /**
     * 邀请按钮呼吸效果
     */
    private void startAnim(View view) {
        //组合动画
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
            //后几个参数是放大的倍数
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1);
            //永久循环
            scaleX.setRepeatCount(ValueAnimator.INFINITE);
            scaleY.setRepeatCount(ValueAnimator.INFINITE);
            //时间
            animatorSet.setDuration(600);
            //两个动画同时开始
            animatorSet.play(scaleX).with(scaleY);
        }
        if (!animatorSet.isRunning()) {
            //开始
            animatorSet.start();
        }
    }


    /**
     * 展示领取金币奖励弹窗
     */
    private void showGetCoinDialog() {
        linGetCoin.setVisibility(View.VISIBLE);
        rlRedPackage.setVisibility(View.GONE);
        tvGetCoinTips.setText(randomCoinTips);
        tvGetCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击领取奖励 上报接口
                NetApi.getReword(GameSdk.getInstance().getChannel(), GameSdk.getInstance().getUserKey(), taskCode, false)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ApiResultData<RewardInfo>>() {
                            @Override
                            public void call(ApiResultData<RewardInfo> stringApiResultData) {
                                //领取到任务奖励 播放gif 然后消失
                                if (isShowing()) {
                                    if (stringApiResultData.status == 100) {
                                        //金币领取成功
                                        dismiss();
                                        //todo 播放gif
                                    } else {
                                        Toast.makeText(getContext(), stringApiResultData.msg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        });
            }
        });
    }


    public void show(String coinTips, String randomCoinTips, String taskCode, int platme, int style, String adId) {
        super.show();
        this.style = style;
        this.adID = adId;
        this.platem = platme;
        this.randomCoinTips = randomCoinTips;
        this.taskCode = taskCode;
        tvCoinTips.setText(coinTips);

        subscription = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        long time = 3 - aLong;
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

    }
}
