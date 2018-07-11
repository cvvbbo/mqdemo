package com.emiaoqian.app.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiong on 2018/5/28.
 */

public class AddMoreSmallAdapter extends RecyclerView.Adapter<AddMoreSmallAdapter.BigHolder> {



    ArrayList<NewHomeBean1> datas=new ArrayList<>();
    private Context mcontext;

    public AddMoreSmallAdapter(Context mcontext,ArrayList<NewHomeBean1> datas) {
        this.datas = datas;
        this.mcontext = mcontext;
    }

    @Override
    public BigHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(parent.getContext(), R.layout.add_app_small_view, null);

        return new BigHolder(view);
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
    public void onBindViewHolder(final BigHolder holder, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        Picasso.with(mcontext).load(datas.get(position).img).into(holder.iv);
        holder.tv.setText(datas.get(position).name);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class BigHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv)
        TextView tv;

        public BigHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
