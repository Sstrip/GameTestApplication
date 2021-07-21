package com.ss.gamesdk.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ss.gamesdk.R;
import com.ss.gamesdk.adapter.CoinConvertAdapter;
import com.ss.gamesdk.base.GameSdk;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.ConvertInfo;
import com.ss.gamesdk.http.NetApi;
import com.ss.gamesdk.http.NetUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Copyright (C) 2019
 * CoinConvertDialog
 * <p>
 * Description
 * 兑换弹窗
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class CoinConvertDialog extends Dialog {
    private View content;
    /**
     * 关闭按钮
     */
    private ImageView ivClose;
    /**
     * 兑换按钮
     */
    private TextView tvConvert;
    /**
     * 兑换提示
     */
    private TextView tvTips;
    /**
     * 兑换的列表
     */
    private RecyclerView rvList;
    /**
     * 兑换列表适配器
     */
    private CoinConvertAdapter adapter;
    /**
     * 弹窗的标题
     */
    private TextView tvTitle;

    public CoinConvertDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        initView();
    }

    public CoinConvertDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BaseDialog);
        initView();
    }

    private void initView() {
        content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_coin_convert, null, false);
        setContentView(content);
        ivClose = content.findViewById(R.id.iv_close);
        tvConvert = content.findViewById(R.id.tv_convert_btn);
        tvTips = content.findViewById(R.id.tv_tips);
        rvList = content.findViewById(R.id.rv_list);
        tvTitle = content.findViewById(R.id.tv_title);
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CoinConvertAdapter(getContext());
        rvList.setAdapter(adapter);
        getConvertData();
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击兑换按钮
                String exchangeID = adapter.getCheckItemExchangeId();
                exchange(exchangeID);
            }
        });
    }

    /**
     * 兑换
     *
     * @param exchangeID
     */
    private void exchange(String exchangeID) {
        NetApi.exchangeCoin(GameSdk.getInstance().getChannel(), GameSdk.getInstance().getUserKey(), exchangeID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResultData<Object>>() {
                    @Override
                    public void call(ApiResultData<Object> objectApiResultData) {
                        GameSdk.OnCoverFinishListener listener = GameSdk.getInstance().getCoverFinishListener();
                        if (objectApiResultData.status == 100) {
                            //兑换成功
                            dismiss();
                            if (listener != null) {
                                listener.onFinish(true);
                            }
                            Toast.makeText(getContext(), "兑换成功", Toast.LENGTH_SHORT).show();
                        } else {
                            dismiss();
                            Toast.makeText(getContext(), objectApiResultData.msg, Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.onFinish(false);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (!isShowing()) {
                            return;
                        }
                        Toast.makeText(getContext(), "兑换失败,请重试", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * 获取兑换数据
     */
    private void getConvertData() {
        NetApi.getConvertData(GameSdk.getInstance().getChannel(), GameSdk.getInstance().getUserKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResultData<ConvertInfo>>() {
                    @Override
                    public void call(ApiResultData<ConvertInfo> convertInfoApiResultData) {
                        //拉取到数据
                        if (convertInfoApiResultData.data != null) {
                            tvTitle.setText(convertInfoApiResultData.data.getTitle());
                            tvTips.setText(convertInfoApiResultData.data.getTitleDown());
                            adapter.addAll(convertInfoApiResultData.data.getExchangeList());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
