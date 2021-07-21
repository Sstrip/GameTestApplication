package com.ss.gamesdk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ss.gamesdk.R;
import com.ss.gamesdk.bean.SignDayBean;
import com.ss.gamesdk.bean.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019
 * SignDayTabAdapter
 * <p>
 * Description
 * 签到日期tab
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class SignDayTabAdapter extends RecyclerView.Adapter<SignDayTabAdapter.SignDayTabViewHold> {

    private List<SignDayBean> list = new ArrayList<>();

    private Context context;
    /**
     * 选中状态改变监听
     */
    private OnDayTabSelectedChangeListener listener;

    public SignDayTabAdapter(Context context) {
        this.context = context;
    }


    public void setOnDayTabSelectedChangeListener(OnDayTabSelectedChangeListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public SignDayTabViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SignDayTabViewHold(LayoutInflater.from(context).inflate(R.layout.item_sign_day_tab, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SignDayTabViewHold signDayTabViewHold, int i) {
        signDayTabViewHold.bindData(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<SignDayBean> signDayBeans) {
        this.list.clear();
        this.list.addAll(signDayBeans);
    }

    public class SignDayTabViewHold extends RecyclerView.ViewHolder {
        private SignDayBean dayBean;
        /**
         * 标题
         */
        private TextView tvTitle;
        /**
         * 锁的状态
         */
        private ImageView ivLock;
        /**
         * view
         */
        private RelativeLayout rlDayTab;

        public SignDayTabViewHold(@NonNull View itemView) {
            super(itemView);
            rlDayTab = itemView.findViewById(R.id.rl_day_tab);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivLock = itemView.findViewById(R.id.iv_lock);
            rlDayTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //选中状态改变
                    if (dayBean == null) {
                        return;
                    }
                    if (dayBean.getFinish() == 0) {
                        Toast.makeText(context, "只能签到当天的数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    resetSelectedStatus();
                    dayBean.setSelected(true);
                    notifyDataSetChanged();
                    //todo 需要通知外层切换数据
                    if (null != listener) {
                        listener.onSelectedChange(dayBean.getTasks());
                    }
                }
            });
        }

        void bindData(SignDayBean dayBean) {
            this.dayBean = dayBean;
            tvTitle.setText(dayBean.getTitle());
            if (dayBean.getFinish() == 0) {
                //锁定显示view
                ivLock.setVisibility(View.VISIBLE);
            } else {
                ivLock.setVisibility(View.GONE);
            }
            rlDayTab.setSelected(dayBean.isSelected());
        }

        /**
         * 重置选中的状态
         */
        private void resetSelectedStatus() {
            for (SignDayBean dayBean : list) {
                dayBean.setSelected(false);
            }
        }
    }

    /**
     * 日期选择监听
     */
    public interface OnDayTabSelectedChangeListener {
        void onSelectedChange(List<Task> tasks);
    }

}
