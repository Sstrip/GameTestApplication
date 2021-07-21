package com.ss.gamesdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAd;
import com.ss.gamesdk.R;
import com.ss.gamesdk.base.GameSdk;
import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.RewardInfo;
import com.ss.gamesdk.bean.Task;
import com.ss.gamesdk.http.NetApi;
import com.ss.gamesdk.utils.AdUtils;
import com.ss.gamesdk.utils.RewardVideoUtils;
import com.ss.gamesdk.utils.UserTimeUtil;
import com.ss.gamesdk.weidgt.RedPackageGetDialog;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Copyright (C) 2019
 * TaskAdapter
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/3, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 成长任务适配器
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHold> {
    /**
     * 数据列表
     */
    private List<Task> list;
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 当前activity
     */
    private Activity activity;
    /**
     * 是否可领取
     */
    private boolean isGetReward = true;


    public TaskAdapter(List<Task> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;

    }

    /**
     * 添加倒计时回调
     */
    private void addTimeCutDownCallBack() {
        UserTimeUtil.getInstance().addTimeCallBack(GameSdk.getInstance().getGrowingTaskTimeSpace() * 1000, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                UserTimeUtil.getInstance().removeTimeCallBack(this);
                isGetReward = true;
            }
        });
    }


    @NonNull
    @Override
    public TaskViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TaskViewHold(LayoutInflater.from(context).inflate(R.layout.item_task, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHold taskViewHold, int i) {
        taskViewHold.bindData(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class TaskViewHold extends RecyclerView.ViewHolder {
        /**
         * 图标icon
         */
        private ImageView ivIcon;
        /**
         * 标题
         */
        private TextView tvTitle;
        /**
         * 按钮
         */
        private TextView tvBtn;
        /**
         * 进度
         */
        private View viewProgress;
        /**
         * 任务实体
         */
        private Task task;

        public TaskViewHold(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            viewProgress = itemView.findViewById(R.id.view_progress);
            tvBtn = itemView.findViewById(R.id.tv_button);
            tvBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击领取奖励
                    if (task == null) {
                        return;
                    }
                    //TODO 校验其他时长任务是否领取完成
                    if (!isGetReward) {
                        //有未完成的任务.提示完成
                        Toast.makeText(context, "还需等待" + GameSdk.getInstance().getGrowingTaskTimeSpace() / 60 + "分钟才可领取", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isGetReward = false;
                    if (task.getAdConfig() == null) {
                        getAward();
                        return;
                    }
                    //TODO 需要先看激励视频广告
                    if (AdConfigInfo.PLAT_GDT.equals(task.getAdConfig().getChannel())) {
                        //默认2为广点通平台
                        AdUtils.getInstance(context).loadRewardVideoAD(task.getAdConfig().getAdPlatformInfo().getYlhAppid(),
                                task.getAdConfig().getBundleid(),
                                new RewardVideoUtils.OnVideoAdListener() {
                                    @Override
                                    public void onVideoFinish() {

                                    }

                                    @Override
                                    public void onVideoClose() {
                                        getAward();
                                    }

                                    @Override
                                    public void onVideoLoaded(Object ad) {
                                        if (ad != null) {
                                            ((ExpressRewardVideoAD) ad).showAD(null);
                                        }
                                    }

                                });
                    } else if (AdConfigInfo.PLAT_SGM.equals(task.getAdConfig().getChannel())) {
                        //其他广告平台
                        AdUtils.getInstance(context).loadOtherRewardVideoAD(activity, task.getAdConfig().getBundleid(), new RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd>() {
                            @Override
                            public void onVideoFinish() {

                            }

                            @Override
                            public void onVideoClose() {
                                getAward();
                            }

                            @Override
                            public void onVideoLoaded(WindRewardedVideoAd ad) {
                                AdUtils.getInstance(context).showOtherRewardVideoAd(activity);
                            }
                        });
                    }
                }
            });
        }

        /**
         * 获取任务奖励
         */
        private void getAward() {
            //激励视频结束才可拉取奖励
            NetApi.getReword(GameSdk.getInstance().getChannel(),
                    GameSdk.getInstance().getUserKey(),
                    String.valueOf(task.getTaskCode()),
                    false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ApiResultData<RewardInfo>>() {
                        @Override
                        public void call(ApiResultData<RewardInfo> stringApiResultData) {
                            //拉取到数据
                            if (stringApiResultData.status == 100) {
                                //领取成功
                                //finish为0标识领取奖励
                                task.setFinish("2");
//                                RedPackageGetDialog packageGetDialog = new RedPackageGetDialog(context);
//                                //todo 红包弹窗 需要金币的数量和模板广告的id
//                                packageGetDialog.show(String.valueOf(coin), GameSdk.getInstance().getRedBagVideoAdInfo().getBundleid());
                                Toast.makeText(context, stringApiResultData.msg, Toast.LENGTH_SHORT).show();
                                addTimeCutDownCallBack();
                            } else {
                                Toast.makeText(context, stringApiResultData.msg, Toast.LENGTH_SHORT).show();
                            }
                            notifyDataSetChanged();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(context, "领取失败,请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


//        private boolean checkTask(Task currentTask) {
//            int index = list.indexOf(currentTask);
//            for (int i = 0; i < index; i++) {
//                Task task = list.get(i);
//                long taskTime = task.getOperationTime() * 60 * 1000;
//                boolean isGet = UserTimeUtil.getInstance().checkIfGet(taskTime);
//                if (!"2".equals(task.getFinish()) && isGet) {
//                    return false;
//                }
//            }
//            return true;
//        }


        /**
         * 绑定task和UI
         *
         * @param task
         */
        void bindData(Task task) {
            this.task = task;
            tvTitle.setText(task.getTaskName());
            //任务时长的毫秒值
            long taskTime = task.getOperationTime() * 60 * 1000;
            boolean isGet = UserTimeUtil.getInstance().checkIfGet(taskTime);
            if ("2".equals(task.getFinish())) {
                //说明任务已经完成
                tvBtn.setBackgroundResource(R.mipmap.bg_growing_get);
            } else {
                tvBtn.setSelected(isGet);
            }
            viewProgress.setSelected(isGet);
        }
    }
}
