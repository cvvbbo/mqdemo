package com.emiaoqian.app.mq.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.adapter.MessageRecyclerAdapter;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.bean.Newsbean;
import com.emiaoqian.app.mq.other.MyDecoration;
import com.emiaoqian.app.mq.receiver.NetWorkStateReceiver;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.StatebarUtils2;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiong on 2018/6/1.
 */

public class Mymessagefragment extends BaseFragment implements MyCallback.RefreashMessage{


    private RefreshLayout refreshLayout;
    private RecyclerView rv_message;
    private MaterialHeader mMaterialHeader;

    private ArrayList<NewHomeBean1> messagedatas=new ArrayList<>();
    private ArrayList<NewHomeBean1> olddatas=new ArrayList<>();

    private Handler handler=new Handler();

    //记录页数
    private int recordpage=1;



    private MessageRecyclerAdapter adapter;
    private StatebarUtils2 statebar;
    private TextView no_message_note;
    private TextView login_bt;
    private RelativeLayout title_rl;

    NetWorkStateReceiver netWorkStateReceiver;


    @Override
    public int getlayout() {
        return R.layout.new_my_message_view;
    }

    @Override
    public void initialize() {

        //沉浸式状态栏 6.6
        statebar = new StatebarUtils2(this);
        if (Build.VERSION.SDK_INT<21){
            title_rl= (RelativeLayout) view.findViewById(R.id.title_rl);
            lessApi19NeedStatarbarHeight(title_rl);
        }
        LoginFragment.setRefreashMessageListener(this);
        login_bt = (TextView) view.findViewById(R.id.login_bt);
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().
                        beginTransaction().
                        addToBackStack(null).
                        replace(R.id.rlroot, new LoginFragment()).
                        commit();
            }
        });
        no_message_note = (TextView) view.findViewById(R.id.no_message_note);
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
//        if (!user_id.matches("[0-9]+")) {
//            LoginFragment fragment = LoginFragment.newInstance("Mymessagefragment");
//            //判断是否已经登录 6.20
//            getFragmentManager().
//                    beginTransaction().
//                    addToBackStack(null).
//                    replace(R.id.rlroot, fragment).
//                    commit();
//        }else {
//
//            initMessagepager();
//        }

        initMessagepager();
    }

    private void initMessagepager() {
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        GetNetDatas("1");
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        if (user_id.matches("[0-9]+")){
            refreshLayout.autoRefresh();
        }
        rv_message = (RecyclerView) view.findViewById(R.id.rv_message);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        rv_message.setLayoutManager(linearLayout);
        rv_message.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));

        //需要延迟加载数据适配器，不然数据可能会显示不出来 6.4
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //首先是自动下拉刷新 6.6
                adapter = new MessageRecyclerAdapter(messagedatas);
                rv_message.setAdapter(adapter);
                adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       // ToastUtil.showToastCenter(adapter.getItemString(position));
                        ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(adapter.getItemString(position),"nostatabar");
                        getFragmentManager().beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.rlroot,toWebviewFragment)
                                .commit();
                    }
                });
                LogUtil.e(messagedatas + "");
                //判断是否能够上拉加载
//                if (messagedatas.size() < 6) {
//                    refreshLayout.setEnableLoadmore(false);
//                } else {
//                    refreshLayout.setEnableLoadmore(true);
//                }
            }
        }, 300);

        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                //messagedatas.clear();
                GetNetDatas("1");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        //这个一定要设置刷新结束，不然的话下拉刷新会一直在转。
                        refreshlayout.finishRefresh();
                    }
                }, 300);
            }
        });

        //首先判断是否能够上拉加载（感觉以后会存在坑），如果不能上拉加载就不走这个方法
        if (refreshLayout.isEnableLoadmore()) {
            refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    recordpage++;
                    String num = String.valueOf(recordpage);
                    LogUtil.e(num);
                    GetNetDatas(num);
                    refreshLayout.finishLoadmore();
//                    adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            ToastUtil.showToastCenter(adapter.getItemString(position));
//                        }
//                    });
                }
            });


            /**改造脚部（如何当前item的条目少于屏幕显示的个数那就不显示（因为屏幕是经过适配的，所以基本上一个屏幕显示几个条目）6.4）**/
            BallPulseFooter footer = new BallPulseFooter(getActivity());
            //这里的底部上拉加载的（脚部）重新改造了一下，改变了大小  6.3
            //用了转化的工具类，就可以不用dimen文件了
            footer.mBallPulseView.DEFAULT_SIZE = getActivity().getResources().getDimensionPixelOffset(R.dimen.x30);
            footer.setAnimatingColor(getResources().getColor(R.color.bottom_color));
            refreshLayout.setRefreshFooter(footer);
            /***改造脚部**/

            /**改造头部**/
            mMaterialHeader = (MaterialHeader) refreshLayout.getRefreshHeader();
            mMaterialHeader.mProgress.setColorSchemeColors(getResources().getColor(R.color.bottom_color));
            /***改造头部**/

        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        //
        if (hidden==false) {
            statebar.whitestatus(this);
        }
        //登录判断
       // String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");

        normalrefreash();

    }


    String testdata="{\n" +
            "    \"code\": 100000,\n" +
            "    \"msg\": \"操作成功\",\n" +
            "    \"data\": []\n" +
            "}";

    /**
     *
     * 获取网络数据
     *
     */
    private void GetNetDatas(String page) {
        //记得考虑这样的用户 6.4
        //还有一种是什么数据也没有的新用户。（或者是没有登录的时候）

        //单独剥离成一个方法
        long l = System.currentTimeMillis();
        String  str=String.valueOf(l);
        final HashMap<String,String> datas=new HashMap<>();
        datas.put("appv","v1");
        datas.put("page",page);
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        if (user_id.matches("[0-9]+")) {
            datas.put("user_id",user_id);
            login_bt.setVisibility(View.GONE);
        }else {
            //这个是从登录到退出的时候，洗recyclerview的数据 6.29
            olddatas.clear();
            messagedatas.clear();
            if (adapter!=null){
                adapter.notifyDataSetChanged();
            }
            if (no_message_note!=null&&rv_message!=null&&login_bt!=null) {
                no_message_note.setVisibility(View.VISIBLE);
                rv_message.setVisibility(View.GONE);
                login_bt.setVisibility(View.VISIBLE);
            }
        }
        datas.put("timestamp",str);
        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString3(s);
        datas.put("sign",sign);
        httphelper.create().NewdataHomePage2(Constants.CONST_HOST + "/v2/homeIndex/getTidings", datas, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e(s);
                try {
                    no_message_note.setVisibility(View.GONE);
                    rv_message.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("100000")){
                        String data = jsonObject.getString("data");
                        ArrayList<NewHomeBean1> newsbeen = (ArrayList<NewHomeBean1>) GsonUtil.parseJsonToList(data,
                                new TypeToken<List<NewHomeBean1>>() {}.getType());
                        LogUtil.e(newsbeen+"");
                        //这个仅限于上拉加载。
                        if (newsbeen.size()==0&&messagedatas.size()==0){
                            no_message_note.setVisibility(View.VISIBLE);
                            rv_message.setVisibility(View.GONE);
                            return;
                        }
                        if (Integer.valueOf(datas.get("page"))>1&&newsbeen.size()!=0){
                        //if (Integer.valueOf(datas.get("page"))>1){
                            messagedatas.addAll(newsbeen);
                            adapter.notifyDataSetChanged();
                        }else if (Integer.valueOf(datas.get("page"))==1){
                            if (messagedatas.size()!=0&&olddatas.size()!=0){
                                for (int i=0;i<olddatas.size();i++){
                                    messagedatas.set(i,newsbeen.get(i));
                                }
                            }else {
                                messagedatas.addAll(newsbeen);
                                olddatas.addAll(newsbeen);
                                //这里的记录页数只能放在这个初次加载这里！！
                                recordpage=1;
                                //判断是否能够上拉加载
                                if (messagedatas.size() < 6) {
                                    refreshLayout.setEnableLoadmore(false);
                                } else {
                                    refreshLayout.setEnableLoadmore(true);
                                }
                            }

                        }

                    }
                    else if (code.equals("100001")){
                        no_message_note.setVisibility(View.VISIBLE);
                        rv_message.setVisibility(View.GONE);
                    }
                    else {
                        no_message_note.setVisibility(View.VISIBLE);
                        rv_message.setVisibility(View.GONE);
                        ToastUtil.showToastCenter("后台数据错误，正在紧急修复中");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    no_message_note.setVisibility(View.VISIBLE);
                    rv_message.setVisibility(View.GONE);
                    ToastUtil.showToastCenter("后台数据错误，正在紧急修复中");
                }
            }

            @Override
            public void fail(Exception e) {
                no_message_note.setVisibility(View.VISIBLE);
                rv_message.setVisibility(View.GONE);
                if (netWorkStateReceiver == null) {
                    netWorkStateReceiver = new NetWorkStateReceiver();
                    netWorkStateReceiver.setRefreashMessageListener(Mymessagefragment.this);
                }
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                getActivity().registerReceiver(netWorkStateReceiver, filter);
            }
        });
    }

    //从消息界面页面登录之后，刷新消息页面 6.22
    @Override
    public void RefreashMessage() {
       // initMessagepager();
        refreshLayout.autoRefresh();
        normalrefreash();
//        if (netWorkStateReceiver!=null){
//            getActivity().unregisterReceiver(netWorkStateReceiver);
//        }

    }

    //在有网络的情况下，只是没有user_id ,再次刷新数据
    private void normalrefreash() {
        GetNetDatas("1");
        if (refreshLayout.isEnableLoadmore()) {
            refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    recordpage++;
                    String num = String.valueOf(recordpage);
                    LogUtil.e(num);
                    GetNetDatas(num);
                    refreshLayout.finishLoadmore();
//                    adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            ToastUtil.showToastCenter(adapter.getItemString(position));
//                        }
//                    });
                }
            });


            /**改造脚部（如何当前item的条目少于屏幕显示的个数那就不显示（因为屏幕是经过适配的，所以基本上一个屏幕显示几个条目）6.4）**/
            BallPulseFooter footer = new BallPulseFooter(getActivity());
            //这里的底部上拉加载的（脚部）重新改造了一下，改变了大小  6.3
            //用了转化的工具类，就可以不用dimen文件了
            footer.mBallPulseView.DEFAULT_SIZE = getActivity().getResources().getDimensionPixelOffset(R.dimen.x30);
            footer.setAnimatingColor(getResources().getColor(R.color.bottom_color));
            refreshLayout.setRefreshFooter(footer);
            /***改造脚部**/

            /**改造头部**/
            mMaterialHeader = (MaterialHeader) refreshLayout.getRefreshHeader();
            mMaterialHeader.mProgress.setColorSchemeColors(getResources().getColor(R.color.bottom_color));
            /***改造头部**/
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(netWorkStateReceiver);
        super.onDestroy();
    }
}
