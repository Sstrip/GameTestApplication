package com.ss.gamesdk.weidgt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ss.gamesdk.R;
import com.ss.gamesdk.adapter.TaskAdapter;
import com.ss.gamesdk.bean.Task;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Copyright (C) 2019
 * GrowTaskDialog
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/3, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 成长任务
 */
public class GrowTaskDialog extends Dialog {
    /**
     * 成长任务列表
     */
    private RecyclerView rvTaskList;
    /**
     * 适配器
     */
    private TaskAdapter adapter;
    /**
     * 当前的activity
     */
    private Activity activity;
    /**
     * 关闭
     */
    private TextView tvClose;
    private Subscription subscription;


    public GrowTaskDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    public GrowTaskDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView(context);
    }

    /**
     * 初始化view
     *
     * @param context
     */
    private void initView(Context context) {
        this.activity = (Activity) context;
        View content = View.inflate(context, R.layout.dialog_grow_task, null);
        tvClose = content.findViewById(R.id.iv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(content);
        rvTaskList = content.findViewById(R.id.rv_task_list);
        rvTaskList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

    }

    /**
     * 展示成长任务弹窗
     *
     * @param list
     */
    public void showGrowTaskDialog(List<Task> list) {
        super.show();
        adapter = new TaskAdapter(list, getContext(), activity);
        rvTaskList.setAdapter(adapter);
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
