package com.ss.gamesdk.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ss.gamesdk.R;
import com.ss.gamesdk.utils.AdUtils;

/**
 * Copyright (C) 2019
 * RedPackageGetDialog
 * <p>
 * Description
 * 红包奖励获取的弹窗
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class RedPackageGetDialog extends Dialog {
    /**
     * 关闭弹窗view
     */
    private ImageView ivClose;
    /**
     * 金币数量
     */
    private TextView tvCount;
    /**
     * 广告的容器
     */
    private RelativeLayout rlAdContent;
    /**
     * 标题
     */
    private TextView tvTitle;


    public RedPackageGetDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        initView();
    }

    public RedPackageGetDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        View content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_red_package_get, null, false);
        setContentView(content);
        ivClose = content.findViewById(R.id.iv_close);
        tvCount = content.findViewById(R.id.tv_coin_count);
        rlAdContent = content.findViewById(R.id.rl_ad_content);
        tvTitle = content.findViewById(R.id.tv_title);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击关闭弹窗
                dismiss();
            }
        });
    }

    public void show(String count, String adId) {
        tvCount.setText("X" + count);
        rlAdContent.removeAllViews();
        AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(), 282, adId, rlAdContent);
        super.show();
    }
}
