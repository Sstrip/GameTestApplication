package com.ss.gamesdk.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.sigmob.windad.WindAds;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAd;
import com.ss.gamesdk.R;
import com.ss.gamesdk.base.GameSdk;
import com.ss.gamesdk.bean.AdConfigInfo;
import com.ss.gamesdk.bean.AdRewardVideoInfo;
import com.ss.gamesdk.bean.ApiResultData;
import com.ss.gamesdk.bean.RewardInfo;
import com.ss.gamesdk.bean.Task;
import com.ss.gamesdk.bean.UserInfo;
import com.ss.gamesdk.http.NetApi;
import com.ss.gamesdk.http.NetUtil;
import com.ss.gamesdk.utils.AdUtils;
import com.ss.gamesdk.utils.DensityUtil;
import com.ss.gamesdk.utils.EventUtil;
import com.ss.gamesdk.utils.RewardVideoUtils;
import com.ss.gamesdk.utils.UserTimeUtil;
import com.ss.gamesdk.weidgt.CoinConvertDialog;
import com.ss.gamesdk.weidgt.CompoundPropTipsDialog;
import com.ss.gamesdk.weidgt.DoubleHitRedPackageDialog;
import com.ss.gamesdk.weidgt.ExitTipsDialog;
import com.ss.gamesdk.weidgt.FunctionClickDialog;
import com.ss.gamesdk.weidgt.GameLevelFinishDialog;
import com.ss.gamesdk.weidgt.GameOverDialog;
import com.ss.gamesdk.weidgt.GrowTaskDialog;
import com.ss.gamesdk.weidgt.InsertAdDialog;
import com.ss.gamesdk.weidgt.SignDialog;
import com.ss.gamesdk.weidgt.TreasureBoxDialog;
import com.ss.gamesdk.weidgt.UserLeadDialog;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Copyright (C) 2019
 * SdkMainActivity
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/5/1, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 * 主页面
 */
public class SdkMainActivity extends AppCompatActivity {
    /**
     * 动画开启间隔
     */
    private static final long TIME_ANIM_SPACE = 3 * 60 * 1000;
//    private static final long TIME_ANIM_SPACE = 5 * 1000;


    /**
     * 退出页面的次数
     */
    private int exitNum = 0;
    /**
     * 页面web
     */
    private WebView webView;
    /**
     * 底部常驻banner广告
     */
    private RelativeLayout rlBottomAd;
    /**
     * 奖励按钮
     */
    private ImageView ivAward;
    /**
     * 浮动红包按钮
     */
    private ImageView ivFloatBag;
    /**
     * 访问链接
     */
    private String url;
    /**
     * 动画
     */
    private ValueAnimator valueAnimator;
    /**
     * 奖励提示view
     */
    private ImageView ivTips;
    /**
     * 页面关闭按钮
     */
    private ImageView ivClose;
    /**
     * 标题
     */
    private TextView tvTitle;
    /**
     * 游戏合成了多少水果
     */
    private TextView tvCoin;
    /**
     * 奖励用户金币数
     */
    private TextView tvRedBag;
    /**
     * 红包容器
     */
    private RelativeLayout rlRedBag;
    /**
     * 金币容器
     */
    private RelativeLayout rlCoin;

    /**
     * 常驻红包
     */
    private ImageView ivLocalRedPackage;
    /**
     * 签到
     */
    private ImageView ivSign;
    /**
     * 兑换
     */
    private ImageView ivConvert;
    /**
     * 本地红包是否可以点击标识
     */
    private boolean canClickLocalRedPackage = true;
    /**
     * 当前的激励视频广告索引位置
     * 仅限于h5调用原生的激励视频广告
     */
    private int currentIndexRewardAd = 0;
    /**
     * 宝箱的激励视频广告索引
     */
    private int currentBoxRewardAd = 0;
    /**
     * 道具的数量索引
     */
    private int currentPropAd = 0;
    /**
     * 本地红包倒计时
     */
    private TextView tvLocalRedPackageCutDown;

    /**
     * 本地红包倒计时
     */
    private long localCutDownTime = -1;
    /**
     * 获取到金币的gif
     */
    private ImageView ivGifRewardCoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //主动READ_PHONE_STATE，WRITE_EXTERNAL_STORAGE，ACCESS_FINE_LOCATION 权限授权请求
        WindAds.requestPermission(this);
        setContentView(R.layout.activity_sdk_main);
        rlRedBag = findViewById(R.id.rl_red_bag);
        rlCoin = findViewById(R.id.rl_coin);

        webView = findViewById(R.id.wb);
        rlBottomAd = findViewById(R.id.rl_ad_bottom);
        ivAward = findViewById(R.id.iv_award);
        ivFloatBag = findViewById(R.id.iv_float_red);
        ivTips = findViewById(R.id.iv_tips);
        ivClose = findViewById(R.id.iv_close);
        tvTitle = findViewById(R.id.tv_title);
        tvRedBag = findViewById(R.id.tv_redbag_count);
        tvCoin = findViewById(R.id.tv_coin_count);
        ivSign = findViewById(R.id.iv_sign_in);
        ivLocalRedPackage = findViewById(R.id.iv_red_packager);
        ivConvert = findViewById(R.id.iv_convert);
        tvLocalRedPackageCutDown = findViewById(R.id.tv_cut_down_time);
        ivGifRewardCoin = findViewById(R.id.iv_reward_coin);

        Glide.with(this).asGif().load(R.drawable.icon_red_package).into(ivLocalRedPackage);
        Glide.with(this).asGif().load(R.drawable.icon_sign_in).into(ivSign);
        Glide.with(this).asGif().load(R.drawable.icon_main_award).into(ivAward);


        setWebView();
        url = "http://game.szmingtuo.com/dxgsdktest/";
        syncCookie(this, url);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(this, "gamesdk");

//        AdUtils.getInstance(this).showOtherRewardVideoAD(this, "ebb4cd6a0bd", true);


        ivAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击奖励按钮
                showLocalInsertAd();
                GrowTaskDialog dialog = new GrowTaskDialog(SdkMainActivity.this);
                dialog.showGrowTaskDialog(GameSdk.getInstance().getGrowTasks());
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //弹窗结束，需要重新校验任务的完成状态
                        checkUserGrowingTaskStatus();
                    }
                });
            }
        });
        UserTimeUtil.getInstance().initUserTimer(this);
        UserTimeUtil.getInstance().startTimer();

        //插屏广告
//        AdUtils.getInstance(this).loadGdtInsertAd(this,"6061283307462152");

        showBannerAd();

        Glide.with(this).asGif().load(R.mipmap.red_bag).into(ivFloatBag);

        UserTimeUtil.getInstance().addTimeCallBack(TIME_ANIM_SPACE, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                //间隔10s回调一次
                //开启红包的动画
                if (GameSdk.getInstance().getRedBagVideoAdInfo() != null) {
                    ivFloatBag.setVisibility(View.VISIBLE);
                    startFloatAnim();
                }
            }
        });

        ivFloatBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击红包
                EventUtil.get().addEvent("点击「浮动gif红包点我零钱」按钮");
                showRewardVideoAd(GameSdk.getInstance().getRedBagVideoAdInfo(), false);
            }
        });

        UserTimeUtil.getInstance().addTimeCallBack(1000 * 60, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                //间隔1s回调
                checkUserGrowingTaskStatus();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击关闭页面
                onBackPressed(true);
                EventUtil.get().addEvent("点击「左上角退出」按钮");
            }
        });
        NetApi.getUserInfo(GameSdk.getInstance().getChannel(), GameSdk.getInstance().getUserKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResultData<UserInfo>>() {
                    @Override
                    public void call(ApiResultData<UserInfo> userInfoApiResultData) {
                        if (userInfoApiResultData.data != null) {
                            long coin = userInfoApiResultData.data.getRemain();
                            tvRedBag.setText(String.valueOf(coin));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //异常

                    }
                });

        ivSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocalInsertAd();
                EventUtil.get().addEvent("点击「签到」按钮");
                SignDialog signDialog = new SignDialog(SdkMainActivity.this);
                signDialog.show();
            }
        });

        ivLocalRedPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClickLocalRedPackage) {
                    canClickLocalRedPackage = false;
                    EventUtil.get().addEvent("点击「红包」按钮");
                    showRewardVideoAd(GameSdk.getInstance().getLocalRedPackageAdInfo(), true);
                } else {
                    Toast.makeText(SdkMainActivity.this, "红包倒计时中,请等待倒计时结束", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocalInsertAd();
                EventUtil.get().addEvent("点击「兑换」按钮");
                CoinConvertDialog dialog = new CoinConvertDialog(SdkMainActivity.this);
                dialog.show();
            }
        });
        //计时两分钟一次调起插屏广告
        cutDownShowInsertAd();
        EventUtil.get().addEvent("启动sdk");
    }

    /**
     * 展示金币gif
     */
    private void showGetRewardCoinGif() {
        ivGifRewardCoin.setVisibility(View.VISIBLE);
        Glide.with(this).asGif().load(R.drawable.get_reward_coin).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    resource.setLoopCount(1);//只播放一次
                }
                return false;
            }
        }).into(ivGifRewardCoin);
    }


    /**
     * 展示激励视频广告
     *
     * @param configInfo
     * @param rewardCoin 是否需要领取奖励
     */
    private void showRewardVideoAd(AdConfigInfo configInfo, boolean rewardCoin) {
        if (configInfo == null) {
            return;
        }
        if (AdConfigInfo.PLAT_GDT.equals(configInfo.getChannel())) {
            //广点通广告
            AdUtils.getInstance(SdkMainActivity.this).loadRewardVideoAD(configInfo.getAdPlatformInfo().getYlhAppid(),
                    configInfo.getBundleid(), new RewardVideoUtils.OnVideoAdListener() {
                        @Override
                        public void onVideoFinish() {

                        }

                        @Override
                        public void onVideoClose() {
                            //todo 获取奖励
                            if (rewardCoin) {
                                getAward("61");
                            }
                        }

                        @Override
                        public void onVideoLoaded(Object ad) {

                        }
                    });
        } else if (AdConfigInfo.PLAT_SGM.equals(configInfo.getChannel())) {
            //其他广告平台
            AdUtils.getInstance(SdkMainActivity.this).loadOtherRewardVideoAD(SdkMainActivity.this,
                    configInfo.getBundleid(), new RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd>() {
                        @Override
                        public void onVideoFinish() {

                        }

                        @Override
                        public void onVideoClose() {
                            //todo 获取奖励
                            if (rewardCoin) {
                                getAward("61");
                            }
                        }

                        @Override
                        public void onVideoLoaded(WindRewardedVideoAd ad) {
                            AdUtils.getInstance(SdkMainActivity.this).showOtherRewardVideoAd(SdkMainActivity.this);
                        }
                    });
        }
    }


    /**
     * 开始本地红包倒计时
     */
    private void startLocalRedPackageCutDownTime() {
        if (canClickLocalRedPackage) {
            return;
        }
        localCutDownTime = Integer.parseInt(GameSdk.getInstance().getLocalRedPackageAdInfo().getShieldAdTime());
        UserTimeUtil.getInstance().addTimeCallBack(1000, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                //每秒调用
                if (localCutDownTime == -1) {
                    return;
                }
                if (localCutDownTime == 0) {
                    //倒计时结束需要隐藏掉倒计时
                    tvLocalRedPackageCutDown.setVisibility(View.GONE);
                    //为0时才可以点击
                    canClickLocalRedPackage = true;
                } else {
                    // 显示倒计时
                    tvLocalRedPackageCutDown.setVisibility(View.VISIBLE);
                    localCutDownTime--;
                    int miniter = (int) (localCutDownTime / 60);
                    int second = (int) (localCutDownTime % 60);
                    String cutDownTime = (miniter >= 10 ? String.valueOf(miniter) : "0" + miniter) + ":" + (second >= 10 ? String.valueOf(second) : "0" + second);
                    tvLocalRedPackageCutDown.setText(cutDownTime);
                }
            }
        });
    }


    /**
     * 倒计时显示插屏广告
     */
    private void cutDownShowInsertAd() {
        UserTimeUtil.getInstance().addTimeCallBack(1000 * 60 * 2, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                //两分钟展示一次插屏广告
                showLocalInsertAd();
            }
        });
    }

    /**
     * 展示插屏广告
     */
    private void showLocalInsertAd() {
        if (GameSdk.getInstance().getInsertAdInfo() == null) {
            return;
        }
        AdUtils.getInstance(SdkMainActivity.this).loadGdtInsertAd(SdkMainActivity.this, GameSdk.getInstance().getInsertAdInfo().getBundleid());
    }


    /**
     * 领取奖励
     */
    private void getAward(String taskCode) {
        if (!TextUtils.isEmpty(taskCode)) {
            NetApi.getReword(GameSdk.getInstance().getChannel(),
                    GameSdk.getInstance().getUserKey(),
                    taskCode,
                    false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ApiResultData<RewardInfo>>() {
                        @Override
                        public void call(ApiResultData<RewardInfo> stringApiResultData) {
                            if (stringApiResultData.status == 100) {
                                //奖励领取
                                Toast.makeText(SdkMainActivity.this, stringApiResultData.msg, Toast.LENGTH_SHORT).show();
                                //todo  重置倒计时
//                                localCutDownTime = Integer.parseInt(GameSdk.getInstance().getLocalRedPackageAdInfo().getShieldAdTime());
                                startLocalRedPackageCutDownTime();
                                showGetRewardCoinGif();
                            } else {
                                Toast.makeText(SdkMainActivity.this, stringApiResultData.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(SdkMainActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    /***
     * 校验用户的成长任务的状态
     */
    private void checkUserGrowingTaskStatus() {
        List<Task> list = GameSdk.getInstance().getGrowTasks();
        for (Task task : list) {
            //校验用户时长是否已经满足大于任务时长
            if (!"2".equals(task.getFinish()) && task.getOperationTime() * 60 * 1000 < UserTimeUtil.getInstance().getUserTimer()) {
                //未领取任务奖励，并且当前的用户时长已经大于任务时长
                if (ivAward.isShown()) {
                    ivTips.setVisibility(View.VISIBLE);
                }
                return;
            }
        }
        ivTips.setVisibility(View.GONE);
    }


    /**
     * 开启动画
     */
    private void startFloatAnim() {
        if (valueAnimator == null) {
            float total = 1000.0F;
            long totalTime = 20000;
            valueAnimator = ValueAnimator.ofFloat(total);
            valueAnimator.setDuration(totalTime);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    int screenWidth = DensityUtil.getScreenWidth(SdkMainActivity.this) - DensityUtil.dip2px(SdkMainActivity.this, 20);
                    float translateX;
                    int count = (int) (value / 1000);
                    if (count % 2 == 0) {
                        //从左到右移动
                        translateX = (value % 1000 * (screenWidth - DensityUtil.dip2px(SdkMainActivity.this, 50))) / 1000.0F;
                    } else {
                        //从右到左移动
                        translateX = screenWidth - ((value % 1000 * screenWidth) / 1000.0F) - DensityUtil.dip2px(SdkMainActivity.this, 50);
                        if (translateX <= 0) {
                            translateX = 0;
                        }
                    }
                    ivFloatBag.setTranslationX(translateX);
                    if (value == total) {
                        //动画结束
                        ivFloatBag.setTranslationX(0);
                        ivFloatBag.setVisibility(View.GONE);
                    }
                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());
        }
        if (valueAnimator.isRunning()) {
            return;
        }
        valueAnimator.start();
    }


    /**
     * 展示banner广告
     */
    private void showBannerAd() {
        AdConfigInfo info = GameSdk.getInstance().getBannerAdInfo();
        if (info == null) {
            rlBottomAd.setVisibility(View.GONE);
            return;
        } else {
            rlBottomAd.setVisibility(View.VISIBLE);
        }
        //加载banner广告
        if (AdConfigInfo.PLAT_GDT.equals(info.getChannel())) {
            //广点通banner广告
            AdUtils.getInstance(this).loadBannerAd(this, info.getBundleid(), rlBottomAd);
        } else if (AdConfigInfo.PLAT_SGM.equals(info.getChannel())) {
            //其他广告平台

        }
    }

    /**
     * 展示合成水果数量
     */
    @JavascriptInterface
    public void showFruitsCount(int count) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                tvCoin.setText(String.valueOf(count));
            }
        });
    }

    /**
     * 展示奖励按钮
     */
    @JavascriptInterface
    public void showRewardBtn() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ivAward.setVisibility(View.VISIBLE);
                rlRedBag.setVisibility(View.VISIBLE);
                rlCoin.setVisibility(View.VISIBLE);
                ivSign.setVisibility(View.VISIBLE);
                ivLocalRedPackage.setVisibility(View.VISIBLE);
                ivConvert.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 设置页面标题
     *
     * @param title
     */
    @JavascriptInterface
    public void setTitle(String title) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText(title);
            }
        });

    }

    /**
     * 展示激励视频广告
     *
     * @param adInfo 广告的gson串
     */
    @JavascriptInterface
    public void showRewardVideo(String adInfo, String functionName) {
        Log.e("打印调起激励视频广告", "***" + adInfo);
        List<AdRewardVideoInfo> tempInfos = new Gson().fromJson(adInfo, new TypeToken<List<AdRewardVideoInfo>>() {
        }.getType());
        AdRewardVideoInfo info = tempInfos.get(currentIndexRewardAd);
        if (currentIndexRewardAd >= tempInfos.size() - 1) {
            currentIndexRewardAd = 0;
        } else {
            currentIndexRewardAd++;
        }
        int platem = Integer.parseInt(info.getPlatform());
        String adID = info.getAdID();
        if (platem == 0) {
            //广点通
            AdUtils.getInstance(this).loadRewardVideoAD(GameSdk.getInstance().gdtAppID(), adID, new RewardVideoUtils.OnVideoAdListener() {
                @Override
                public void onVideoFinish() {

                }

                @Override
                public void onVideoClose() {
                    if (TextUtils.isEmpty(functionName)) {
                        return;
                    }
                    webView.evaluateJavascript("javascript:window.sGameFunction.useProp(\"" + functionName + "\")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //获取返回值，如果存在
                            Log.e("打印激励视频调用", "**");
                        }
                    });
                }

                @Override
                public void onVideoLoaded(Object ad) {
                    if (ad != null) {
                        ((ExpressRewardVideoAD) ad).showAD(null);
                    }
                }
            });
        } else {
            //其他广告
            AdUtils.getInstance(this).loadOtherRewardVideoAD(this, adID, new RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd>() {
                @Override
                public void onVideoFinish() {

                }

                @Override
                public void onVideoClose() {
                    if (TextUtils.isEmpty(functionName)) {
                        return;
                    }
                    webView.evaluateJavascript("javascript:window.sGameFunction.useProp(\"" + functionName + "\")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //获取返回值，如果存在
                            Log.e("打印激励视频调用", "**");
                        }
                    });
                }

                @Override
                public void onVideoLoaded(WindRewardedVideoAd ad) {
                    AdUtils.getInstance(SdkMainActivity.this).showOtherRewardVideoAd(SdkMainActivity.this);
                }
            });
        }
    }

    /**
     * 展示道具弹窗
     *
     * @param propName   道具名称
     * @param propImgUrl 道具的图片url
     * @param adInfo     广告json
     */
    @JavascriptInterface
    public void showPropDialog(String propName, String propImgUrl, String propId, String adInfo) {
        //弹出道具弹窗
        Log.e("打印道具弹窗", "showPropDialog->" + propImgUrl + "--proid->" + propId);

        List<AdRewardVideoInfo> tempInfos = new Gson().fromJson(adInfo, new TypeToken<List<AdRewardVideoInfo>>() {
        }.getType());
        AdRewardVideoInfo info = tempInfos.get(currentPropAd);
        if (currentPropAd >= tempInfos.size() - 1) {
            currentPropAd = 0;
        } else {
            currentPropAd++;
        }
        int platform = Integer.parseInt(info.getPlatform());
        String adID = info.getAdID();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                CompoundPropTipsDialog dialog = new CompoundPropTipsDialog(SdkMainActivity.this);
                dialog.show(propName,propImgUrl, platform, 1, adID);
                dialog.setOnGetPropListener(new CompoundPropTipsDialog.OnGetPropListener() {
                    @Override
                    public void onGetProp() {
                        //获取到道具 todo 调用js交互
                        webView.evaluateJavascript("javascript:window.sGameFunction.addProp(\"" + propId + "\",\"1\")", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //获取返回值，如果存在
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * taskCode 任务红包
     */
    @JavascriptInterface
    public void showRedPackageDialog(String taskCode) {
        //连击水果的红包弹窗
        Log.e("打印连击水果", "showRedPackageDialog");
        //todo 通过拉取渊博那接口获取红包的相关数据 再展示红
        NetApi.getTask(GameSdk.getInstance().getChannel(), GameSdk.getInstance().getUserKey(), taskCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResultData<Task>>() {
                    @Override
                    public void call(ApiResultData<Task> taskApiResultData) {
                        if (taskApiResultData.data != null) {
                            DoubleHitRedPackageDialog packageDialog = new DoubleHitRedPackageDialog(SdkMainActivity.this);
                            int coinCount = taskApiResultData.data.getRewardMax();
                            AdConfigInfo adConfigInfo = taskApiResultData.data.getAdConfig();
                            String platfirm = adConfigInfo.getChannel();
                            // 最大金币数提示语
                            packageDialog.show("最高" + taskApiResultData.data.getRewardMax() + "金币",
                                    String.valueOf(coinCount), taskCode,
                                    Integer.parseInt(platfirm), 1,
                                    adConfigInfo.getBundleid());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    /**
     * 展示宝箱弹窗
     *
     * @param propName       道具名称
     * @param treasureImgUrl 道具的图片url地址
     * @param treasureCount  道具的数量
     * @param adInfo         广告数据实体
     * @param clickAdId      点击激励视频的广告id
     */
    @JavascriptInterface
    public void showTreasureBoxDialog(String propName, String treasureImgUrl, String propId, String treasureCount, String adInfo, String clickAdId) {
        //宝箱弹窗
        Log.e("打印宝箱弹窗", "showTreasureBoxDialog->" + treasureImgUrl + "-proid->" + propId + "-treasureCount->" + treasureCount + "-clickAdId->" + clickAdId);
        List<AdRewardVideoInfo> tempInfos = new Gson().fromJson(adInfo, new TypeToken<List<AdRewardVideoInfo>>() {
        }.getType());
        AdRewardVideoInfo info = tempInfos.get(currentBoxRewardAd);
        if (currentBoxRewardAd >= tempInfos.size() - 1) {
            currentBoxRewardAd = 0;
        } else {
            currentBoxRewardAd++;
        }
        int platform = Integer.parseInt(info.getPlatform());
        String adID = info.getAdID();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                TreasureBoxDialog treasureBoxDialog = new TreasureBoxDialog(SdkMainActivity.this);
                treasureBoxDialog.show(propName, treasureImgUrl, treasureCount, platform, 1, adID, clickAdId);
                treasureBoxDialog.setOnGetPropListener(new CompoundPropTipsDialog.OnGetPropListener() {
                    @Override
                    public void onGetProp() {
                        //获取到道具 通过js回调
                        webView.evaluateJavascript("javascript:window.sGameFunction.addProp(\"" + propId + "\"" + ",\"" + treasureCount + "\")", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //获取返回值，如果存在
                            }
                        });
                    }
                });
            }
        });
    }


    /**
     * 调起插屏广告弹窗
     *
     * @param title 提示文案
     * @param plate 广告平台
     * @param style 广告样式
     * @param adID  广告id
     */
    @JavascriptInterface
    public void showInsertScreenDialog(String title, int plate, int style, String adID) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                InsertAdDialog dialog = new InsertAdDialog(SdkMainActivity.this);
                dialog.showDialog(title, plate, style, adID);
            }
        });
    }

    /**
     * 调起新手引导弹窗
     *
     * @param gifUrl gif的url地址
     */
    @JavascriptInterface
    public void showNewUserLeadDialog(String gifUrl, int plate, int style, String adID) {
        Log.e("打印新手引导", gifUrl);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                UserLeadDialog dialog = new UserLeadDialog(SdkMainActivity.this);
                dialog.showUserLeadDialog(gifUrl, plate, style, adID);
            }
        });
    }

    /**
     * 调起点击功能按钮的弹窗
     *
     * @param title        文案
     * @param plate        广告平台
     * @param style        广告类型
     * @param adID         广告id
     * @param cutDownTime  倒计时时长 单位s
     * @param functionName h5方法名
     */
    @JavascriptInterface
    public void showFunctionClickDialog(String title, int plate, int style, String adID, long cutDownTime, String functionName) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                FunctionClickDialog dialog = new FunctionClickDialog(SdkMainActivity.this);
                dialog.showFunctionClickDialog(title, plate, style, adID, cutDownTime);
                dialog.setOnButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        webView.evaluateJavascript("javascript:window.sGameFunction.useProp(\"" + functionName + "\")", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //获取返回值，如果存在
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 调起含有底部按钮的弹窗
     *
     * @param title       文案
     * @param plate       广告平台
     * @param style       广告类型
     * @param adID        广告id
     * @param cutDownTime 倒计时时长
     * @param btnString   底部按钮文案
     * @param rewardPlate 激励视频平台
     * @param rewardAdID  激励视频广告ID
     * @param appId       激励视频的appID
     * @param taskCode    任务id 用来领取金币奖励
     */
    @JavascriptInterface
    public void showDialogWithBottomButton(String title, int plate, int style, String adID, long cutDownTime,
                                           String btnString, int rewardPlate, String appId, String rewardAdID, String taskCode) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GameLevelFinishDialog dialog = new GameLevelFinishDialog(SdkMainActivity.this);
                dialog.showGameLevelFinishDialog(title, plate, style, adID, btnString, cutDownTime, rewardPlate, appId, rewardAdID, taskCode);
            }
        });
    }

    /**
     * 游戏结束页面的弹窗
     *
     * @param title     顶部文案
     * @param plate     广告平台
     * @param style     广告样式
     * @param adID      广告id
     * @param btnString 底部按钮文案
     */
    @JavascriptInterface
    public void showGameOverDialog(String title, int plate, int style, String adID, String btnString) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GameOverDialog dialog = new GameOverDialog(SdkMainActivity.this);
                dialog.showGameOverDialog(title, plate, style, adID, btnString);
                dialog.setButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo 点击查看分数
                        EventUtil.get().addEvent("点击「游戏结束点击查看分数」按钮");
                        dialog.dismiss();
                        webView.evaluateJavascript("javascript:window.sGameFunction.toEndLayer()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //获取返回值，如果存在
                            }
                        });
                    }
                });
            }
        });
    }


    /**
     * 设置webview
     */
    private void setWebView() {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        /**设置自适应屏幕，两者合用**/
        //将图片调整到适合webview的大小
        setting.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        setting.setLoadWithOverviewMode(true);
        /**缩放操作**/
        // 是否支持画面缩放，默认不支持
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        // 是否显示缩放图标，默认显示
        setting.setDisplayZoomControls(false);
        // 设置网页内容自适应屏幕大小
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        /**设置允许JS弹窗**/
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);

        /**关闭webview中缓存**/
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        /**设置可以访问文件 **/
        setting.setAllowFileAccess(true);
        setting.setAllowFileAccessFromFileURLs(true);
        setting.setAllowUniversalAccessFromFileURLs(true);
    }

    /**
     * 同步cookie
     *
     * @param context
     * @return
     */
    public boolean syncCookie(Context context, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        HashMap<String, String> cookie = new HashMap<>();
        cookie.put("packageName", context.getPackageName());
        cookie.put("channel", GameSdk.getInstance().getChannel());
        cookie.put("oaid", GameSdk.getInstance().getOAID());
        cookie.put("userKey", GameSdk.getInstance().getUserKey());
        for (Map.Entry<String, String> entry : cookie.entrySet()) {
            // 键值对拼接成 value
            String value = entry.getKey() + "=" + entry.getValue();
            cookieManager.setCookie(url, value);
        }
        String newCookie = cookieManager.getCookie(url);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getApplicationContext());
            cookieSyncManager.sync();
        }
        return TextUtils.isEmpty(newCookie) ? false : true;
    }


    public void onBackPressed(boolean isIcon) {
        if (exitNum == 0) {
            //播放激励视频
            exitNum++;
            if (AdConfigInfo.PLAT_GDT.equals(GameSdk.getInstance().getRewardVideoAdInfo().getChannel())) {
                //2为广点通广告
                AdUtils.getInstance(this).loadRewardVideoAD(GameSdk.getInstance().
                                getRewardVideoAdInfo().getAdPlatformInfo().getYlhAppid(),
                        GameSdk.getInstance().getRewardVideoAdInfo().getBundleid(), true);
            } else if (AdConfigInfo.PLAT_SGM.equals(GameSdk.getInstance().getRewardVideoAdInfo().getChannel())) {
                //其他广告平台
                AdUtils.getInstance(this).loadOtherRewardVideoAD(this,
                        GameSdk.getInstance().getRewardVideoAdInfo().getBundleid(),
                        true);
            }
        } else if (exitNum == 1) {
            //弹窗提示
            if (!isFinishing()) {
                exitNum++;
                ExitTipsDialog dialog = new ExitTipsDialog(this);
                dialog.show();
                dialog.setOnClickDialogListener(new ExitTipsDialog.OnClickDialogListener() {
                    @Override
                    public void onClickFinish() {
                        SdkMainActivity.super.onBackPressed();
                    }

                    @Override
                    public void onClickCancel() {
                        dialog.dismiss();
                        exitNum = 0;
                    }
                });
            }
        } else {
            if (!isIcon) {
                EventUtil.get().addEvent("点击「物理键退出」按钮");
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        UserTimeUtil.getInstance().saveUserTime(this);
        super.onDestroy();
    }
}
