package com.emiaoqian.app.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiong on 2018/5/11.
 */

public class RecyclerOtherAppAdapter extends RecyclerView.Adapter<RecyclerOtherAppAdapter.Myholder> {

    private Context context;
    private ArrayList<NewHomeBean1> otherappdatas=new ArrayList<>();

    public RecyclerOtherAppAdapter(Context context, ArrayList<NewHomeBean1> otherappdatas) {
        this.context = context;
        this.otherappdatas = otherappdatas;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_app_view, null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(final Myholder holder, int position) {

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        for (int i=0;i<otherappdatas.size();i++){
            holder.tv.setText(otherappdatas.get(position).name);
            Picasso.with(context).load(otherappdatas.get(position).img).into(holder.im1);
        }
        //holder.tv.setText("秒签发票");


    }

    //获取点击的地址
    public String getItemString(int position){
        return otherappdatas.get(position).url;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return otherappdatas.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        @BindView(R.id.im1)
        ImageView im1;
        @BindView(R.id.tv)
        TextView tv;

        public Myholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
