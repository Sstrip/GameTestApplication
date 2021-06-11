package com.ss.gamesdk.weidgt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ss.gamesdk.R;
import com.ss.gamesdk.adapter.TaskAdapter;
import com.ss.gamesdk.bean.Task;

import java.util.List;

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
    }

}
