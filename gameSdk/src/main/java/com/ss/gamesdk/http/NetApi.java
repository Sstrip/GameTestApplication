package com.ss.gamesdk.http;

import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.ConvertInfo;
import com.ss.gamesdk.bean.GrowingWrapTask;
import com.ss.gamesdk.bean.RewardInfo;
import com.ss.gamesdk.bean.SignDayBean;
import com.ss.gamesdk.bean.SignWrapInfo;
import com.ss.gamesdk.bean.Task;
import com.ss.gamesdk.bean.UserInfo;
import com.ss.gamesdk.bean.WrapAdConfigInfo;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Copyright (C) 2019
 * NetApi
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/4, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class NetApi {


    /**
     * 获取成长任务
     *
     * @param channel
     * @param userId
     * @param time
     * @return
     */
    public static Observable<ApiResultData<GrowingWrapTask>> getGrowList(String channel,
                                                                         String userId,
                                                                         String time) {
        return NetUtil.get().getOkClient().getGrowList(channel, userId, time);
    }

    /**
     * 获取指定任务
     *
     * @param channel
     * @param userId
     * @param taskCode
     * @return
     */
    public static Observable<ApiResultData<Task>> getTask(String channel, String userId, String taskCode) {
        return NetUtil.get().getOkClient().getTask(channel, userId, taskCode);
    }

    /**
     * 领取任务奖励
     *
     * @param channel
     * @param userId
     * @param taskCode
     * @return
     */
    public static Observable<ApiResultData<RewardInfo>> getReword(String channel, String userId, String taskCode, boolean doubleReword) {
        return NetUtil.get().getOkClient().getReword(channel, userId, taskCode, doubleReword);
    }

    /**
     * 返回广告配置
     *
     * @param channel
     * @return
     */
    public static Observable<ApiResultData<List<WrapAdConfigInfo>>> getAdConfig(String channel) {
        return NetUtil.get().getOkClient().getAdConfig(channel);
    }

    /**
     * 获取用户数据
     *
     * @param channel
     * @param userId
     * @return
     */
    public static Observable<ApiResultData<UserInfo>> getUserInfo(String channel, String userId) {
        return NetUtil.get().getOkClient().getUserInfo(channel, userId);
    }

    /**
     * 获取签到数据
     *
     * @param channel
     * @param userId
     * @return
     */
    public static Observable<ApiResultData<SignWrapInfo>> getSignData(String channel, String userId) {
        return NetUtil.get().getOkClient().getSignData(channel, userId);
    }

    /**
     * 获取兑换数据列表
     *
     * @param channel
     * @param userId
     * @return
     */
    public static Observable<ApiResultData<ConvertInfo>> getConvertData(String channel, String userId) {
        return NetUtil.get().getOkClient().getConvertData(channel, userId);
    }

    /**
     * 兑换接口
     *
     * @param channel
     * @param userId
     * @param exchangeId
     * @return
     */
    public static Observable<ApiResultData<Object>> exchangeCoin(String channel, String userId, String exchangeId) {
        return NetUtil.get().getOkClient().exchangeCoin(channel, userId, exchangeId);
    }


}

