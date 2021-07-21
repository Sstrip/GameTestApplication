package com.ss.gamesdk.weidgt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.ss.gamesdk.R;
import com.ss.gamesdk.activity.SdkMainActivity;
import com.ss.gamesdk.base.GameSdk;
import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.utils.AdUtils;
import com.ss.gamesdk.utils.EventUtil;
import com.ss.gamesdk.utils.RewardVideoUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Copyright (C) 2019
 * CompoundPropTipsDialog
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class CompoundPropTipsDialog extends Dialog {
    private View content;
    /**
     * 道具的图片view
     */
    private ImageView ivPic;
    /**
     * 领取view
     */
    private TextView tvOK;
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
     * 依赖activity
     */
    private Activity activity;
    /**
     * 已经播放完视频
     */
    private boolean hasPlayVideo = false;
    /**
     * 领取道具的回调
     */
    private OnGetPropListener listener;
    /**
     * 模板广告的id
     */
    private String templateAdId;
    /**
     * 模板广告容器
     */
    private RelativeLayout rlAdContent;
    /**
     * 奖励道具数量
     */
    private String count;
    /**
     * 道具的数量
     */
    private TextView tvCount;
    /**
     * 道具名称
     */
    private TextView tvPropName;
    /**
     * 关闭按钮
     */
    private TextView tvClose;
    private Subscription subscription;


    public CompoundPropTipsDialog(@NonNull Activity context) {
        super(context, R.style.BaseDialog);
        this.activity = context;
        initView();
    }

    public CompoundPropTipsDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView();
    }

    /**
     * 设置领取道具奖励回调
     *
     * @param listener
     */
    public void setOnGetPropListener(OnGetPropListener listener) {
        this.listener = listener;
    }


    /**
     * 初始化view
     */
    private void initView() {
        content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_compound_prop_tips, null, false);
        setContentView(content);
        setCanceledOnTouchOutside(false);
        ivPic = content.findViewById(R.id.iv_prop_img);
        tvOK = content.findViewById(R.id.tv_ok);
        rlAdContent = content.findViewById(R.id.rl_ad_content);
        tvCount = content.findViewById(R.id.tv_count);
        tvPropName = content.findViewById(R.id.tv_prop_name);
        tvClose = content.findViewById(R.id.iv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //优先播放激励视频广告
                EventUtil.get().addEvent("点击「幸运道具中开启」按钮");
                if (platem == -1 || style == -1 || TextUtils.isEmpty(adID)) {


                } else if (AdConfigInfo.PLAT_SGM.equals(String.valueOf(platem))) {
                    //优先移除掉所有的view
                    //需要显示广告
                    AdUtils.getInstance(getContext()).loadOtherRewardVideoAD(activity, adID, new RewardVideoUtils.OnVideoAdListener() {
                        @Override
                        public void onVideoFinish() {

                        }

                        @Override
                        public void onVideoClose() {
                            dismiss();
                            //需要与js交互 领取道具
                            if (null != listener) {
                                listener.onGetProp();
                            }
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
                            dismiss();
                            //需要与js交互 领取道具
                            if (null != listener) {
                                listener.onGetProp();
                            }
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
     * 展示领取道具弹窗
     *
     * @param propName 道具名称
     * @param imgUrl
     */
    public void show(String propName, String imgUrl, int platem, int style, String adId) {
        super.show();
        Glide.with(getContext()).load(imgUrl).into(ivPic);
        tvCount.setVisibility(View.GONE);
        this.style = style;
        this.adID = adId;
        this.platem = platem;
        tvPropName.setText(propName);
        showClose();
        rlAdContent.setVisibility(View.GONE);
    }

    /**
     * 带模板广告的道具获取弹窗
     *
     * @param propName     道具名称
     * @param imgUrl
     * @param count        道具数量
     * @param platem
     * @param style
     * @param videoAdId
     * @param templateAdId 模板广告id
     */
    public void show(String propName, String imgUrl, String count, int platem, int style, String videoAdId, String templateAdId) {
        super.show();
        Glide.with(getContext()).load(imgUrl).into(ivPic);
        this.style = style;
        this.count = count;
        this.adID = videoAdId;
        this.platem = platem;
        this.templateAdId = templateAdId;
        rlAdContent.setVisibility(View.VISIBLE);
        rlAdContent.removeAllViews();
        tvCount.setVisibility(View.VISIBLE);
        tvPropName.setText(propName);
        tvCount.setText("X" + count);
        showClose();
        AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(), 282, adID, rlAdContent);
    }

    private void showClose() {
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


    public interface OnGetPropListener {
        void onGetProp();
    }
}
