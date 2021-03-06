package com.ss.gamesdk.weidgt;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ss.gamesdk.R;
import com.ss.gamesdk.utils.AdUtils;

/**
 * Copyright (C) 2019
 * GameOverDialog
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/3, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 游戏结束弹窗
 */
public class GameOverDialog extends Dialog {
    /**
     * 广告容器
     */
    private RelativeLayout rlAdContent;
    /**
     * 底部按钮
     */
    private TextView tvButton;
    /**
     * 关闭按钮
     */
    private TextView tvClose;
    /**
     * 点击事件
     */
    private View.OnClickListener listener;
    private AnimatorSet animatorSet;


    public GameOverDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    public GameOverDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    private void initView(Context context) {
        View content = View.inflate(context, R.layout.dialog_game_over, null);
        setContentView(content);
        rlAdContent = content.findViewById(R.id.rl_ad_content);
        tvButton = content.findViewById(R.id.tv_button);
        tvClose = content.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭
                dismiss();
            }
        });
        startAnim(tvButton);
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
     * 展示游戏结束弹窗
     *
     * @param platem
     * @param style
     * @param adID
     * @param btnString
     */
    public void showGameOverDialog(String title, int platem, int style, String adID, String btnString) {
        GameOverDialog.super.show();
        tvButton.setText(btnString);
        if (platem == -1 || style == -1 || TextUtils.isEmpty(adID)) {
            //展示默认占位图

        } else {
            AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(), 250, adID, rlAdContent);
//            AdUtils.getInstance(getContext()).loadGdtNativeExpressAd(getContext(), "9011684347965344", rlAdContent);
        }
    }

    /**
     * 设置点击监听
     *
     * @param listener
     */
    public void setButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
        //点击查看分数（金币）
        tvButton.setOnClickListener(listener);
    }


}
