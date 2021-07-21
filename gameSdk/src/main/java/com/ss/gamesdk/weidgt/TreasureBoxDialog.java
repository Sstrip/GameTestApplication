package com.ss.gamesdk.weidgt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.gamesdk.R;
import com.ss.gamesdk.utils.EventUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Copyright (C) 2019
 * TreasureBoxDialog
 * <p>
 * Description
 * 宝箱弹窗
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class TreasureBoxDialog extends Dialog {
    private Activity activity;
    /**
     * 道具的图片url
     */
    private String propImgUrl;
    /**
     * 广告样式
     */
    private int style;
    /**
     * 激励视频广告id
     */
    private String adID;
    /**
     * 模板广告的id
     */
    private String templateAdId;
    /**
     * 广告平台
     */
    private int platform;
    /**
     * 获取道具的回调
     */
    private CompoundPropTipsDialog.OnGetPropListener listener;
    /**
     * 道具数量
     */
    private String count;
    /**
     * 关闭按钮
     */
    private TextView ivClose;

    /**
     * 回调
     */
    private Subscription subscription;
    /**
     * 道具名称
     */
    private String propName;

    public TreasureBoxDialog(@NonNull Activity context) {
        super(context, R.style.BaseDialog);
        this.activity = context;
        initView();
    }

    public TreasureBoxDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView();
    }

    /**
     * 初始化宝箱view
     */
    private void initView() {
        View content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_treasure_box, null, false);
        setContentView(content);
        ivClose = content.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭按钮
                dismiss();
            }
        });

        TextView tvOpen = content.findViewById(R.id.tv_open);
        tvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击开启宝箱
                EventUtil.get().addEvent("点击「宝箱中开启」按钮");
                dismiss();
                CompoundPropTipsDialog dialog = new CompoundPropTipsDialog(activity);
                dialog.show(propName, propImgUrl, count, platform, style, adID, templateAdId);
                dialog.setOnGetPropListener(listener);
            }
        });
    }

    /**
     * 设置领取道具奖励回调
     *
     * @param listener
     */
    public void setOnGetPropListener(CompoundPropTipsDialog.OnGetPropListener listener) {
        this.listener = listener;
    }


    /**
     * 展示道具弹窗
     *
     * @param propName     道具名称
     * @param propImgUrl
     * @param count        道具数量
     * @param platform
     * @param style
     * @param videoAdID
     * @param templateAdId
     */
    public void show(String propName, String propImgUrl, String count, int platform, int style, String videoAdID, String templateAdId) {
        super.show();
        this.platform = platform;
        this.count = count;
        this.propImgUrl = propImgUrl;
        this.style = style;
        this.adID = videoAdID;
        this.propName = propName;
        this.templateAdId = templateAdId;
        subscription = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        long time = 3 - aLong;
                        if (time >= 0) {
                            ivClose.setText(String.valueOf(time));
                            ivClose.setClickable(false);
                        } else {
                            subscription.unsubscribe();
                            ivClose.setText("X");
                            ivClose.setClickable(true);
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
