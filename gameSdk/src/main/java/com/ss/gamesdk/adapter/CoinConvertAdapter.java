package com.ss.gamesdk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ss.gamesdk.R;
import com.ss.gamesdk.bean.ConvertListInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019
 * CoinConvertAdapter
 * <p>
 * Description
 * 兑换原子币列表
 *
 * @author Shaoshuai
 * @version 1.0
 * <p>
 * Ver 1.0, 2021/6/20, Administrator, Create file
 * @emial shaoshuai@staff.hexun.com
 */
public class CoinConvertAdapter extends RecyclerView.Adapter<CoinConvertAdapter.CoinConvertViewHold> {
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 兑换列表数据
     */
    private List<ConvertListInfo> exchangeList = new ArrayList<>();

    public CoinConvertAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CoinConvertViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CoinConvertViewHold(LayoutInflater.from(context).inflate(R.layout.item_coin_convert_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CoinConvertViewHold coinConvertViewHold, int i) {
        coinConvertViewHold.bindData(exchangeList.get(i));
    }

    @Override
    public int getItemCount() {
        return exchangeList.size();
    }

    /**
     * 追加数据
     *
     * @param exchangeList
     */
    public void addAll(List<ConvertListInfo> exchangeList) {
        this.exchangeList.clear();
        this.exchangeList.addAll(exchangeList);
    }

    /**
     * 获取选中的兑换id
     *
     * @return
     */
    public String getCheckItemExchangeId() {
        if (exchangeList.isEmpty()) {
            return "";
        }
        //获取兑换中的数据
        String exchangeId = "";
        for (ConvertListInfo info : exchangeList) {
            if (info.getStatus() == 2) {
                //选中状态
                exchangeId = info.getId() + "";
            }
        }
        return exchangeId;
    }

    public class CoinConvertViewHold extends RecyclerView.ViewHolder {
        /**
         * 数据实体
         */
        private ConvertListInfo info;
        /**
         * 条目选中view
         */
        private TextView cbItem;
        /**
         * 标题
         */
        private TextView tvTitle;


        public CoinConvertViewHold(@NonNull View itemView) {
            super(itemView);
            cbItem = itemView.findViewById(R.id.cb_item);
            tvTitle = itemView.findViewById(R.id.tv_title);
            cbItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    resetCheckedData(position);
                    notifyDataSetChanged();
                }
            });
        }

        void bindData(ConvertListInfo info) {
            this.info = info;
            cbItem.setSelected(info.getStatus() == 2);
            tvTitle.setText(info.getShowMsg());
        }

        /**
         * 重置选中的数据
         *
         * @param position
         */
        private void resetCheckedData(int position) {
            for (int i = 0; i < exchangeList.size(); i++) {
                if (i != position) {
                    exchangeList.get(i).setStatus(1);
                } else {
                    exchangeList.get(i).setStatus(2);
                }
            }
        }
    }
}
