package com.emiaoqian.app.mq.adapter;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.bean.Newsbean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiong on 2018/6/21.
 */

public class MeadapterMore extends RecyclerView.Adapter<MeadapterMore.DifferentVolder> {


    ArrayList<NewHomeBean1> datas = new ArrayList<>();
    private Context mcontext;
    private View view;
    private String title;


    public MeadapterMore(ArrayList<NewHomeBean1> datas, Context mcontext,String title) {
        this.datas = datas;
        this.mcontext = mcontext;
        this.title=title;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //获取点击的地址
    public String getItemString(int position){
        return datas.get(position).url;
    }


    @Override
    public DifferentVolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = View.inflate(parent.getContext(), R.layout.me_adapter_view, null);
        return new DifferentVolder(view);

    }

    @Override
    public void onBindViewHolder(final DifferentVolder holder, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        if (position==0){
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(title);
        }else {
            holder.title.setVisibility(View.INVISIBLE);
        }
        holder.tv.setText(datas.get(position).name);
        Picasso.with(MyApplication.mcontext).load(datas.get(position).img).into(holder.im);


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class DifferentVolder extends RecyclerView.ViewHolder {
        @BindView(R.id.im)
        ImageView im;
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.title)
        TextView title;



        DifferentVolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
