package com.emiaoqian.app.mq.adapter;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.LogUtil;

import java.util.ArrayList;

//import static com.emiaoqian.app.mq.fragment.WorkFragment1.appiconList;


/**
 * Created by xiong on 2017/12/28.
 */

public class GridViewAppAdapter extends BaseAdapter {

    ArrayList<String> apptitle=new ArrayList<>();
    ArrayList<ImageView> appimage=new ArrayList<>();
    private ImageView iv;
    private int height;

    public GridViewAppAdapter() {
        apptitle.add("秒签快递");
        apptitle.add("实物快递");
        apptitle.add("电子合同");
        apptitle.add("秒签快递");
        apptitle.add("实物快递");
        apptitle.add("电子合同");
        apptitle.add("更多");

    }

    @Override
    public int getCount() {
        //return appiconList.size();
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
        View  view= View.inflate(parent.getContext(), R.layout.grid_app_view,null);
        iv = (ImageView) view.findViewById(R.id.iv);
        TextView tv = (TextView) view.findViewById(R.id.tv);
       // tv.setText(apptitle.get(position));
        //iv.setImageResource(R.mipmap.ic_launcher2);
       // Glide.with(MyApplication.mcontext).load(Constants.IMAGE+appiconList.get(position)).into(iv);

        Glide.with(MyApplication.mcontext).load("https://b-ssl.duitang.com/uploads/item/201412/19/20141219162216_jCHju.jpeg").into(iv);
        iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //判断系统版本是否大于16
                if (Build.VERSION.SDK_INT >= 16) {
                    iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else {
                    iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                int with=iv.getWidth(); // 获取宽度
                // 获取高度
                height = iv.getHeight();

                LogUtil.e("宽--"+with+"--高--"+ height);
            }
        });


        return view;
    }


}
