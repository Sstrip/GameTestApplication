package com.ss.gamesdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAd;
import com.ss.gamesdk.R;
import com.ss.gamesdk.activity.SdkMainActivity;
import com.ss.gamesdk.base.GameSdk;
import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.RewardInfo;
import com.ss.gamesdk.bean.SignDayBean;
import com.ss.gamesdk.bean.Task;
import com.ss.gamesdk.http.NetApi;
import com.ss.gamesdk.utils.AdUtils;
import com.ss.gamesdk.utils.EventUtil;
import com.ss.gamesdk.utils.RewardVideoUtils;
import com.ss.gamesdk.utils.UserTimeUtil;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Copyright (C) 2019
 * SIgnRedPackageAdapter
 * <p>
 * Description
 * 签到红包适配器
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class SignRedPackageAdapter extends RecyclerView.Adapter<SignRedPackageAdapter.SignRedPackageViewHold> implements UserTimeUtil.TimeCallBack {

    private Activity context;
    /**
     * 红包任务列表
     */
    private List<Task> taskList = new ArrayList<>();
    /**
     * 红包回调监听
     */
    private OnRedPackageGetListener listener;
    /**
     * 任务的提示
     */
    private String taskMsg;

    public SignRedPackageAdapter(Activity context) {
        this.context = context;
        //每秒回调
        UserTimeUtil.getInstance().addTimeCallBack(1000, this);
    }

    @NonNull
    @Override
    public SignRedPackageViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SignRedPackageViewHold(LayoutInflater.from(context).inflate(R.layout.item_sign_red_package, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SignRedPackageViewHold signRedPackageViewHold, int i) {
        signRedPackageViewHold.bindData(taskList.get(i));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void addAll(List<Task> tasks, boolean b) {
        if (b) {
            this.taskList.clear();
        }
        this.taskList.addAll(tasks);
    }

    @Override
    public void onCall() {
        String time = UserTimeUtil.getInstance().getSignCutDownTime();
        //不为空，说明还在倒计时中
        for (Task task : taskList) {
            if ("1".equals(task.getFinish())) {
                //可领取
                if (TextUtils.isEmpty(taskMsg)) {
                    taskMsg = task.getTaskMsg();
                }
                if (TextUtils.isEmpty(time)) {
                    task.setTaskMsg(taskMsg);
                    task.setOperationTime(0);
                    UserTimeUtil.getInstance().setSignCanRewardTime(0);
                } else {
                    task.setTaskMsg(time + "开启");
                }
            }
        }
        notifyDataSetChanged();
    }

    public class SignRedPackageViewHold extends RecyclerView.ViewHolder {
        /**
         * 未解锁的红包view
         */
        private RelativeLayout rlUnLock;
        /**
         * 已领取金币奖励
         */
        private RelativeLayout rlGetCoin;
        /**
         * 金币数
         */
        private TextView tvCoin;
        /**
         * 未解锁的提示
         */
        private TextView tvTips;
        /**
         * 任务实体
         */
        private Task task;


        public SignRedPackageViewHold(@NonNull View itemView) {
            super(itemView);
            rlUnLock = itemView.findViewById(R.id.rl_lock);
            rlGetCoin = itemView.findViewById(R.id.rl_has_get_coin);
            tvCoin = itemView.findViewById(R.id.tv_coin_count);
            tvTips = itemView.findViewById(R.id.tv_tips);
            rlUnLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo 未解锁，点击解锁需要调起激励视频广告
                    if (task == null || task.getAdConfig() == null) {
                        return;
                    }
                    EventUtil.get().addEvent("点击「签到中红包」按钮");
                    int taskCode = task.getTaskCode();
                    int cutDownTime = task.getOperationTime();
                    if ("1".equals(task.getFinish()) && UserTimeUtil.getInstance().getSignCutDownTimeBySeconds() == 0) {
                        //完成未领取状态 且倒计时为0 则播放激励视频领取
                        if (AdConfigInfo.PLAT_GDT.equals(task.getAdConfig().getChannel())) {
                            AdUtils.getInstance(context).loadRewardVideoAD(task.getAdConfig().getAdPlatformInfo().getYlhAppid(), task.getAdConfig().getBundleid(), new RewardVideoUtils.OnVideoAdListener() {
                                @Override
                                public void onVideoFinish() {

                                }

                                @Override
                                public void onVideoClose() {
                                    //广告关闭 拉取上报接口
                                    getRedPackage(String.valueOf(taskCode), cutDownTime);
                                }

                                @Override
                                public void onVideoLoaded(Object ad) {
                                    if (ad != null) {
                                        ((ExpressRewardVideoAD) ad).showAD(null);
                                    }
                                }
                            });
                        } else if (AdConfigInfo.PLAT_SGM.equals(task.getAdConfig().getChannel())) {
                            AdUtils.getInstance(context).loadOtherRewardVideoAD(context, task.getAdConfig().getBundleid(), new RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd>() {
                                @Override
                                public void onVideoFinish() {

                                }

                                @Override
                                public void onVideoClose() {
                                    //广告关闭 拉取上报接口
                                    getRedPackage(String.valueOf(taskCode), cutDownTime);
                                }

                                @Override
                                public void onVideoLoaded(WindRewardedVideoAd ad) {
                                    AdUtils.getInstance(context).showOtherRewardVideoAd(context);
                                }
                            });
                        }
                    }
                }
            });
        }

        /**
         * 获取红包的奖励
         *
         * @param taskCode
         */
        private void getRedPackage(String taskCode, int cutDownTime) {
            NetApi.getReword(GameSdk.getInstance().getChannel(), GameSdk.getInstance().getUserKey(), taskCode, false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ApiResultData<RewardInfo>>() {
                        @Override
                        public void call(ApiResultData<RewardInfo> stringApiResultData) {
                            //获取到红包奖励
                            if (stringApiResultData.status == 100) {
                                if (listener != null) {
                                    listener.onRedPackageGet(stringApiResultData.data.getTotalCoin());
                                }
                                long time = cutDownTime * 1000;
                                UserTimeUtil.getInstance().setSignCanRewardTime(time);
//                            task.setFinish("2");
//                            notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, stringApiResultData.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }


        void bindData(Task task) {
            this.task = task;
            if ("2".equals(task.getFinish())) {
                //已领取红包
                rlGetCoin.setVisibility(View.VISIBLE);
                rlUnLock.setVisibility(View.GONE);
            } else if ("1".equals(task.getFinish())) {
                rlGetCoin.setVisibility(View.GONE);
                rlUnLock.setVisibility(View.VISIBLE);
                if (task.getOperationTime() != 0) {
                    //说明还在倒计时中,开启倒计时
                    if (UserTimeUtil.getInstance().getSignCutDownTimeBySeconds() == 0) {
                        long time = task.getOperationTime() * 1000;
                        UserTimeUtil.getInstance().setSignCanRewardTime(time);
                        task.setTaskMsg(UserTimeUtil.getInstance().getSignCutDownTime() + "开启");
                    }
                }
            } else {
                rlGetCoin.setVisibility(View.GONE);
                rlUnLock.setVisibility(View.VISIBLE);
            }
            tvCoin.setText(String.valueOf(task.getRewardMax()));
            tvTips.setText(task.getTaskMsg());
        }
    }

    public void setOnRedPackageGetListener(OnRedPackageGetListener listener) {
        this.listener = listener;
    }


    /**
     * 红包奖励领取
     */
    public interface OnRedPackageGetListener {
        void onRedPackageGet(int coin);
    }


}
