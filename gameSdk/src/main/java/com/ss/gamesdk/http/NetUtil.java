package com.ss.gamesdk.http;


import com.ss.gamesdk.BuildConfig;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.Task;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Copyright (C) 2019
 * NetUtil
 * <p>
 * Description
 *
 * @author Administrator
 * @version 1.0
 * <p>
 * Ver 1.0, 2019/10/23, Administrator, Create file
 */
public class NetUtil {
    private Api api;
    private static NetUtil util;

    public static NetUtil get() {
        if (util == null) {
            util = new NetUtil();
        }
        return util;
    }

    Api getOkClient() {
        if (api == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);//重试
            clientBuilder.addInterceptor(new HeaderInterceptor());
            //配置日志拦截器
            if (BuildConfig.DEBUG) {
                clientBuilder.addInterceptor(new LoggingInterceptor());
            }
            api = getRetrofitBuilder("http://39.99.147.243:8089", clientBuilder.build()).build().create(Api.class);
        }
        return api;
    }

    private Retrofit.Builder getRetrofitBuilder(String url, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client);
    }




}
