package com.emiaoqian.app.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Created by xiong on 2018/6/21.
 */

public class MeadapterMoreDetail extends RecyclerView.Adapter<MeadapterMoreDetail.Deatilsadapter> {


    ArrayList<NewHomeBean1> datas=new ArrayList<>();
    private Context mcontext;

    public MeadapterMoreDetail(ArrayList<NewHomeBean1> datas, Context mcontext) {
        this.datas = datas;
        this.mcontext = mcontext;
    }

    @Override
    public Deatilsadapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.me_adapter_view, null);
        return new Deatilsadapter(view);
    }

    @Override
    public void onBindViewHolder(Deatilsadapter holder, int position) {
        holder.tv.setText(datas.get(position).name);
        Picasso.with(MyApplication.mcontext).load(datas.get(position).img).into(holder.im);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class  Deatilsadapter extends RecyclerView.ViewHolder{
        @BindView(R.id.im)
        ImageView im;
        @BindView(R.id.tv)
        TextView tv;

        public Deatilsadapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
