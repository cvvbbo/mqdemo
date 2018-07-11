package com.emiaoqian.app.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.bean.HistoryDatabean;
import com.emiaoqian.app.mq.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiong on 2018/2/4.
 */

public class HoistoryRecyclerAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private ArrayList<HistoryDatabean> data;

    private ArrayList<String> afterpager;


    public HoistoryRecyclerAdapter1(Context context, ArrayList<HistoryDatabean> data,ArrayList<String> afterpager) {
        this.context = context;
        this.data = data;
        this.afterpager=afterpager;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {

            return TYPE_FOOTER;
        } else {

            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_history, null);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot,  null);
            return new FootViewHolder(view);
        }
        return null;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder= (ItemViewHolder) holder;
            itemViewHolder.fileName.setText(data.get(position).doc_name);
            itemViewHolder.receiveName.setText(data.get(position).receive_name);
            itemViewHolder.sendName.setText(data.get(position).sender_name);

            String doc_status = data.get(position).doc_status;
            switch (doc_status){
                case  "3":
                    itemViewHolder.docstatus.setText("已签收");
                    break;
                case "2":
                    itemViewHolder.docstatus.setText("拒收");
                    break;
                case "0":
                    itemViewHolder.docstatus.setText("草稿");
                    break;
                case "1":
                    itemViewHolder.docstatus.setText("待签收");
                    break;
                default:
                    itemViewHolder.docstatus.setText("草稿");
                    break;

            }


            SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lcc = Long.valueOf(data.get(position).create_time);
            int i = Integer.parseInt(data.get(position).create_time);
            String times = sdr.format(new Date(i * 1000L));
            itemViewHolder.tvTime.setText(times);


            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }


        }else if (holder instanceof FootViewHolder){
            FootViewHolder footViewHolder= (FootViewHolder) holder;
            if (afterpager.get(0).equals("true")) {
                footViewHolder.tv_loading.setVisibility(View.VISIBLE);
                footViewHolder.progressBar.setVisibility(View.VISIBLE);
                footViewHolder.tv_nomore.setVisibility(View.GONE);
            }else {
                footViewHolder.tv_loading.setVisibility(View.GONE);
                footViewHolder.progressBar.setVisibility(View.GONE);
                footViewHolder.tv_nomore.setVisibility(View.VISIBLE);
            }
        }
    }





//    public boolean isVisibility(boolean  isvisibility){
//        return  isvisibility;
//
//    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_name)
        TextView fileName;
        @BindView(R.id.send_name)
        TextView sendName;
        @BindView(R.id.receive_name)
        TextView receiveName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.docstatus)
        TextView docstatus;



        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);

        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_loading)
        TextView tv_loading;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.tv_nomore)
        TextView tv_nomore;

        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }

}
