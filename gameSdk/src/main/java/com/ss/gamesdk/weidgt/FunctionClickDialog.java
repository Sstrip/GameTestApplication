package com.ss.gamesdk.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ss.gamesdk.R;
import com.ss.gamesdk.utils.AdUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Copyright (C) 2019
 * FunctionClickDialog
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/2, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 点击功能按钮弹窗
 */
public class FunctionClickDialog extends Dialog {
    /**
     * 关闭按钮
     */
    private TextView ivClose;
    /**
     * 标题
     */
    private TextView tvTitle;
    /**
     * 广告容器
     */
    private RelativeLayout rlAdContent;
    /**
     * 无广告背景
     */
    private ImageView ivNoad;
    /**
     * 回调
     */
    private Subscription subscription;
    /**
     * 点击事件回调
     */
    private View.OnClickListener listener;

    public FunctionClickDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    public FunctionClickDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    /**
     * 初始化view
     *
     * @param context
     */
    public void initView(Context context) {
        View content = View.inflate(context, R.layout.dialog_click_funciton, null);
        setContentView(content);
        ivClose = content.findViewById(R.id.iv_close);
        tvTitle = content.findViewById(R.id.tv_title);
        rlAdContent = content.findViewById(R.id.rl_ad_content);
        ivNoad = content.findViewById(R.id.iv_no_ad);
        setCanceledOnTouchOutside(false);

    }

    /**
     * 展示功能按钮点击弹窗
     *
     * @param title
     * @param platem
     * @param style
     * @param adID
     */
    public void showFunctionClickDialog(String title, int platem, int style, String adID, long cutDownTime) {
        FunctionClickDialog.super.show();
        tvTitle.setText(title);
        subscription = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        long time = cutDownTime - aLong;
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
        if (platem == -1 || style == -1 || TextUtils.isEmpty(adID)) {
            ivNoad.setVisibility(View.VISIBLE);
        } else {
            //优先移除掉所有的view
            rlAdContent.removeAllViews();
            //TODO 需要显示广告
                    AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(),282, adID, rlAdContent);
//            AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(), "6071082326967264", rlAdContent);
        }
    }

    /**
     * 设置点击事件
     *
     * @param listener
     */
    public void setOnButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
        ivClose.setOnClickListener(listener);
    }


}
