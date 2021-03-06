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
 * ?????????
 */
public class SdkMainActivity extends AppCompatActivity {
    /**
     * ??????????????????
     */
    private static final long TIME_ANIM_SPACE = 3 * 60 * 1000;
//    private static final long TIME_ANIM_SPACE = 5 * 1000;


    /**
     * ?????????????????????
     */
    private int exitNum = 0;
    /**
     * ??????web
     */
    private WebView webView;
    /**
     * ????????????banner??????
     */
    private RelativeLayout rlBottomAd;
    /**
     * ????????????
     */
    private ImageView ivAward;
    /**
     * ??????????????????
     */
    private ImageView ivFloatBag;
    /**
     * ????????????
     */
    private String url;
    /**
     * ??????
     */
    private ValueAnimator valueAnimator;
    /**
     * ????????????view
     */
    private ImageView ivTips;
    /**
     * ??????????????????
     */
    private ImageView ivClose;
    /**
     * ??????
     */
    private TextView tvTitle;
    /**
     * ???????????????????????????
     */
    private TextView tvCoin;
    /**
     * ?????????????????????
     */
    private TextView tvRedBag;
    /**
     * ????????????
     */
    private RelativeLayout rlRedBag;
    /**
     * ????????????
     */
    private RelativeLayout rlCoin;

    /**
     * ????????????
     */
    private ImageView ivLocalRedPackage;
    /**
     * ??????
     */
    private ImageView ivSign;
    /**
     * ??????
     */
    private ImageView ivConvert;
    /**
     * ????????????????????????????????????
     */
    private boolean canClickLocalRedPackage = true;
    /**
     * ???????????????????????????????????????
     * ?????????h5?????????????????????????????????
     */
    private int currentIndexRewardAd = 0;
    /**
     * ?????????????????????????????????
     */
    private int currentBoxRewardAd = 0;
    /**
     * ?????????????????????
     */
    private int currentPropAd = 0;
    /**
     * ?????????????????????
     */
    private TextView tvLocalRedPackageCutDown;

    /**
     * ?????????????????????
     */
    private long localCutDownTime = -1;
    /**
     * ??????????????????gif
     */
    private ImageView ivGifRewardCoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //??????READ_PHONE_STATE???WRITE_EXTERNAL_STORAGE???ACCESS_FINE_LOCATION ??????????????????
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
                //??????????????????
                showLocalInsertAd();
                GrowTaskDialog dialog = new GrowTaskDialog(SdkMainActivity.this);
                dialog.showGrowTaskDialog(GameSdk.getInstance().getGrowTasks());
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //??????????????????????????????????????????????????????
                        checkUserGrowingTaskStatus();
                    }
                });
            }
        });
        UserTimeUtil.getInstance().initUserTimer(this);
        UserTimeUtil.getInstance().startTimer();

        //????????????
//        AdUtils.getInstance(this).loadGdtInsertAd(this,"6061283307462152");

        showBannerAd();

        Glide.with(this).asGif().load(R.mipmap.red_bag).into(ivFloatBag);

        UserTimeUtil.getInstance().addTimeCallBack(TIME_ANIM_SPACE, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                //??????10s????????????
                //?????????????????????
                if (GameSdk.getInstance().getRedBagVideoAdInfo() != null) {
                    ivFloatBag.setVisibility(View.VISIBLE);
                    startFloatAnim();
                }
            }
        });

        ivFloatBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????
                EventUtil.get().addEvent("???????????????gif???????????????????????????");
                showRewardVideoAd(GameSdk.getInstance().getRedBagVideoAdInfo(), false);
            }
        });

        UserTimeUtil.getInstance().addTimeCallBack(1000 * 60, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                //??????1s??????
                checkUserGrowingTaskStatus();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????????????????
                onBackPressed(true);
                EventUtil.get().addEvent("?????????????????????????????????");
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
                        //??????

                    }
                });

        ivSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocalInsertAd();
                EventUtil.get().addEvent("????????????????????????");
                SignDialog signDialog = new SignDialog(SdkMainActivity.this);
                signDialog.show();
            }
        });

        ivLocalRedPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClickLocalRedPackage) {
                    canClickLocalRedPackage = false;
                    EventUtil.get().addEvent("????????????????????????");
                    showRewardVideoAd(GameSdk.getInstance().getLocalRedPackageAdInfo(), true);
                    startLocalRedPackageCutDownTime();
                } else {
                    Toast.makeText(SdkMainActivity.this, "??????????????????,????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocalInsertAd();
                EventUtil.get().addEvent("????????????????????????");
                CoinConvertDialog dialog = new CoinConvertDialog(SdkMainActivity.this);
                dialog.show();
            }
        });
        //???????????????????????????????????????
        cutDownShowInsertAd();
        EventUtil.get().addEvent("??????sdk");
    }

    /**
     * ????????????gif
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
                    resource.setLoopCount(1);//???????????????
                }
                return false;
            }
        }).into(ivGifRewardCoin);
    }


    /**
     * ????????????????????????
     *
     * @param configInfo
     * @param rewardCoin ????????????????????????
     */
    private void showRewardVideoAd(AdConfigInfo configInfo, boolean rewardCoin) {
        if (configInfo == null) {
            return;
        }
        if (AdConfigInfo.PLAT_GDT.equals(configInfo.getChannel())) {
            //???????????????
            AdUtils.getInstance(SdkMainActivity.this).loadRewardVideoAD(configInfo.getAdPlatformInfo().getYlhAppid(),
                    configInfo.getBundleid(), new RewardVideoUtils.OnVideoAdListener() {
                        @Override
                        public void onVideoFinish() {

                        }

                        @Override
                        public void onVideoClose() {
                            //todo ????????????
                            if (rewardCoin) {
                                getAward("61");
                            }
                        }

                        @Override
                        public void onVideoLoaded(Object ad) {

                        }
                    });
        } else if (AdConfigInfo.PLAT_SGM.equals(configInfo.getChannel())) {
            //??????????????????
            AdUtils.getInstance(SdkMainActivity.this).loadOtherRewardVideoAD(SdkMainActivity.this,
                    configInfo.getBundleid(), new RewardVideoUtils.OnVideoAdListener<WindRewardedVideoAd>() {
                        @Override
                        public void onVideoFinish() {

                        }

                        @Override
                        public void onVideoClose() {
                            //todo ????????????
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
     * ???????????????????????????
     */
    private void startLocalRedPackageCutDownTime() {
        if (canClickLocalRedPackage) {
            return;
        }
        localCutDownTime = Integer.parseInt(GameSdk.getInstance().getLocalRedPackageAdInfo().getShieldAdTime());
        UserTimeUtil.getInstance().addTimeCallBack(1000, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                //????????????
                if (localCutDownTime == -1) {
                    return;
                }
                if (localCutDownTime == 0) {
                    //???????????????????????????????????????
                    tvLocalRedPackageCutDown.setVisibility(View.GONE);
                    //???0??????????????????
                    canClickLocalRedPackage = true;
                } else {
                    // ???????????????
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
     * ???????????????????????????
     */
    private void cutDownShowInsertAd() {
        UserTimeUtil.getInstance().addTimeCallBack(1000 * 60 * 2, new UserTimeUtil.TimeCallBack() {
            @Override
            public void onCall() {
                //?????????????????????????????????
                showLocalInsertAd();
            }
        });
    }

    /**
     * ??????????????????
     */
    private void showLocalInsertAd() {
        if (GameSdk.getInstance().getInsertAdInfo() == null) {
            return;
        }
        AdUtils.getInstance(SdkMainActivity.this).loadGdtInsertAd(SdkMainActivity.this, GameSdk.getInstance().getInsertAdInfo().getBundleid());
    }


    /**
     * ????????????
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
                                //????????????
                                Toast.makeText(SdkMainActivity.this, stringApiResultData.msg, Toast.LENGTH_SHORT).show();
                                //todo  ???????????????
//                                localCutDownTime = Integer.parseInt(GameSdk.getInstance().getLocalRedPackageAdInfo().getShieldAdTime());
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
     * ????????????????????????????????????
     */
    private void checkUserGrowingTaskStatus() {
        List<Task> list = GameSdk.getInstance().getGrowTasks();
        for (Task task : list) {
            //??????????????????????????????????????????????????????
            if (!"2".equals(task.getFinish()) && task.getOperationTime() * 60 * 1000 < UserTimeUtil.getInstance().getUserTimer()) {
                //???????????????????????????????????????????????????????????????????????????
                if (ivAward.isShown()) {
                    ivTips.setVisibility(View.VISIBLE);
                }
                return;
            }
        }
        ivTips.setVisibility(View.GONE);
    }


    /**
     * ????????????
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
                        //??????????????????
                        translateX = (value % 1000 * (screenWidth - DensityUtil.dip2px(SdkMainActivity.this, 50))) / 1000.0F;
                    } else {
                        //??????????????????
                        translateX = screenWidth - ((value % 1000 * screenWidth) / 1000.0F) - DensityUtil.dip2px(SdkMainActivity.this, 50);
                        if (translateX <= 0) {
                            translateX = 0;
                        }
                    }
                    ivFloatBag.setTranslationX(translateX);
                    if (value == total) {
                        //????????????
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
     * ??????banner??????
     */
    private void showBannerAd() {
        AdConfigInfo info = GameSdk.getInstance().getBannerAdInfo();
        if (info == null) {
            rlBottomAd.setVisibility(View.GONE);
            return;
        } else {
            rlBottomAd.setVisibility(View.VISIBLE);
        }
        //??????banner??????
        if (AdConfigInfo.PLAT_GDT.equals(info.getChannel())) {
            //?????????banner??????
            AdUtils.getInstance(this).loadBannerAd(this, info.getBundleid(), rlBottomAd);
        } else if (AdConfigInfo.PLAT_SGM.equals(info.getChannel())) {
            //??????????????????

        }
    }

    /**
     * ????????????????????????
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
     * ??????????????????
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
     * ??????????????????
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
     * ????????????????????????
     *
     * @param adInfo ?????????gson???
     */
    @JavascriptInterface
    public void showRewardVideo(String adInfo, String functionName) {
        Log.e("??????????????????????????????", "***" + adInfo);
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
            //?????????
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
                            //??????????????????????????????
                            Log.e("????????????????????????", "**");
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
            //????????????
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
                            //??????????????????????????????
                            Log.e("????????????????????????", "**");
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
     * ??????????????????
     *
     * @param propName   ????????????
     * @param propImgUrl ???????????????url
     * @param adInfo     ??????json
     */
    @JavascriptInterface
    public void showPropDialog(String propName, String propImgUrl, String propId, String adInfo) {
        //??????????????????
        Log.e("??????????????????", "showPropDialog->" + propImgUrl + "--proid->" + propId);

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
                dialog.show(propName, propImgUrl, platform, 1, adID);
                dialog.setOnGetPropListener(new CompoundPropTipsDialog.OnGetPropListener() {
                    @Override
                    public void onGetProp() {
                        //??????????????? todo ??????js??????
                        webView.evaluateJavascript("javascript:window.sGameFunction.addProp(\"" + propId + "\",\"1\")", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //??????????????????????????????
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * taskCode ????????????
     */
    @JavascriptInterface
    public void showRedPackageDialog(String taskCode) {
        //???????????????????????????
        Log.e("??????????????????", "showRedPackageDialog");
        //todo ?????????????????????????????????????????????????????? ????????????
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
                            // ????????????????????????
                            packageDialog.show("??????" + taskApiResultData.data.getRewardMax() + "??????",
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
     * ??????????????????
     *
     * @param propName       ????????????
     * @param treasureImgUrl ???????????????url??????
     * @param treasureCount  ???????????????
     * @param adInfo         ??????????????????
     * @param clickAdId      ???????????????????????????id
     */
    @JavascriptInterface
    public void showTreasureBoxDialog(String propName, String treasureImgUrl, String propId, String treasureCount, String adInfo, String clickAdId) {
        //????????????
        Log.e("??????????????????", "showTreasureBoxDialog->" + treasureImgUrl + "-proid->" + propId + "-treasureCount->" + treasureCount +"adInfo->"+adInfo+ "-clickAdId->" + clickAdId);
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
                        //??????????????? ??????js??????
                        webView.evaluateJavascript("javascript:window.sGameFunction.addProp(\"" + propId + "\"" + ",\"" + treasureCount + "\")", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //??????????????????????????????
                            }
                        });
                    }
                });
            }
        });
    }


    /**
     * ????????????????????????
     *
     * @param title ????????????
     * @param plate ????????????
     * @param style ????????????
     * @param adID  ??????id
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
     * ????????????????????????
     *
     * @param gifUrl gif???url??????
     */
    @JavascriptInterface
    public void showNewUserLeadDialog(String gifUrl, int plate, int style, String adID) {
        Log.e("??????????????????", gifUrl);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                UserLeadDialog dialog = new UserLeadDialog(SdkMainActivity.this);
                dialog.showUserLeadDialog(gifUrl, plate, style, adID);
            }
        });
    }

    /**
     * ?????????????????????????????????
     *
     * @param title        ??????
     * @param plate        ????????????
     * @param style        ????????????
     * @param adID         ??????id
     * @param cutDownTime  ??????????????? ??????s
     * @param functionName h5?????????
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
                                //??????????????????????????????
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * ?????????????????????????????????
     *
     * @param title       ??????
     * @param plate       ????????????
     * @param style       ????????????
     * @param adID        ??????id
     * @param cutDownTime ???????????????
     * @param btnString   ??????????????????
     * @param rewardPlate ??????????????????
     * @param rewardAdID  ??????????????????ID
     * @param appId       ???????????????appID
     * @param taskCode    ??????id ????????????????????????
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
     * ???????????????????????????
     *
     * @param title     ????????????
     * @param plate     ????????????
     * @param style     ????????????
     * @param adID      ??????id
     * @param btnString ??????????????????
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
                        //todo ??????????????????
                        EventUtil.get().addEvent("????????????????????????????????????????????????");
                        dialog.dismiss();
                        webView.evaluateJavascript("javascript:window.sGameFunction.toEndLayer()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //??????????????????????????????
                            }
                        });
                    }
                });
            }
        });
    }


    /**
     * ??????webview
     */
    private void setWebView() {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        /**????????????????????????????????????**/
        //????????????????????????webview?????????
        setting.setUseWideViewPort(true);
        // ????????????????????????
        setting.setLoadWithOverviewMode(true);
        /**????????????**/
        // ??????????????????????????????????????????
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        // ???????????????????????????????????????
        setting.setDisplayZoomControls(false);
        // ???????????????????????????????????????
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        /**????????????JS??????**/
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);

        /**??????webview?????????**/
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        /**???????????????????????? **/
        setting.setAllowFileAccess(true);
        setting.setAllowFileAccessFromFileURLs(true);
        setting.setAllowUniversalAccessFromFileURLs(true);
    }

    /**
     * ??????cookie
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
            // ?????????????????? value
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

    @Override
    public void onBackPressed() {
        onBackPressed(false);
    }

    public void onBackPressed(boolean isIcon) {
        if (exitNum == 0) {
            //??????????????????
            exitNum++;
            if (AdConfigInfo.PLAT_GDT.equals(GameSdk.getInstance().getRewardVideoAdInfo().getChannel())) {
                //2??????????????????
                AdUtils.getInstance(this).loadRewardVideoAD(GameSdk.getInstance().
                                getRewardVideoAdInfo().getAdPlatformInfo().getYlhAppid(),
                        GameSdk.getInstance().getRewardVideoAdInfo().getBundleid(), true);
            } else if (AdConfigInfo.PLAT_SGM.equals(GameSdk.getInstance().getRewardVideoAdInfo().getChannel())) {
                //??????????????????
                AdUtils.getInstance(this).loadOtherRewardVideoAD(this,
                        GameSdk.getInstance().getRewardVideoAdInfo().getBundleid(),
                        true);
            }
        } else if (exitNum == 1) {
            //????????????
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
                EventUtil.get().addEvent("?????????????????????????????????");
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
