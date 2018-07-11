package com.emiaoqian.app.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.bean.NewHomeBean1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiong on 2018/5/23.
 */

public class StatusRecyclerAdapter extends RecyclerView.Adapter<StatusRecyclerAdapter.Myholder1> {



    ArrayList<String> num=new ArrayList<>();
    ArrayList<String> title=new ArrayList<>();

    ArrayList<NewHomeBean1> statusdatas=new ArrayList<>();

    public StatusRecyclerAdapter(ArrayList<NewHomeBean1> statusdatas) {
        this.statusdatas=statusdatas;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Myholder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.girdview, null);
        return new Myholder1(view);
    }

    //获取点击的地址
    public String getItemString(int position){
        return statusdatas.get(position).url;
    }

    @Override
    public void onBindViewHolder(final Myholder1 holder, int position) {

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

//       for (int i=0;i<num.size();i++){
//           holder.tvtitle.setText(title.get(position));
//           holder.tvnum.setText(num.get(position));
//       }

        for (int i=0;i<statusdatas.size();i++){
            holder.tvtitle.setText(statusdatas.get(position).name);
            holder.tvnum.setText(String.valueOf(statusdatas.get(position).value));

        }


    }

    @Override
    public int getItemCount() {
        return statusdatas.size();
    }

    class Myholder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.tvnum)
        TextView tvnum;
        @BindView(R.id.tvtitle)
        TextView tvtitle;

        public Myholder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}


