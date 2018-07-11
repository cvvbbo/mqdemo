package com.emiaoqian.app.mq.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.fragment.WorkFragment2;

import java.util.ArrayList;



/**
 * Created by xiong on 2018/2/10.
 *
 * 这个是分享旁边的弹窗，不是最下面的加号弹窗
 */

public class GirdviewselectAdapter extends BaseAdapter {

    ArrayList<String> titlessize;


    public GirdviewselectAdapter(ArrayList<String> titlesizes) {
        this.titlessize=titlesizes;
    }

    @Override
    public int getCount() {
        return titlessize.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.select_status_gird, null);
        TextView pop_tv_more= (TextView) view.findViewById(R.id.pop_tv_more);

        pop_tv_more.setText(titlessize.get(position));
        return view;
    }
}
