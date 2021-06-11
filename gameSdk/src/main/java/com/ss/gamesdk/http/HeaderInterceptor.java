package com.ss.gamesdk.http;


import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import com.ss.gamesdk.BuildConfig;

/**
 * @Description 为请求添加header的过虑器
 */
public class HeaderInterceptor implements Interceptor {
    private HashMap<String, String> headerMap = new HashMap<>();

    public HeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        headerMap.clear();
        headerMap.putAll(getHeadParams());
        for (String key : headerMap.keySet()) {
            builder.addHeader(key, headerMap.get(key));
            Log.e("打印header信息", "key->" + key + "  values->" + headerMap.get(key));

        }
        return chain.proceed(builder.build());
    }


    public HashMap<String, String> getHeadParams() {
        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("version", BuildConfig.VERSION_NAME);
//        headerMap.put("packageName", BuildConfig.APPLICATION_ID);
//        headerMap.put("appid", RtConfig.get().getAppId());
//        headerMap.put("imei", MobileInfoUtil.get().getPhoneSign());
//        headerMap.put("imsi", MobileInfoUtil.getIMSI());
//        headerMap.put("Cookie", "sessionId = " + SignUtil.get().getSessionID());
        return headerMap;
    }
}
