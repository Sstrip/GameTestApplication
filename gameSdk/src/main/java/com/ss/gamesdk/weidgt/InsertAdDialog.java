package com.ss.gamesdk.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ss.gamesdk.R;

/**
 * Copyright (C) 2019
 * InsertAdDialog
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/2, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class InsertAdDialog extends Dialog {
    /**
     * 广告容器view
     */
    private RelativeLayout rlAdContent;
    /**
     * 背景imagevew
     */
    private ImageView ivBackGround;
    /**
     * 标题
     */
    private TextView tvTitle;


    public InsertAdDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    public InsertAdDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    /**
     * 初始化view
     *
     * @param context
     */
    private void initView(Context context) {
        View content = View.inflate(context, R.layout.dialog_insert_ad, null);
        setContentView(content);
        rlAdContent = content.findViewById(R.id.rl_ad_content);
        ivBackGround = content.findViewById(R.id.iv_no_ad);
        tvTitle = content.findViewById(R.id.tv_title);
    }

    /**
     * 展示dialog
     *
     * @param platmate
     * @param style
     * @param adId
     */
    public void showDialog(String title, int platmate, int style, String adId) {
        InsertAdDialog.super.show();
        tvTitle.setText(title);
        if (platmate == -1 || style == -1 || TextUtils.isEmpty(adId)) {
            ivBackGround.setVisibility(View.VISIBLE);
        } else {
            //移除默认的背景.直接加载广告
            rlAdContent.removeAllViews();
            //TODO 加载插屏广告
        }
    }
}
