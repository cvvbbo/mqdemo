package com.emiaoqian.app.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
 * Created by xiong on 2018/6/3.
 * 二次改版的秒签app
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NewHomeBean1> datas = new ArrayList<>();

    public MessageRecyclerAdapter(ArrayList<NewHomeBean1> datas) {
        this.datas = datas;
    }

    private static final int TYPE_TEXT_NUMLESS = 0;
    private static final int TYPE_TEXT_NUMMORE = 1;


    public String getItemString(int position){
        return datas.get(position).url;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_TEXT_NUMLESS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_message_text_less_adapter, null);
            return new TextLessHolder(view);
        } else if (viewType == TYPE_TEXT_NUMMORE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_message_text_more_adapter, null);
            return new TextMoreHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        if (holder instanceof TextLessHolder){
            TextLessHolder textholderless = (TextLessHolder) holder;
            textholderless.textDes.setText(datas.get(position).text);
            textholderless.title1.setText(datas.get(position).title);
            if (!datas.get(position).img.equals("")) {
                Picasso.with(MyApplication.mcontext).load(datas.get(position).img).error(R.drawable.test_im).into(textholderless.headIcon);
            }
            textholderless.time_tv.setText(datas.get(position).create_time);
        }else if (holder instanceof  TextMoreHolder){
            TextMoreHolder moreHolder = (TextMoreHolder) holder;
            moreHolder.textDes.setText(datas.get(position).text);
            moreHolder.title1.setText(datas.get(position).title);
            if (!datas.get(position).img.equals("")) {
                Picasso.with(MyApplication.mcontext).load(datas.get(position).img).error(R.drawable.test_im).into(moreHolder.headIcon);
            }
            moreHolder.time_tv.setText(datas.get(position).create_time);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (datas.size()!=0&&datas.get(position).text.length()>= 20) {
            return TYPE_TEXT_NUMMORE;
        }
        else  {
            return TYPE_TEXT_NUMLESS;
        }

    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    class TextMoreHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.head_icon)
        ImageView headIcon;
        @BindView(R.id.title1)
        TextView title1;
        @BindView(R.id.text_des)
        TextView textDes;
        @BindView(R.id.time_tv)
        TextView time_tv;

        public TextMoreHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class TextLessHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.head_icon)
        ImageView headIcon;
        @BindView(R.id.title1)
        TextView title1;
        @BindView(R.id.text_des)
        TextView textDes;
        @BindView(R.id.time_tv)
        TextView time_tv;

        public TextLessHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }




}



