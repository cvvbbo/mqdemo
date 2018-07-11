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

public class RecyclerAppAdapter extends RecyclerView.Adapter<RecyclerAppAdapter.Myholder> {

    ArrayList<NewHomeBean1> appdatas=new ArrayList<>();
    Context mcontext;

    public RecyclerAppAdapter(Context context, ArrayList<NewHomeBean1> appdatas) {
        this.appdatas = appdatas;
        this.mcontext=context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_app_view, null);
        return new Myholder(view);
    }


    //获取点击的地址
    public String getItemString(int position){
        return appdatas.get(position).url;
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

        for (int i=0;i<appdatas.size();i++){
            Picasso.with(mcontext).load(appdatas.get(position).img).into(holder.im);
            holder.tv.setText(appdatas.get(position).name);
        }


    }

    @Override
    public int getItemCount() {
        return appdatas.size();
    }

    class Myholder extends RecyclerView.ViewHolder {

        @BindView(R.id.im)
        ImageView im;
        @BindView(R.id.tv)
        TextView tv;

        public Myholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
