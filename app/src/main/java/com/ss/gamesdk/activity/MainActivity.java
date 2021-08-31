package com.ss.gamesdk.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ss.gamesdk.R;
import com.ss.gamesdk.base.GameSdk;

/**
 * Copyright (C) 2019
 * MainActivity
 * <p>
 * Description
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/4/19, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class MainActivity extends AppCompatActivity {
    /**
     * oaid
     */
    private TextView tvOaid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questPermission();
        setContentView(R.layout.activity_main);
        tvOaid = findViewById(R.id.tv_oaid);
        //点击跳转测试
        GameSdk.getInstance().init(MainActivity.this, "test", "test");
        GameSdk.getInstance().showGamePage(MainActivity.this);
        finish();
//        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GameSdk.getInstance().showGamePage(MainActivity.this);
//            }
//        });
//        GameSdk.getInstance().setOnCoverFinishListener(new GameSdk.OnCoverFinishListener() {
//            @Override
//            public void onFinish(boolean success) {
//                //回调兑换成功
//            }
//        });
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tvOaid.setText(GameSdk.getInstance().getOAID());
//            }
//        }, 5000);
//        tvOaid.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                //长按复制到粘贴板
//                //获取剪贴板管理器
//                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                // 创建普通字符型ClipData
//                ClipData mClipData = ClipData.newPlainText("Label", GameSdk.getInstance().getOAID());
//                // 将ClipData内容放到系统剪贴板里。
//                cm.setPrimaryClip(mClipData);
//                Toast.makeText(MainActivity.this, "已复制到粘贴板", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

    /**
     * 请求权限
     */
    private void questPermission() {
        String[] permissionString = {Manifest.permission.READ_PHONE_STATE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permissionString, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //权限获取
    }
}
