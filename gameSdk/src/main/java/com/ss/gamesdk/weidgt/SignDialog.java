package com.ss.gamesdk.weidgt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.gamesdk.R;
import com.ss.gamesdk.adapter.SignDayTabAdapter;
import com.ss.gamesdk.adapter.SignRedPackageAdapter;
import com.ss.gamesdk.base.GameSdk;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.SignDayBean;
import com.ss.gamesdk.bean.SignWrapInfo;
import com.ss.gamesdk.bean.Task;
import com.ss.gamesdk.http.NetApi;
import com.ss.gamesdk.utils.UserTimeUtil;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Copyright (C) 2019
 * SignDialog
 * <p>
 * Description
 * 签到弹窗
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class SignDialog extends Dialog {
    private View content;
    /**
     * 关闭按钮
     */
    private ImageView ivClose;
    /**
     * 金币提示
     */
    private TextView tvCoin;
    /**
     * 日期的tab  list
     */
    private RecyclerView rvDayTab;
    /**
     * 红包数据
     */
    private RecyclerView rvRedPackage;
    /**
     * 日期tab 适配器
     */
    private SignDayTabAdapter dayTabAdapter;
    /**
     * 红包任务适配器
     */
    private SignRedPackageAdapter signRedPackageAdapter;

    private Activity activity;


    public SignDialog(@NonNull Activity context) {
        super(context, R.style.BaseDialog);
        this.activity = context;
        initView();
    }

    public SignDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sign, null, false);
        setContentView(content);
        ivClose = content.findViewById(R.id.iv_close);
        tvCoin = content.findViewById(R.id.tv_coin_tips);
        rvDayTab = content.findViewById(R.id.rv_day_tab);
        rvDayTab.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvRedPackage = content.findViewById(R.id.rv_red_package);
        rvRedPackage.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dayTabAdapter = new SignDayTabAdapter(getContext());
        rvDayTab.setAdapter(dayTabAdapter);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击关闭
                dismiss();
            }
        });
        dayTabAdapter.setOnDayTabSelectedChangeListener(new SignDayTabAdapter.OnDayTabSelectedChangeListener() {
            @Override
            public void onSelectedChange(List<Task> tasks) {
                if (signRedPackageAdapter != null) {
                    signRedPackageAdapter.addAll(tasks, true);
                    signRedPackageAdapter.notifyDataSetChanged();
                }
            }
        });
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        NetApi.getSignData(GameSdk.getInstance().getChannel(), GameSdk.getInstance().getUserKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResultData<SignWrapInfo>>() {
                    @Override
                    public void call(ApiResultData<SignWrapInfo> listApiResultData) {
                        if (listApiResultData.data != null) {
                            SignWrapInfo wrapInfo = listApiResultData.data;
                            tvCoin.setText("领取签到金币" + wrapInfo.getCoinTotal());
                            List<SignDayBean> signDayBeans = wrapInfo.getSignTaskList();
                            initDayTabData(signDayBeans);
                            dayTabAdapter.addAll(signDayBeans);
                            dayTabAdapter.notifyDataSetChanged();
                            if (signRedPackageAdapter == null) {
                                signRedPackageAdapter = new SignRedPackageAdapter(activity);
                                signRedPackageAdapter.setOnRedPackageGetListener(new SignRedPackageAdapter.OnRedPackageGetListener() {
                                    @Override
                                    public void onRedPackageGet(int coin) {
                                        //红包获取到
                                        RedPackageGetDialog packageGetDialog = new RedPackageGetDialog(getContext());
                                        //todo 红包弹窗 需要金币的数量和模板广告的id
                                        packageGetDialog.show(String.valueOf(coin), GameSdk.getInstance().getRedBagVideoAdInfo().getBundleid());
                                        dismiss();
                                    }
                                });
                                rvRedPackage.setAdapter(signRedPackageAdapter);
                            }
                            signRedPackageAdapter.addAll(signDayBeans.get(0).getTasks(), true);
                            signRedPackageAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }


    /**
     * 初始化日期tab的数据
     */
    private void initDayTabData(List<SignDayBean> signDayBeans) {
        if (signDayBeans.size() < 7) {
            return;
        }
        signDayBeans.get(0).setTitle("第一天");
        signDayBeans.get(1).setTitle("第二天");
        signDayBeans.get(2).setTitle("第三天");
        signDayBeans.get(3).setTitle("第四天");
        signDayBeans.get(4).setTitle("第五天");
        signDayBeans.get(5).setTitle("第六天");
        signDayBeans.get(6).setTitle("第七天");
    }

    @Override
    public void onDetachedFromWindow() {
        UserTimeUtil.getInstance().removeTimeCallBack(signRedPackageAdapter);
        super.onDetachedFromWindow();
    }
}
