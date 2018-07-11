package com.emiaoqian.app.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiong on 2018/6/5.
 */

public class Meadapter extends RecyclerView.Adapter<Meadapter.MeitemHolder> {

    ArrayList<NewHomeBean1> datas = new ArrayList<>();
    private Context mcontext;

    public Meadapter(ArrayList<NewHomeBean1> datas,Context context) {
        this.datas = datas;
        this.mcontext=context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public String getItemString(int position){
        return datas.get(position).url;
    }

    @Override
    public MeitemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_adapter_view, null);
        return new MeitemHolder(view);
    }

    @Override
    public void onBindViewHolder(final MeitemHolder holder, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        holder.title.setVisibility(View.GONE);
        holder.tv.setText(datas.get(position).name);
        Picasso.with(MyApplication.mcontext).load(datas.get(position).img).into(holder.im);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MeitemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.im)
        ImageView im;
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.title)
        TextView title;

        public MeitemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
