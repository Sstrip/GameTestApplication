package com.ss.gamesdk.http;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Copyright (C) 2019
 * LoggingInterceptor
 * <p>
 * Description
 *
 * @author Administrator
 * @version 1.0
 * <p>
 * Ver 1.0, 2019/11/8, Administrator, Create file
 */
public class LoggingInterceptor implements Interceptor {
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private  Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SuppressLint("DefaultLocale")
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Log.e("打印网络访问开始", String.format("Sending %s request %s on %s%n%s", request.method(), request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return response;
        }
        String bodyString = responseBody.string();
        long t2 = System.nanoTime();
        Log.e("打印网络访问响应", String.format("Received response for %s in %.1fms%n%s" + SINGLE_DIVIDER + SINGLE_DIVIDER + "\n %s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers(), bodyString));

        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
    }
}
