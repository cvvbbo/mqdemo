package com.emiaoqian.app.mq.fragment;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.app.mq.Interface.ChangeStateBarCallbcak;
import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;

/**
 * Created by xiong on 2018/6/20.
 */

public class SelectStatusFragment extends BaseFragment implements View.OnClickListener{


    private ListView lv;
    private TextView tv;
    private ImageView returnIm;
    private RelativeLayout returnIm_rl;
    private RelativeLayout title_rl;

    @Override
    public int getlayout() {
        return R.layout.select_status_view;
    }

    @Override
    public void initialize() {
        if (Build.VERSION.SDK_INT<21){
            title_rl = (RelativeLayout) view.findViewById(R.id.title_rl);
            lessApi19NeedStatarbarHeight(title_rl);
        }
       Mainactivity.getChildFragmentWebviewCallback(this);
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(new Mylistviewadapter());
        returnIm = (ImageView) view.findViewById(R.id.returnIm);
        returnIm_rl = (RelativeLayout) view.findViewById(R.id.returnIm_rl);
        returnIm.setOnClickListener(this);
        returnIm_rl.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    selectStatus.SelectStatuscallback("express");
                }else if (position==1){
                    selectStatus.SelectStatuscallback("doc");
                }else if (position==2){
                    selectStatus.SelectStatuscallback("invoice");
                }
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.returnIm:
            case R.id.returnIm_rl:

                getFragmentManager().popBackStack();

                break;
        }

    }


    class Mylistviewadapter extends BaseAdapter{

        @Override
        public int getCount() {
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
            View view = View.inflate(getActivity(), R.layout.select_item, null);
            tv = (TextView) view.findViewById(R.id.tv);
            if (position==1){
                tv.setText("电子合同");
            }else if (position==2){
                tv.setText("电子发票");
            }
            return view;
        }
    }


    public static ChangeStateBarCallbcak changeStateBarCallbcak;
    public static void setChangeStateBarListener(ChangeStateBarCallbcak changeStateBarCallbcak){
        SelectStatusFragment.changeStateBarCallbcak=changeStateBarCallbcak;
    }

    public static MyCallback.SelectStatus selectStatus;
    public static void setSelectStatusListener(MyCallback.SelectStatus selectStatus){
        SelectStatusFragment.selectStatus=selectStatus;

    }

    @Override
    public void onDestroy() {
        changeStateBarCallbcak.changestatebarcallbcak();
        Mainactivity.getChildFragmentWebviewCallback(null);
        super.onDestroy();
    }
}
