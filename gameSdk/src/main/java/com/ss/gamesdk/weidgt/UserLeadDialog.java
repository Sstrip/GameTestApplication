package com.ss.gamesdk.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ss.gamesdk.R;
import com.ss.gamesdk.utils.AdUtils;

/**
 * Copyright (C) 2019
 * UserLeadDialog
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/2, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 新手用户引导弹窗
 */
public class UserLeadDialog extends Dialog {
    /**
     * gif图view
     */
    private ImageView ivGif;
    /**
     * 关闭弹窗
     */
    private ImageView ivClose;
    /**
     * 广告容器
     */
    private RelativeLayout flAdContent;


    public UserLeadDialog(@NonNull Context context) {
        super(context,R.style.BaseDialog);
        initView(context);
    }

    public UserLeadDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context) {
        View content = View.inflate(context, R.layout.dialog_user_lead, null);
        setContentView(content);
        ivGif = content.findViewById(R.id.iv_gif);
        ivClose = content.findViewById(R.id.iv_close);
        flAdContent = content.findViewById(R.id.fl_ad);
    }

    /**
     * 展示弹窗
     *
     * @param gifUrl
     * @param plame
     * @param style
     * @param adId
     */
    public void showUserLeadDialog(String gifUrl, int plame, int style, String adId) {
        UserLeadDialog.super.show();
        Glide.with(getContext()).asGif().load(gifUrl).into(ivGif);

        if (plame == -1 || style == -1 || TextUtils.isEmpty(adId)) {

        } else {
            AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(),282, adId, flAdContent);
//            AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(), "9011684347965344", rlAdContent);
        }


    }
}
