package com.emiaoqian.app.mq.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emiaoqian.app.mq.Interface.ChangeStateBarCallbcak;
import com.emiaoqian.app.mq.Interface.EndLessOnScrollListener;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.adapter.HoistoryRecyclerAdapter1;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.HistoryBean;
import com.emiaoqian.app.mq.bean.HistoryDatabean;
import com.emiaoqian.app.mq.other.MyDecoration;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;

import java.util.ArrayList;
import java.util.List;


public class HistoryDetailFragment extends BaseFragment {

    //没有添加的东西有 下拉刷新的取消


    private RecyclerView recycler;
    private SwipeRefreshLayout history_refresh;

    private Handler handler = new Handler();

    private ArrayList<HistoryDatabean> historyData = new ArrayList<>();

    private ArrayList<HistoryDatabean>  oldhistoryData=new ArrayList<>();

    private ArrayList<HistoryDatabean>  lasthistoryData=new ArrayList<>();

    //private ArrayList<String> aa = new ArrayList<>();

    boolean isLoading;

    private int pagebumber=0;

    private boolean afterpage;

    private ArrayList<String> afterpage1=new ArrayList<>();





    //private HoistoryRecyclerAdapter1 hoistoryRecyclerAdapter = new HoistoryRecyclerAdapter1(MyApplication.mcontext,historyData);
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private HoistoryRecyclerAdapter1 hoistoryRecyclerAdapter1;
    private String num;
    private ImageView returnIm;
    private String user_id;
    private TextView no_more;


    public static HistoryDetailFragment newInstance(String s){
        HistoryDetailFragment fragment=new HistoryDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString("num",s);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public int getlayout() {
        return R.layout.new_fragment;
    }

    //上拉加载

    /**
     *
     * 上拉加载
     *
     */
    public void PullHistoryData(int i){
        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        String s = EncodeBuilder.javaToJSON4(user_id, str, num,String.valueOf(i));
        String sign = EncodeBuilder.newString2(s);

            httphelper.create().saveDeviceNo4(Constants.TEST_QUERY, num, String.valueOf(i), user_id, sign, str, new httphelper.httpcallback() {
                @Override
                public void success(String s) {
                    LogUtil.w("--第2次的数据success数据--" + s);

                    HistoryBean historyBean = GsonUtil.parseJsonToBean(s, HistoryBean.class);
                    String status = historyBean.status;
                    if (!status.equals("1")) {
                        ToastUtil.showToastCenter("服务器连接错误");
                        return;
                    }
                    //data能为0，为0的时候就是没有更多数据了2.7
                    List<HistoryDatabean> data = historyBean.data;
                    if (data.size()==0){
                        LogUtil.e("没有更多数据~");
                        afterpage1.clear();
                        afterpage1.add("false");
                       return;
                    }
                   afterpage1.clear();
                    afterpage1.add("true");
                    historyData.addAll(data);
                }

                @Override
                public void fail(Exception e) {
                    //如果是网络连接失败，要给一个提示！！ 2.7
                    LogUtil.w("--fail失败--" + e);
                }
            });
    }


    //下拉刷新
    //获取第0页的最新数据
    public void DownHistoryData(){

        //添加未登录时候的判断，为了避免空制针

        if(!user_id.matches("[0-9]+")){
            history_refresh.setVisibility(View.GONE);
            no_more.setVisibility(View.VISIBLE);
            return;
        }

        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        String s = EncodeBuilder.javaToJSON4(user_id, str, num,"0");
        String sign = EncodeBuilder.newString2(s);

        httphelper.create().saveDeviceNo4(Constants.TEST_QUERY, num, "0", user_id, sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.w("--success数据--" + s);

                HistoryBean historyBean = GsonUtil.parseJsonToBean(s, HistoryBean.class);
                String status = historyBean.status;
                if (!status.equals("1")) {
                    ToastUtil.showToastCenter("服务器连接错误");
                    return;
                }
                //data能为0，为0的时候就是没有更多数据了2.7
                List<HistoryDatabean> data = historyBean.data;

                //以后可能加一些更加人性化的提示，下拉刷新的时候显示没有更多的数据，或者是本次刷新有多少条新数据 2.7
                if (historyData.size()!=0){
                    for (int i=0;i<oldhistoryData.size();i++){
                        //不能全部清除全部的数据，因为下拉刷新还会添加别的数据
                        historyData.set(i,data.get(i));
                    }
                }else {
                    //1280*720的屏幕是显示9条数据，已这个为参考

                    if (data.size()==0){
                        history_refresh.setVisibility(View.GONE);
                        no_more.setVisibility(View.VISIBLE);
                        return;

                    }

                    if (data.size()>9) {
                        historyData.addAll(data);
                        // historyData.addAll(data);
                        oldhistoryData.addAll(data);
                        afterpage1.add("true");
                        //下面这个愿意应该是小于9但是大于0的时候 3.5
                    }else {
                        historyData.addAll(data);
                        oldhistoryData.addAll(data);
                        afterpage1.add("false");
                    }
                }

//                    historyData.addAll(data);
//                    oldhistoryData.addAll(data);
            }

            @Override
            public void fail(Exception e) {
                LogUtil.w("--fail失败--" + e);
            }
        });
    }




    @Override
    public void initialize() {
        if (getArguments()!=null) {
            if (Build.VERSION.SDK_INT<21){
                LinearLayout appbars= (LinearLayout) view.findViewById(R.id.appbars);
                lessApi19NeedStatarbarHeight(appbars);
            }

            //因为下面的下拉更多会显示暂无更多，所以接口要提前注册 4.4
            Mainactivity.getChildFragmentWebviewCallback(this);

            user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
            num = getArguments().getString("num");
           // DownHistoryData();
            history_refresh = (SwipeRefreshLayout) view.findViewById(R.id.history_refresh);
            no_more = (TextView) view.findViewById(R.id.no_more);
            history_refresh.setColorSchemeResources(R.color.bottom_color);
            //一开始显示等待数据时候的loding界面,这个swiperefreash先初始化，然后在下面取消掉
            history_refresh.post(new Runnable() {
                @Override
                public void run() {
                    history_refresh.setRefreshing(true);
                }
            });
            DownHistoryData();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initview();
                }
            }, 800);

            returnIm = (ImageView) view.findViewById(R.id.returnIm);
            returnIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });


        }



    }

    private void initview() {
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        //这个能解决recyclerview嵌套了scrollview滑动缓慢的问题2.6
        recycler.setNestedScrollingEnabled(false);

        //swipereflash要提前初始化，然后因为页面显示已经是延迟的（为了等数据），然后在这里数据必然是加载好了，就取消就行
       history_refresh.setRefreshing(false);

        history_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                DownHistoryData();
                hoistoryRecyclerAdapter1.notifyDataSetChanged();
                //hoistoryRecyclerAdapter1.notifyItemRangeChanged(0,historyData.size());
                history_refresh.setRefreshing(false);

            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(linearLayoutManager);
        hoistoryRecyclerAdapter1 = new HoistoryRecyclerAdapter1(getActivity(),historyData,afterpage1);
        recycler.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        recycler.setAdapter(hoistoryRecyclerAdapter1);
        recycler.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {

                Log.e("--当前是几页--",currentPage+"");

                PullHistoryData(currentPage);

                //所有的网络获取数据 和 ui获取数据之后的展示ui不能同时进行， 同时进行不能同时展示，
                //一定是获取网络时候，先延迟之后在进行数据的展示 2.8
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hoistoryRecyclerAdapter1.notifyDataSetChanged();
                        hoistoryRecyclerAdapter1.notifyItemRemoved(hoistoryRecyclerAdapter1.getItemCount());
                    }
                }, 300);

            }
        });

        hoistoryRecyclerAdapter1.setOnItemClickListener(new HoistoryRecyclerAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtil.e("第--"+position+"--个--"+Constants.TEST_HISTORY_DATA+historyData.get(position).id);
                ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(Constants.TEST_HISTORY_DATA + historyData.get(position).id
                ,"nostatabar");
               // 这里不能不小心写成add，不然回退栈就是空！贼坑。。
                getFragmentManager().beginTransaction()
                        .replace(R.id.rlroot,toWebviewFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


    }


    public static ChangeStateBarCallbcak changeStateBarCallbcak;
    public static void setChangeStateBarListener(ChangeStateBarCallbcak changeStateBarCallbcak){
        HistoryDetailFragment.changeStateBarCallbcak=changeStateBarCallbcak;

    }


    @Override
    public void onDestroy() {
        changeStateBarCallbcak.changestatebarcallbcak();
        Mainactivity.getChildFragmentWebviewCallback(null);
        super.onDestroy();
    }
}
