package com.emiaoqian.app.mq.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;



/**
 * Created by xiong on 2018/2/10.
 *
 * 这个是分享旁边的弹窗，不是最下面的加号弹窗
 */

public class GirdviewMoreAdapter extends BaseAdapter {
    @Override
    public int getCount() {


        //return buttonmore.size();
        return 3;

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
        View view = View.inflate(parent.getContext(), R.layout.excel_more_gird, null);
        TextView pop_tv_more= (TextView) view.findViewById(R.id.pop_tv_more);

       // pop_tv_more.setText(buttonmore.get(position).name);
        pop_tv_more.setText("haah");
        return view;
    }
}
