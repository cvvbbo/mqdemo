package com.emiaoqian.app.mq.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.fragment.WorkFragment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by xiong on 2017/12/27.
 */

public class GirdViewStatusApadter extends BaseAdapter {

    //ArrayList<String> tvtitle=new ArrayList<>();
   // ArrayList<String> waitbeenlist=new ArrayList<>();

    HashMap<String,String> mystatus=new HashMap<>();


    public GirdViewStatusApadter(HashMap<String,String> data) {
        mystatus=data;

    }

    @Override
    public int getCount() {
       // return mystatus.size();

        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //
        View view = View.inflate(parent.getContext(), R.layout.girdview, null);
        TextView textnum= (TextView) view.findViewById(R.id.tvnum);
        TextView texttitle= (TextView) view.findViewById(R.id.tvtitle);
        Iterator iterator = mystatus.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry entry= (Map.Entry) iterator.next();
//            String name= (String) entry.getKey();
//            String value= (String) entry.getValue();
//            textnum.setText(value);
//            texttitle.setText(name);
//        }

        textnum.setText("10");
        texttitle.setText("haha");


        return view;
    }


}
