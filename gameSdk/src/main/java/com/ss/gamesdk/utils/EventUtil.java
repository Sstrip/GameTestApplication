package com.ss.gamesdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.ss.gamesdk.BuildConfig;
import com.ss.gamesdk.base.GameSdk;
import com.tendcloud.tenddata.TCAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2019
 * EventUtil
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/7/4, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class EventUtil {
    private static EventUtil util;

    private Context context;

    public static EventUtil get() {
        if (util == null) {
            util = new EventUtil();
        }
        return util;
    }

    /**
     * 注册
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 获取基础属性
     *
     * @return
     */
    private Map<String, Object> getDefaultParam() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("渠道", GameSdk.getInstance().getChannel());
        param.put("app版本", getAppName(context));
        param.put("手机型号", android.os.Build.MODEL);
        param.put("手机系统版本", Build.VERSION.RELEASE);
        param.put("用户登录", GameSdk.getInstance().getUserKey());
        return param;
    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加事件
     *
     * @param eventName
     */
    public void addEvent(String eventName) {
        TCAgent.onEvent(context, eventName, "游戏sdk", getDefaultParam());
    }


}
