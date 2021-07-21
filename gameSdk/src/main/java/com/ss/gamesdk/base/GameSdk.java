package com.ss.gamesdk.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.qq.e.comm.managers.GDTADManager;
import com.sigmob.windad.WindAdOptions;
import com.sigmob.windad.WindAds;
import com.ss.gamesdk.activity.SdkMainActivity;
import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.GrowingWrapTask;
import com.ss.gamesdk.bean.Task;
import com.ss.gamesdk.bean.WrapAdConfigInfo;
import com.ss.gamesdk.http.NetApi;
import com.ss.gamesdk.utils.EventUtil;
import com.ss.gamesdk.utils.UserTimeUtil;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Copyright (C) 2019
 * GameSdk
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/4/19, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class GameSdk {
    private static GameSdk gameSdk;
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 用户标识
     */
    private String userKey;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 广点通的appid
     */
    private String gdtAppID;
    /**
     * 其他广告的appID
     */
    private String otherAppID;
    /**
     * 其他广告的appkey
     */
    private String otherAppKey;
    /**
     * 成长任务
     */
    private List<Task> tasks;
    /**
     * banner的广告配置实体
     */
    private WrapAdConfigInfo bannerAdInfo;

    /**
     * 激励视频广告实体
     */
    private WrapAdConfigInfo rewardVideoAdInfo;
    /**
     * 红包激励视频广告
     */
    private WrapAdConfigInfo redBagVideoAdInfo;
    /**
     * 本地红包的广告
     */
    private WrapAdConfigInfo localRedPackageAdInfo;
    /**
     * 插屏广告
     */
    private WrapAdConfigInfo insertAdInfo;

    /**
     * 领取红包奖励的广告
     */
    private WrapAdConfigInfo getRedPackageAdInfo;

    /**
     * 成长任务的时间间隔
     */
    private int growingTaskTimeSpace;
    /**
     * 原子币兑换回调
     */
    private OnCoverFinishListener listener;
    /**
     * 安全联盟
     */
    private IdSupplier idSupplier;

    public static GameSdk getInstance() {
        if (gameSdk == null) {
            gameSdk = new GameSdk();
        }
        return gameSdk;
    }

    /**
     * 设置当兑换原子币回调
     *
     * @param listener
     */
    public void setOnCoverFinishListener(OnCoverFinishListener listener) {
        this.listener = listener;
    }

    /**
     * 获取原子币回调
     *
     * @return
     */
    public OnCoverFinishListener getCoverFinishListener() {
        return listener;
    }


    /**
     * 获取成长任务列表
     *
     * @return
     */
    public List<Task> getGrowTasks() {
        return tasks;
    }


    public String getChannel() {
        return channel;
    }


    public String getUserKey() {
        return userKey;
    }

    /**
     * 获取成长任务的时间间隔
     *
     * @return
     */
    public int getGrowingTaskTimeSpace() {
        return growingTaskTimeSpace;
    }


    /**
     * 初始化
     *
     * @param context 上下文
     * @param userKey 用户标识
     * @param channel 渠道
     */
    public void init(Activity context, String userKey, String channel) {
        getInstance().context = context.getApplicationContext();
        getInstance().userKey = userKey;
        getInstance().channel = channel;
        EventUtil.get().init(context);

        //拉取数据接口
        initAdConfig(context);

        initTalkingData(context);
        NetApi.getGrowList(channel, GameSdk.getInstance().getUserKey(),
                String.valueOf(UserTimeUtil.getInstance().getUserTimer() / (60 * 1000))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResultData<GrowingWrapTask>>() {
                    @Override
                    public void call(ApiResultData<GrowingWrapTask> listApiResultData) {
                        Log.e("打印成长任务回调", listApiResultData.getData().toString());
                        getInstance().tasks = listApiResultData.data.getTaskList();
                        growingTaskTimeSpace = listApiResultData.data.getInterval();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //异常
                        getInstance().tasks = new ArrayList<>();
                    }
                });

    }

    /**
     * 初始化广告配置
     *
     * @param context
     */
    private void initAdConfig(Activity context) {
        NetApi.getAdConfig(channel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResultData<List<WrapAdConfigInfo>>>() {
                    @Override
                    public void call(ApiResultData<List<WrapAdConfigInfo>> listApiResultData) {
                        //拉取到广告的配置数据
                        for (WrapAdConfigInfo info : listApiResultData.data) {
                            Log.e("打印广告相关", info.toString());
                            if (info.getLocation() == AdConfigInfo.LOCATION_BANNER) {
                                //banner广告数据
                                bannerAdInfo = info;
                                gdtAppID = bannerAdInfo.getAdList().get(0).getAdPlatformInfo().getYlhAppid();
                                otherAppID = bannerAdInfo.getAdList().get(0).getAdPlatformInfo().getSgmAppid();
                                otherAppKey = bannerAdInfo.getAdList().get(0).getAdPlatformInfo().getSgmAppkey();
                            } else if (info.getLocation() == AdConfigInfo.LOCATION_REWARD_VIDEO) {
                                //激励视频数据
                                rewardVideoAdInfo = info;
                            } else if (info.getLocation() == AdConfigInfo.LOCATION_RED_BAG) {
                                //红包激励视频广告
                                redBagVideoAdInfo = info;
                            } else if (info.getLocation() == AdConfigInfo.LOCATION_INSERT_AD) {
                                // 两分钟的插屏广告
                                insertAdInfo = info;
                            } else if (info.getLocation() == AdConfigInfo.LOCATION_PERMANENT_RED_PACKAGE) {
                                //常驻红包广告
                                localRedPackageAdInfo = info;
                            } else if (info.getLocation() == AdConfigInfo.LOCATION_GET_RED_PACKAGE) {
                                //红包领取奖励后的广告
                                getRedPackageAdInfo = info;
                            }
                        }
                        //初始化广告sdk
                        initGDTsdk();
                        initOtherAD((Activity) context);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }


    /**
     * 获取banner的广告配置
     *
     * @return
     */
    public AdConfigInfo getBannerAdInfo() {
        if (bannerAdInfo == null || bannerAdInfo.getAdList() == null) {
            return null;
        }
        int tempCurrent = bannerAdInfo.getCurrentPosition();
        if (tempCurrent >= bannerAdInfo.getAdList().size() - 1) {
            bannerAdInfo.setCurrentPosition(0);
            return bannerAdInfo.getAdList().get(0);
        }
        AdConfigInfo info = bannerAdInfo.getAdList().get(tempCurrent);
        tempCurrent++;
        bannerAdInfo.setCurrentPosition(tempCurrent);
        return info;
    }

    /**
     * 获取激励视频广告的配置实体
     *
     * @return
     */
    public AdConfigInfo getRewardVideoAdInfo() {
        if (rewardVideoAdInfo == null || rewardVideoAdInfo.getAdList() == null) {
            return null;
        }
        int tempCurrent = rewardVideoAdInfo.getCurrentPosition();
        if (tempCurrent >= rewardVideoAdInfo.getAdList().size() - 1) {
            rewardVideoAdInfo.setCurrentPosition(0);
            return rewardVideoAdInfo.getAdList().get(0);
        }
        AdConfigInfo info = rewardVideoAdInfo.getAdList().get(tempCurrent);
        tempCurrent++;
        rewardVideoAdInfo.setCurrentPosition(tempCurrent);
        return info;
    }

    /**
     * 获取红包激励视频广告
     *
     * @return
     */
    public AdConfigInfo getRedBagVideoAdInfo() {
        if (redBagVideoAdInfo == null || rewardVideoAdInfo.getAdList() == null) {
            return null;
        }
        int tempCurrent = redBagVideoAdInfo.getCurrentPosition();
        if (tempCurrent >= redBagVideoAdInfo.getAdList().size() - 1) {
            redBagVideoAdInfo.setCurrentPosition(0);
            return redBagVideoAdInfo.getAdList().get(0);
        }
        AdConfigInfo info = redBagVideoAdInfo.getAdList().get(tempCurrent);
        tempCurrent++;
        redBagVideoAdInfo.setCurrentPosition(tempCurrent);
        return info;
    }

    /**
     * 本地常驻红包广告
     *
     * @return
     */
    public AdConfigInfo getLocalRedPackageAdInfo() {
        if (localRedPackageAdInfo == null || localRedPackageAdInfo.getAdList() == null) {
            return null;
        }
        int tempCurrent = localRedPackageAdInfo.getCurrentPosition();
        if (tempCurrent >= localRedPackageAdInfo.getAdList().size() - 1) {
            localRedPackageAdInfo.setCurrentPosition(0);
            return localRedPackageAdInfo.getAdList().get(0);
        }
        AdConfigInfo info = localRedPackageAdInfo.getAdList().get(tempCurrent);
        tempCurrent++;
        localRedPackageAdInfo.setCurrentPosition(tempCurrent);
        return info;
    }

    /**
     * 插屏广告
     *
     * @return
     */
    public AdConfigInfo getInsertAdInfo() {
        if (insertAdInfo == null || insertAdInfo.getAdList() == null) {
            return null;
        }
        int tempCurrent = insertAdInfo.getCurrentPosition();
        if (tempCurrent >= insertAdInfo.getAdList().size() - 1) {
            insertAdInfo.setCurrentPosition(0);
            return insertAdInfo.getAdList().get(0);
        }
        AdConfigInfo info = insertAdInfo.getAdList().get(tempCurrent);
        tempCurrent++;
        insertAdInfo.setCurrentPosition(tempCurrent);
        return info;
    }

    /**
     * 获取红包奖励后的广告实体
     *
     * @return 广告实体
     */
    public AdConfigInfo getRedPackageRewardAdInfo() {
        if (getRedPackageAdInfo == null || getRedPackageAdInfo.getAdList() == null) {
            return null;
        }
        int tempCurrent = getRedPackageAdInfo.getCurrentPosition();
        if (tempCurrent >= getRedPackageAdInfo.getAdList().size() - 1) {
            getRedPackageAdInfo.setCurrentPosition(0);
            return getRedPackageAdInfo.getAdList().get(0);
        }
        AdConfigInfo info = getRedPackageAdInfo.getAdList().get(tempCurrent);
        tempCurrent++;
        getRedPackageAdInfo.setCurrentPosition(tempCurrent);
        return info;
    }


    /**
     * 获取广点通appID
     *
     * @return
     */
    public String gdtAppID() {
        return gdtAppID;
    }


    /**
     * 展示游戏页面
     *
     * @param context
     */
    public void showGamePage(Context context) {
        if (context == null) {
            throw new NullPointerException("");
        }
        context.startActivity(new Intent(context, SdkMainActivity.class));

    }

    /**
     * 初始化广点通广告
     */
    private void initGDTsdk() {
        // 通过调用此方法初始化 SDK。如果需要在多个进程拉取广告，每个进程都需要初始化 SDK。
//        GDTADManager.getInstance().initWith(context, "1110313053");
        if (bannerAdInfo != null || bannerAdInfo.getAdList() != null) {

            GDTADManager.getInstance().initWith(context, gdtAppID);
            // 在调用TBS初始化、创建WebView之前进行如下配置
            HashMap map = new HashMap();
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
            QbSdk.initTbsSettings(map);
        }
    }

    /**
     * 初始化其他广告
     */
    private void initOtherAD(Activity context) {
        if (bannerAdInfo == null) {
            return;
        }
        WindAds ads = WindAds.sharedAds();

        //useMediation:true代表使用聚合服务;false:代表单接SigMob
//        ads.startWithOptions(context, new WindAdOptions("9390", "fe06847d3729d1f5", true));
        ads.startWithOptions(context, new WindAdOptions(otherAppID, otherAppKey, false));

        //主动READ_PHONE_STATE，WRITE_EXTERNAL_STORAGE，ACCESS_FINE_LOCATION 权限授权请求
        WindAds.requestPermission(context);
        getIMEI(context);
    }


    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        try {
            String deviceUniqueIdentifier = null;
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) return null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deviceUniqueIdentifier = tm.getImei();

                if (TextUtils.isEmpty(deviceUniqueIdentifier)) {
                    try {
                        return tm.getDeviceId();
                    } catch (Throwable ignored) {
                        ignored.printStackTrace();
                    }

                    return tm.getMeid();
                }
            } else {
                deviceUniqueIdentifier = tm.getDeviceId();
            }
            if (deviceUniqueIdentifier != null) {
                return deviceUniqueIdentifier;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 初始化统计相关sdk
     *
     * @param context
     */
    private void initTalkingData(Context context) {
//        JLibrary.InitEntry(this); //移动安全联盟统一SDK初始化
        int code = MdidSdkHelper.InitSdk(context, true, new IIdentifierListener() {
            @Override
            public void OnSupport(boolean b, IdSupplier idSupplier) {
                //回调,打印是否支持
                getInstance().idSupplier = idSupplier;
                Log.e("打印是否支持", b + "**" + idSupplier.getOAID());
            }
        });
        Log.e("打印安全联盟初始化code", "==>" + code);


        TCAgent.LOG_ON = true;
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。 B51495A275C94AD19FB1479D24F1685E
        TCAgent.init(context, "19C4FD16A04744438F1E6F42A7F28A92", channel);
        // 如果已经在AndroidManifest.xml配置了App ID和渠道ID，调用TCAgent.init(this)即可；或与AndroidManifest.xml中的对应参数保持一致。
        TCAgent.setReportUncaughtExceptions(true);

    }

    /**
     * 获取唯一标识
     *
     * @return
     */
    public String getOAID() {
        return idSupplier == null ? "" : idSupplier.getOAID();
    }

    /**
     * 原子币兑换成功的回调
     */
    public interface OnCoverFinishListener {
        void onFinish(boolean success);
    }

}
