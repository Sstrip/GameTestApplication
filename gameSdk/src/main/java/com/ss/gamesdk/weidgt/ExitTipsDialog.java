package com.ss.gamesdk.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ss.gamesdk.R;

/**
 * Copyright (C) 2019
 * ExitTipsDialog
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/1, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 退出页面弹窗
 */
public class ExitTipsDialog extends Dialog {
    /**
     * 点击再玩一会
     */
    private TextView tvOk;
    /**
     * 点击放弃
     */
    private TextView tvGiveUp;
    /**
     * 点击弹窗的回调监听
     */
    private OnClickDialogListener listener;

    public ExitTipsDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    public ExitTipsDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    /**
     * 初始化view
     *
     * @param context
     */
    private void initView(Context context) {
        View content = View.inflate(context, R.layout.dialog_exit_tips, null);
        setContentView(content);
        tvOk = content.findViewById(R.id.tv_ok);
        tvGiveUp = content.findViewById(R.id.tv_give_up);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击再玩一会
                if(listener != null){
                    listener.onClickCancel();
                }
            }
        });
        tvGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击放弃
                if(listener != null){
                    listener.onClickFinish();
                }
            }
        });
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnClickDialogListener(OnClickDialogListener listener) {
        this.listener = listener;
    }


    public interface OnClickDialogListener {
        /**
         * 点击结束
         */
        void onClickFinish();

        /**
         * 点击取消
         */
        void onClickCancel();
    }

}
