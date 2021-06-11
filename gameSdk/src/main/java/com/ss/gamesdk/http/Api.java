package com.ss.gamesdk.http;

import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.Task;
import com.ss.gamesdk.bean.UserInfo;

import java.util.List;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Copyright (C) 2019
 * Api
 * <p>
 * Description
 *
 * @author Administrator
 * @version 1.0
 * <p>
 * Ver 1.0, 2019/10/24, Administrator, Create file
 */
public interface Api {

    /**
     * 成长任务列表
     *
     * @return
     */
    @POST("/tasksdkservice/task/growlist")
    Observable<ApiResultData<List<Task>>> getGrowList(@Query("channel") String channel,
                                                      @Query("userId") String userId,
                                                      @Query("time") String time);

    /**
     * 获取指定任务
     *
     * @return
     */
    @POST("/tasksdkservice/task/getTask")
    Observable<ApiResultData<Task>> getTask(@Query("channel") String channel,
                                            @Query("userId") String userId,
                                            @Query("taskCode") String taskCode);

    /**
     * 领取奖励
     *
     * @return
     */
    @POST("/tasksdkservice/task/getReword")
    Observable<ApiResultData<String>> getReword(@Query("channel") String channel,
                                                @Query("userId") String userId,
                                                @Query("taskCode") String taskCode,
                                                @Query("doubleReword")boolean  doubleReword);

    /**
     * 获取广告配置数据
     *
     * @param channel
     * @return
     */
    @POST("/tasksdkservice/mapi/config/adconfig")
    Observable<ApiResultData<List<AdConfigInfo>>> getAdConfig(@Query("channel") String channel);

    /**
     * 获取用户数据信息
     *
     * @param channel
     * @param userId
     * @return
     */
    @POST("/tasksdkservice/user/getUserInfo")
    Observable<ApiResultData<UserInfo>> getUserInfo(@Query("channel") String channel,
                                                    @Query("userId") String userId);

}
