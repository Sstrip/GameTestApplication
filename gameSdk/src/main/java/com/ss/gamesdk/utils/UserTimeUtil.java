package com.ss.gamesdk.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2019
 * UserTimeUtil
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/4, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class UserTimeUtil {
    private static final String KEY_USER_TIME = "KEY_USER_TIME";


    private static UserTimeUtil util;
    /**
     * 用户游戏时长
     */
    private long userTime = 0;
    /**
     * 日期格式化
     */
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    /**
     * 时间回调
     */
    private Map<Integer, TimeMessage> callBackMap = new HashMap<>();
    /**
     * 用户的使用时长大于任务时长回调
     */
    private TimeDownCallBack taskTimeCallBack;

    /**
     * 计时handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            userTime = userTime + 1000;
            Map<Long, TimeMessage> temp = new HashMap(callBackMap);
            for (Map.Entry<Long, TimeMessage> entry : temp.entrySet()) {
                long time = System.currentTimeMillis() - entry.getValue().getLastTime();
                if (time >= entry.getValue().getTimeSpace()) {
                    entry.getValue().setLastTime(System.currentTimeMillis());
                    callBackMap.put(entry.hashCode(), entry.getValue());
                    //回调
                    entry.getValue().getCallBack().onCall();
                }
            }
            if (taskTimeCallBack != null) {
                //间隔1s回调
                taskTimeCallBack.onCall();
            }
            //延时1s发送消息
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    public static UserTimeUtil getInstance() {
        if (util == null) {
            util = new UserTimeUtil();
        }
        return util;
    }


    public long getUserTimer() {
        return userTime;
    }

    /**
     * 初始化用户时长
     */
    public void initUserTimer(Context context) {
        String date = format.format(new Date());
        userTime = SPUtil.getInstance(context).getLong(KEY_USER_TIME + date);
    }

    /**
     * 保存用户的时长
     *
     * @param context
     */
    public void saveUserTime(Context context) {
        String date = format.format(new Date());
        SPUtil.getInstance(context).put(KEY_USER_TIME + date, userTime);
    }


    /**
     * 开始计时
     */
    public void startTimer() {
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(0);
    }

    /**
     * 判断是否大于任务时长
     *
     * @param taskTime
     * @return
     */
    public boolean checkIfGet(long taskTime) {
        return userTime >= taskTime;
    }

    /**
     * 添加时间回调
     *
     * @param timeSpace 最小到秒值
     * @param callBack
     */
    public void addTimeCallBack(long timeSpace, TimeCallBack callBack) {
        TimeMessage message = new TimeMessage(callBack, timeSpace, System.currentTimeMillis());
        callBackMap.put(message.hashCode(), message);
    }

    /**
     * 设置时长回调
     *
     * @param taskTimeCallBack
     */
    public void setTimeDownCutCallBack(TimeDownCallBack taskTimeCallBack) {
        this.taskTimeCallBack = taskTimeCallBack;
    }


    /**
     * 判断用户的时长是否已经大于任务时长
     *
     * @param times
     * @return
     */
    public boolean checkTaskTime(long... times) {
        for (long time : times) {
            boolean isGet = checkIfGet(time);
            if (isGet) {
                return isGet;
            }
        }
        return false;
    }


    public class TimeMessage {
        private TimeCallBack callBack;
        private long timeSpace;
        private long lastTime;


        public TimeMessage(TimeCallBack callBack, long timeSpace, long lastTime) {
            this.callBack = callBack;
            this.timeSpace = timeSpace;
            this.lastTime = lastTime;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public TimeCallBack getCallBack() {
            return callBack;
        }

        public void setCallBack(TimeCallBack callBack) {
            this.callBack = callBack;
        }

        public long getTimeSpace() {
            return timeSpace;
        }

        public void setTimeSpace(long timeSpace) {
            this.timeSpace = timeSpace;
        }
    }


    /**
     * 时间回调
     */
    public interface TimeCallBack {
        void onCall();
    }

    /**
     * 计时回调 间隔1s回调
     */
    public interface TimeDownCallBack {
        void onCall();
    }

}
