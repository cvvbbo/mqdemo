package com.emiaoqian.app.mq.adapter;

/**
 * Created by xiong on 2018/1/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;

/**
 * 二级目录的子目录的数据适配器
 * @author Administrator
 *
 */
public class SubListViewAdapter extends BaseAdapter {

    private String[][] sub_items;
    private Context context;
    private int root_position;
    private LayoutInflater inflater;

    private int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public SubListViewAdapter(Context context, String[][] sub_items,
                              int position) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.sub_items = sub_items;
        this.root_position = position;
    }

    /**
     * 因为每个条目都不同，所以每个一级菜单对应的位置也是不同的 1.18
     *
     *
     * @return
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sub_items[root_position].length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return sub_items[root_position][position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.root_listview_item,
                    parent, false);
            holder.item_text = (TextView) convertView
                    .findViewById(R.id.item_name_text);

            holder.item_layout = (LinearLayout)convertView.findViewById(R.id.root_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(selectedPosition == position){
//          Drawable item_bg = new ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(parent.getResources().getColor(R.color.popselectcolor));
        }else{
//          Drawable item_bg = new ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.item_text.setText(sub_items[root_position][position]);
        return convertView;
    }
    class ViewHolder{
        TextView item_text;
        LinearLayout item_layout;
    }

}