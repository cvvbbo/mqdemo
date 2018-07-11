package com.emiaoqian.app.mq.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.emiaoqian.app.mq.Interface.ChangeStateBarCallbcak;
import com.emiaoqian.app.mq.Interface.LoginCallBack;
import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.adapter.RecyclerAppAdapter;
import com.emiaoqian.app.mq.adapter.RecyclerOtherAppAdapter;
import com.emiaoqian.app.mq.adapter.StatusRecyclerAdapter;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.BannerInfo;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.bean.Newsbean;
import com.emiaoqian.app.mq.bean.RecentBean;
import com.emiaoqian.app.mq.receiver.NetWorkStateReceiver;
import com.emiaoqian.app.mq.utils.CPResourceUtil;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.StatebarUtils2;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.UpDatautils;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.emiaoqian.app.mq.view.UPMarqueeView;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.CaptureActivity;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by xiong on 2017/10/10.
 */


/**
 *
 * 如遇到待我签收那行状态栏点击之后webview有时候要回退好几下才能推出，或者秒签应用里面的webview点击好几下才能推出
 *
 * 请参考 360浏览器或者谷歌浏览器，输入地址，进行在手机端相同操作，360浏览器和谷歌浏览器同样也是点击好几下返回才回到上个界面
 *
 * 2.10
 *
 *
 *
 */
public class WorkFragment2 extends BaseFragment implements View.OnClickListener,
        ChangeStateBarCallbcak,MyCallback.RefreashHomePage,MyCallback.SelectStatus{


    public static ArrayList<String> bannerImageList = new ArrayList<>();

    public static ArrayList<String> bannerClickUrl=new ArrayList<>();

    public static ArrayList<String> appiconList = new ArrayList<>();

    public static ArrayList<String> appurl=new ArrayList<>();

    //这个是中间的状态栏
    public static ArrayList<String> mystatus=new ArrayList<>();

    private ArrayList<NewHomeBean1> bannersize=new ArrayList<>();
    public static HashMap<String, String> excel=new HashMap<>();

    public static ArrayList<String> bannertitle=new ArrayList<>();
    public static ArrayList<String>  apptitle=new ArrayList<>();

    //这个是分享旁边的按钮
    public static ArrayList<BannerInfo>  buttonmore=new ArrayList<>();

    public PopupWindow popupWindow;

    //// TODO: 2018/5/24
    public ArrayList<NewHomeBean1> appdata=new ArrayList<>();
    public ArrayList<NewHomeBean1> otherappdatas=new ArrayList<>();
    public  ArrayList<NewHomeBean1> statusdata=new ArrayList<>();
    public static ArrayList<NewHomeBean1> bigdatas=new ArrayList<>();
    public static ArrayList<NewHomeBean1> smalldatas=new ArrayList<>();
    //// TODO: 2018/5/24


    private ViewPager viewPager;
    private LinearLayout ll_point;

    private int Fig_current=1;

    private int currentposition=0;


    private boolean bannersendmessagefirst=true;


    private ArrayList<ImageView> imageViews = new ArrayList<>();



    private final int[] imageaaa = {
            R.drawable.a,
            R.drawable.a,
            R.drawable.a,
            R.drawable.a
    };

    Handler  handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

             if (msg.what==0) {
                int item = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(item);//这个先写，int item后写
                //这里的发送消息是能够让viewpager自动轮播的关键 2017.1.4
                handler.sendEmptyMessageDelayed(0, 2500);
            }

        }
    };


    NetWorkStateReceiver netWorkStateReceiver;

    boolean iscycler=true;


    String s1="{\n" +
            "\t\"img\": \"app_image\\/1176035105c534247ed57d9d95589b0a.png\",\n" +
            "\t\"data\": [{\n" +
            "\t\t\"title1\": \"1\",\n" +
            "\t\t\"title2\": \"2\",\n" +
            "\t\t\"url\": \"https:\\/\\/www.emiaoqian.com\\/mobile_app\\/news\\/detail\\/id\\/25\"\n" +
            "\t}, {\n" +
            "\t\t\"title1\": \"3\",\n" +
            "\t\t\"title2\": \"4\",\n" +
            "\t\t\"url\": \"https:\\/\\/www.emiaoqian.com\\/mobile_app\\/news\\/detail\\/id\\/34\"\n" +
            "\t}, {\n" +
            "\t\t\"title1\": \"5\",\n" +
            "\t\t\"url\": \"https:\\/\\/www.emiaoqian.com\\/mobile\\/index\\/news\"\n" +
            "\t}, {\n" +
            "\t\t\"title1\": \"6\",\n" +
            "\t\t\"title2\": \"7\",\n" +
            "\t\t\"url\": \"https:\\/\\/www.emiaoqian.com\\/mobile_app\\/news\\/detail\\/id\\/34\"\n" +
            "\t}, {\n" +
            "\t\t\"title1\": \"8\",\n" +
            "\t\t\"url\": \"https:\\/\\/www.emiaoqian.com\\/mobile_app\\/news\\/detail\\/id\\/34\"\n" +
            "\t}, {\n" +
            "\t\t\"title1\": \"9\",\n" +
            "\t\t\"title2\": \"10\",\n" +
            "\t\t\"url\": \"https:\\/\\/www.emiaoqian.com\\/mobile_app\\/news\\/detail\\/id\\/34\"\n" +
            "\t}]\n" +
            "}";



    private TextView searchtv;
    private SwipeRefreshLayout refreshLayout;
    private TextView sendername;
    private TextView usercompany;
    private TextView createtime;
    private TextView receivename;
    private TextView receivecompany;
    private TextView docname;
    private TextView docno;
    private TextView circle;
    private TextView select_page_text;
    private LinearLayout apptitlebar;
    //仿淘宝头条 5.10
    private UPMarqueeView upview1;
    //淘宝轮播图效果，数据填充
    List<NewHomeBean1> miaoqiandata = new ArrayList<>();

    List<NewHomeBean1> newmiaoqiandata=new ArrayList<>();


    //填充淘宝头像的view
    List<View> views = new ArrayList<>();
    private TextView child_tv1;
    private TextView child_tv2;
    private RelativeLayout select_tv_ry;
    private RecyclerView rv_app;
    private RecyclerView ry_other_app;

    private boolean isshowstatus;
    public static ArrayList<String> selecttitle;
    private RecyclerView status_rv;
    private TextView app1_title;
    private TextView app2_title;
    private RecyclerOtherAppAdapter otherappAdapter;
    private RecyclerAppAdapter commomadapter;
    private StatusRecyclerAdapter statusadapter;
    private TextView word_status_tv;
    private TextView work_status_time_tv;
    private RelativeLayout new_work_excle;
    private TextView tv_money;
    private StatebarUtils2 statebar;
    private boolean isfrist=true;
    private RelativeLayout status_bar_ry;
    private RelativeLayout no_work_excle1;
    private ImageView scan_photo;

    private static final int QRCode_Action = 666;

    //工作台右边的按钮
    private String righturl;
    private Banner banner;
    private TextView send_bt;
    private ImageView no_work_im;
    private TextView no_work_tv1;


    @Override
    public int getlayout() {
        return R.layout.new_page1;
    }



    @Override
    public void onHiddenChanged( boolean hidden) {
        //状态栏相关 5.9
        //setshowstatebar(hidden);

            statebar.setshowstatebar(hidden);
            isshowstatus = hidden;
        //头一次是不会执行这个方法的，切换到别的页面之后加才会执行这个方法。显示为false，隐藏为true
        LogUtil.e("--233当前页面的状态--"+hidden);
        /**
         * 从未登录到登录的时候
         */

        if (hidden){
            LogUtil.e("1.666"+"Work2");
            LogUtil.e("--接口隐藏注销--"+getClass().getSimpleName());
            Mainactivity.getChildFragmentWebviewCallback(null);
            LoginFragment.setChangeStateBarListener(null);
            ToWebviewFragment.setChangeStateBarListener(null);
//            if (netWorkStateReceiver!=null) {
//                getActivity().unregisterReceiver(netWorkStateReceiver);
//            }
        }else {
            LogUtil.e("--接口显示注册--"+getClass().getSimpleName());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Mainactivity.getChildFragmentWebviewCallback(WorkFragment2.this);
                    LoginFragment.setChangeStateBarListener(WorkFragment2.this);
                    ToWebviewFragment.setChangeStateBarListener(WorkFragment2.this);
                }
            },50);

            //下面这个状态的改变时由登录状态变为登出状态
            String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
            if (!user_id.matches("[0-9]+")){
                RefreashHomepagecallback();
            }


        }
        boolean status = sharepreferenceUtils.getbooleandata(MyApplication.mcontext, "status", false);
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");

    }




    /**
     *
     * 这个是通过反射获取状态栏的高度 (这个是沉浸式标题栏)
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }




    //仿淘宝头条  （这个同样是放在了网络请求后面）
    private void initView() {
        setView();
        //把view装进集合里面
        LogUtil.e(miaoqiandata.size()+"---"+newmiaoqiandata.size());
        upview1.setViews(views);
        /**
         * 设置item_view的监听
         */
        upview1.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
               // Toast.makeText(getActivity(), "你点击了第几个items666" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //仿淘宝头条
    private void setView() {
        //==================================================================洗数据 6.29
        for (int j=0;j<miaoqiandata.size();j++){
            //下面两个判断是第二版的接口
            if (miaoqiandata.get(j).title2!=null){
                newmiaoqiandata.add(new NewHomeBean1(miaoqiandata.get(j).title1,miaoqiandata.get(j).url));
                newmiaoqiandata.add(new NewHomeBean1(miaoqiandata.get(j).title2,miaoqiandata.get(j).url));
                //注意第一个的数据和第二个的判断和第二个的判断不能换，换了会报错 （新版工作台的数据是title1和title2）
            }else if (miaoqiandata.get(j).title1!=null){
                newmiaoqiandata.add(new NewHomeBean1(miaoqiandata.get(j).title1,miaoqiandata.get(j).url,true));
            }
            //这个是第一版的首页接口
            else {
                newmiaoqiandata.add(new NewHomeBean1(miaoqiandata.get(j).title,miaoqiandata.get(j).url));
            }
        }

        //=========================如果数据都是单个的，那么直接用miaoqiandata就行，不然的话先洗数据 6.29
        for (int i = 0; i < newmiaoqiandata.size(); i = i + 2) {
            final int position = i;
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_view, null);
            //初始化布局的控件
            TextView tv1 = (TextView) moreView.findViewById(R.id.tv1);
            TextView tv2 = (TextView) moreView.findViewById(R.id.tv2);

            /**
             * 设置监听
             */
            moreView.findViewById(R.id.rl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToWebviewFragment fragment = ToWebviewFragment.newInstance(newmiaoqiandata.get(position).url, "nostatabar");
                    getFragmentManager().
                            beginTransaction().
                            addToBackStack(null).
                            replace(R.id.rlroot, fragment)
                            .commit();
                   // Toast.makeText(getActivity(), position + "你点击了" + miaoqiandata.get(position).title.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            /**
             * 设置监听
             */
            moreView.findViewById(R.id.rl2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToWebviewFragment fragment = ToWebviewFragment.newInstance(newmiaoqiandata.get(position+1).url, "nostatabar");
                    getFragmentManager().
                            beginTransaction().
                            addToBackStack(null).
                            replace(R.id.rlroot, fragment)
                            .commit();
                   // Toast.makeText(getActivity(), position + "你点击了" + miaoqiandata.get(position + 1).title.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            //进行对控件赋值
           // tv1.setText(newmiaoqiandata.get(i).title.toString());
            // 这里是自己注释掉的，原来是有单双两条数据的
            //if (newmiaoqiandata.size() > i + 1) {
            //// TODO: 2018/7/9 后面的判断是为了防止数组下标越界
                if (!newmiaoqiandata.get(i).single&&newmiaoqiandata.size() > i + 1) {
                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
                    tv1.setText(newmiaoqiandata.get(i).title.toString());
                    tv2.setText(newmiaoqiandata.get(i + 1).title.toString());
            }
            else {
                    tv1.setText(newmiaoqiandata.get(i).title.toString());
                    moreView.findViewById(R.id.rl2).setVisibility(View.GONE);
                    //但是这个也只是在单数个数的情况下，注意双数个数的情况 6.29
                    i=i-1;
            }

            //添加到循环滚动数组里面去
            views.add(moreView);
        }
    }


    //修改按钮tab选择栏(最新xx，xx记录)
    private void setTabSelected(TextView btnSelected) {
        Drawable drawable = getActivity().getResources().getDrawable(R.drawable.shape_nav_indicator);
        ViewGroup.LayoutParams layoutParams = child_tv1.getLayoutParams();
        int width = layoutParams.width;
        drawable.setBounds(0,0,width,3);

//        selectedDrawable.setBounds(0, 0, right, DensityUtils.dipTopx(this, 3));
        btnSelected.setSelected(true);
        btnSelected.setCompoundDrawables(null, null, null, drawable);
        int size = select_tv_ry.getChildCount();
        for (int i = 0; i < size; i++) {
            if (btnSelected.getId() != select_tv_ry.getChildAt(i).getId()) {
                select_tv_ry.getChildAt(i).setSelected(false);
                ((TextView) select_tv_ry.getChildAt(i)).setCompoundDrawables(null, null, null, null);
            }
        }
    }


    //新版首页xxx常用功能
    public void initrecyclerapp(){
        rv_app = (RecyclerView) view.findViewById(R.id.rv_app);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_app.setLayoutManager(layoutManager);
        rv_app.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                DisplayMetrics metrics = new DisplayMetrics();
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                display.getMetrics(metrics);
                /**
                 * 具体的状态要不同的边距，因为每个状态文字不同，导致间距的不同
                 *
                 */
                if(app1_title.getText().toString().contains("电子发票")) {
                    if (parent.getChildAdapterPosition(view) == 0) {
                        //item是第一个的时候不设置间距
                        outRect.left = metrics.widthPixels / 12;
                    } else {
                        outRect.left = metrics.widthPixels / 7 - getResources().getDimensionPixelOffset(R.dimen.x5);
                    }
                }else if (app1_title.getText().toString().contains("电子合同")){
                    if (parent.getChildAdapterPosition(view) == 0) {
                        //item是第一个的时候不设置间距
                        outRect.left = metrics.widthPixels / 12;
                    } else {
                        outRect.left = metrics.widthPixels / 8 - getResources().getDimensionPixelOffset(R.dimen.x5);
                    }
                }else if (app1_title.getText().toString().contains("包裹")){
                    if (parent.getChildAdapterPosition(view) == 0) {
                        //item是第一个的时候不设置间距
                        outRect.left = metrics.widthPixels / 12;
                    } else {
                        outRect.left = metrics.widthPixels / 10 - getResources().getDimensionPixelOffset(R.dimen.x5);
                    }
                }
            }
        });
        commomadapter = new RecyclerAppAdapter(getActivity(), appdata);
        //commomadapter.notifyDataSetChanged();
        rv_app.setAdapter(commomadapter);
        commomadapter.setOnItemClickListener(new RecyclerAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ();
                }else {
                    // ToastUtil.showToastCenter(appdata.get(position).url);
                    ToWebviewFragment fragment = ToWebviewFragment.newInstance(appdata.get(position).url, "nostatabar");
                    getFragmentManager().
                            beginTransaction().
                            addToBackStack(null).
                            replace(R.id.rlroot, fragment)
                            .commit();
                }
            }
        });
    }

    //新版首页  更多app  (数据的初始化在网络请求之后)
    public void initrecyclerotherapp(){
        ry_other_app = (RecyclerView) view.findViewById(R.id.ry_other);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),4);
        ry_other_app.setLayoutManager(layoutManager);
        otherappAdapter = new RecyclerOtherAppAdapter(getActivity(), otherappdatas);
        //otherappAdapter.notifyDataSetChanged();
        ry_other_app.setAdapter(otherappAdapter);
        otherappAdapter.setOnItemClickListener(new RecyclerOtherAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               //ToastUtil.showToastCenter(otherappAdapter.getItemString(position));
                //// TODO: 2018/6/6 第三个参数是控制状态的显示，在这个fragment被销毁的时候
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ();
                }else {
                    // TODO: 2018/6/25 这里点下面的小图标也是切换
                    String itemurl = otherappAdapter.getItemString(position);
                    if (itemurl.contains("json")){
                        if (itemurl.contains("express")){
                            initnetword1("express");
                            select_page_text.setText("包裹快递");
                        }else if (itemurl.contains("invoice")){
                            initnetword1("invoice");
                            select_page_text.setText("电子发票");
                        }else if (itemurl.contains("doc")){
                            initnetword1("doc");
                            select_page_text.setText("电子合同");
                        }

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                commomadapter.notifyDataSetChanged();
                                otherappAdapter.notifyDataSetChanged();
                                statusadapter.notifyDataSetChanged();
                            }
                        },300);
                    }else {
                        ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(otherappAdapter.getItemString(position),
                                "nostatabar");
                        int activity_new_detail1 = CPResourceUtil.getId(getContext(), "rlroot");

                        getFragmentManager().beginTransaction()
                                .replace(R.id.rlroot, toWebviewFragment)
                                //.replace(activity_new_detail1,toWebviewFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });
    }





    //banner下面的状态 5.25
    private void initstatusrecycler() {
        status_rv = (RecyclerView) view.findViewById(R.id.status_rv);
        /**新版的这个状态栏改为recyclerview**/
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        status_rv.setLayoutManager(layoutManager);
        status_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                DisplayMetrics metrics = new DisplayMetrics();
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                display.getMetrics(metrics);
               // LogUtil.e("手机", "display is" + metrics.widthPixels + "--" + metrics.heightPixels);


                if (parent.getChildAdapterPosition(view) == 0) {
                    //item是第一个的时候不设置间距
                    outRect.left =metrics.widthPixels/7;
                    }
                else {
                    outRect.left = metrics.widthPixels/4-getResources().getDimensionPixelOffset(R.dimen.x5);
                    }

            }
        });
        statusadapter = new StatusRecyclerAdapter(statusdata);
        //statusadapter.notifyDataSetChanged();
        status_rv.setAdapter(statusadapter);
        statusadapter.setOnItemClickListener(new StatusRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //ToastUtil.showToastCenter("haha");
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ();
                }else {
                   // ToastUtil.showToastCenter(statusadapter.getItemString(position));
                    ToWebviewFragment fragment = ToWebviewFragment.newInstance(statusadapter.getItemString(position),
                            "nostatabar");
                    getFragmentManager().
                            beginTransaction().
                            addToBackStack(null).
                            replace(R.id.rlroot, fragment)
                            .commit();
                }

            }
        });
    }


    @Override
    public void SelectStatuscallback(String status) {
        initnetword1(status);
        if (status.equals("doc")) {
            select_page_text.setText("电子合同");
        }else if (status.equals("express")){
            select_page_text.setText("包裹快递");
        }else if (status.equals("invoice")){
            select_page_text.setText("电子发票");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                commomadapter.notifyDataSetChanged();
                otherappAdapter.notifyDataSetChanged();
                statusadapter.notifyDataSetChanged();
            }
        }, 300);
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //imageView.setImageResource(((BannerItem) path).pic);
            //因为以前从本地资源加载的是超级大的大图，所以加载起来自然就是全屏，现在这个从网络上传过来的并不是超级大图***
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //glide本身有自己的缓存策略，记得以后网络缓存的时候先用这个1.23**
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.a)
                    .error(R.drawable.a)
                    .priority(Priority.HIGH);
            Glide.with(getActivity())
                    .asBitmap()
                    .load(R.drawable.banner)
                    .apply(options)
                    .into(imageView);

        }
    }

    @Override
    public void initialize() {
        UpDatautils upDatautils = new UpDatautils(getActivity());
        upDatautils.checkVersion();

        //沉浸式状态栏
        statebar = new StatebarUtils2(this);
        //搜索栏
        searchtv = (TextView) view.findViewById(R.id.searchview1);
        upview1 = (UPMarqueeView) view.findViewById(R.id.upview1);
//        banner = (Banner) view.findViewById(R.id.banner);
//        banner.setImageLoader(new GlideImageLoader());
//        banner.setImages(BANNER_ITEMS);
//        banner.start();
        searchtv.setOnClickListener(this);
        //无状态时候的工作台
        send_bt = (TextView) view.findViewById(R.id.send_bt);
        no_work_im = (ImageView) view.findViewById(R.id.no_work_im);
        no_work_tv1 = (TextView) view.findViewById(R.id.no_work_tv1);

        send_bt.setOnClickListener(this);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.bottom_color));
        scan_photo = (ImageView) view.findViewById(R.id.scan_photo);
        scan_photo.setOnClickListener(this);


        //包裹快递常用功能
        app1_title = (TextView) view.findViewById(R.id.app1_title);

        //其他应用
        app2_title = (TextView) view.findViewById(R.id.app2_title);

        // 新版的工作台（有数据时候的状态 6.13）
        new_work_excle = (RelativeLayout) view.findViewById(R.id.new_work_excle);
        //工作台记录时间的 6.13
        status_bar_ry = (RelativeLayout) view.findViewById(R.id.status_bar_ry);
        no_work_excle1 = (RelativeLayout) view.findViewById(R.id.no_work_excle);

        /**有数据时候的工作台**/
        //工作台的状态文字（比如代签收）
        word_status_tv = (TextView) view.findViewById(R.id.word_status_tv);
        //工作台的状态时间（比如签收时间）
        work_status_time_tv = (TextView) view.findViewById(R.id.work_status_time_tv);
        //合同的名称
        docname= (TextView) view.findViewById(R.id.my_doc_name);
        //左边的公司名
        usercompany = (TextView) view.findViewById(R.id.my_user_company);
        //左边的人名
        sendername = (TextView) view.findViewById(R.id.my_sender_name);
        //右边的公司名
        receivecompany = (TextView) view.findViewById(R.id.my_receive_company);
        //右边的人名
        receivename = (TextView) view.findViewById(R.id.my_receive_name);
        //中间圆的状态
        circle = (TextView) view.findViewById(R.id.my_circle);
        //右边的钱数
        tv_money = (TextView) view.findViewById(R.id.tv_money);
        /***有数据时候的新版工作台**/


        //// TODO: 2018/6/6 网络请求
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                //doc invoice express
                initnetword1("doc");

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String s = select_page_text.getText().toString();
                        String selectname=null;
                        if (s.equals("电子合同")){
                            selectname="doc";
                        }else if (s.equals("电子发票")){
                            selectname="invoice";
                        }else if (s.equals("包裹快递")){
                            selectname="express";
                        }
                        initnetword1(selectname);
                       // refreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });



        //轮播图的初始化
        ll_point = (LinearLayout) view.findViewById(R.id.ll_point);
        viewPager = (ViewPager) view.findViewById(R.id.vp);
       // initviewpager();



        //这个是搜索栏里面的选择下角标 6.4
        select_page_text= (TextView) view.findViewById(R.id.select_page_text);
        select_page_text.setOnClickListener(this);
        Drawable drawable = getResources().getDrawable(R.drawable.pull_im);
        drawable.setBounds(0,0,getResources().getDimensionPixelOffset(R.dimen.y6),getResources().getDimensionPixelOffset(R.dimen.y3));
        select_page_text.setCompoundDrawables(null,null,drawable,null);


        //initrecyclerotherapp();

        //最新合同 和 合同记录按钮
        child_tv1 = (TextView) view.findViewById(R.id.child_tv1);
        child_tv2 = (TextView) view.findViewById(R.id.child_tv2);
        select_tv_ry = (RelativeLayout) view.findViewById(R.id.select_tv_ry);
        setTabSelected(child_tv1);
        child_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelected(child_tv1);
            }
        });

        child_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelected(child_tv1);
                if (righturl!=null&&!righturl.equals("")){
                    ToWebviewFragment fragment = ToWebviewFragment.newInstance(righturl,
                            "nostatabar");
                    getFragmentManager().
                            beginTransaction().
                            addToBackStack(null).
                            replace(R.id.rlroot, fragment)
                            .commit();
                }else {
                    ToastUtil.showToastCenter("尽请期待");
                }
            }
        });


            apptitlebar = (LinearLayout) view.findViewById(R.id.appbars);
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams layoutParams = apptitlebar.getLayoutParams();
            //ViewGroup.MarginLayoutParams marginLayoutParams=new ViewGroup.MarginLayoutParams(layoutParams);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            int offset = getResources().getDimensionPixelOffset(R.dimen.x10);
            marginLayoutParams.setMargins(0, statusBarHeight+offset, 0, 0);
            apptitlebar.setLayoutParams(marginLayoutParams);
            statebar.fullScreen(getActivity());



        //改变状态栏 5.11
        ToWebviewFragment.setChangeStateBarListener(this);
        LoginFragment.setChangeStateBarListener(this);
        SearchFragment.setChangeStateBarListener(this);
        HistoryDetailFragment.setChangeStateBarListener(this);
        SelectStatusFragment.setChangeStateBarListener(this);
        LoginFragment.setrefreashcallback(this);
        SelectStatusFragment.setSelectStatusListener(this);


    }





    //网络请求部分 2.5（原来是在Activity中，原来也是在fragment中，但是之前没有做延迟，导致数据的集合为空）
    public void initnetword1(String theme_type) {
        long l = System.currentTimeMillis();
        String  str=String.valueOf(l);
        HashMap<String,String> datas=new HashMap<>();
        datas.put("appv","v1");
        datas.put("theme_type",theme_type);
        //datas.put("page","1");
        datas.put("timestamp",str);
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        if (!user_id.equals("")&&user_id.matches("[0-9]+")){
            datas.put("user_id",user_id);
        }

        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString3(s);
        datas.put("sign", sign);
        httphelper.create().NewdataHomePage2(Constants.NEW_HOME_PAGE, datas,new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                //这个暂时注释了
                LogUtil.e("---有数据么--"+s);
                try {
                    // if (s.contains("waitReceive")&&s.contains("waitPayCount")&&s.contains("waitSign")) {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    //状态栏的（显示 代取件已取件之类的）
                    JSONObject jsonObject1 = new JSONObject(data);
                    String count = jsonObject1.getString("count");

                    //常用功能
                    JSONObject jsonObject2=new JSONObject(data);
                    String common_app = jsonObject2.getString("common_app");

                    //其他应用
                    JSONObject jsonObject3 = new JSONObject(data);
                    String other_app = jsonObject3.getString("other_app");
                    //秒签头条
                    JSONObject jsonObject4=new JSONObject(data);
                    String news = jsonObject4.getString("news");
                    LogUtil.e(news);

                    //banner
                    JSONObject jsonObject7 = new JSONObject(data);
                    String banner = jsonObject7.getString("banner");
                    ArrayList<NewHomeBean1> bannerlist=(ArrayList<NewHomeBean1>)GsonUtil.parseJsonToList(banner
                            ,new TypeToken<List<NewHomeBean1>>(){}.getType());
                   // LogUtil.e(bannerlist+"");
                    bannersize.clear();
                    if (bannerlist.size()>1){
                        bannersize.addAll(bannerlist);
                        bannersize.add(bannersize.size(),bannersize.get(0));
                        bannersize.add(0,bannersize.get(bannersize.size()-1));
                    }else {
                        bannersize.addAll(bannerlist);
                    }
                    initviewpager();

                    //最近操作
                    JSONObject jsonObject5 = new JSONObject(data);
                    String recent = jsonObject5.getString("recent");

                    //主页加号的大图标和小图标
                    JSONObject jsonObject6 = new JSONObject(data);
                    String quick_action = jsonObject6.getString("quick_action");
                    Newsbean bgandsmall = GsonUtil.parseJsonToBean(quick_action, Newsbean.class);
                    LogUtil.e(bgandsmall+"");
                    bigdatas.clear();
                    smalldatas.clear();
                    bigdatas.addAll(bgandsmall.big);
                    smalldatas.addAll(bgandsmall.small);




                    RecentBean recentBean = GsonUtil.parseJsonToBean(recent, RecentBean.class);
                    //控制台左边的数据
                    final RecentBean.LeftBean left = recentBean.left;
                    RecentBean.RightBean right = recentBean.right;

                    child_tv1.setText(left.name);
                    child_tv2.setText(right.name);
                    righturl=right.url;
                    RecentBean.LeftBean.DataBean data1 = left.data;
                    LogUtil.e("----"+data1);
                    //// TODO: 2018/6/6 这个当userid没有值的时候 （或者是未登录的情况）
                    // TODO: 2018/6/25 这里额外小心的是某个变量为空，就会走catch里面，然后数据适配器就没有值了。。
                    if (left.data._$1==null||left.data._$1.equals("")){
                        //ToastUtil.showToastCenter("哈哈");
                        //这里的状态是从网络获取的 6.13
                        status_bar_ry.setVisibility(View.GONE);
                        new_work_excle.setVisibility(View.GONE);
                        //没有任务状态的时候
                        no_work_excle1.setVisibility(View.VISIBLE);

                        send_bt = (TextView) view.findViewById(R.id.send_bt);
                        no_work_im = (ImageView) view.findViewById(R.id.no_work_im);
                        no_work_tv1 = (TextView) view.findViewById(R.id.no_work_tv1);
                        if (!left.img.equals("")){
                            Picasso.with(getActivity()).load(left.img).into(no_work_im);
                        }
                        send_bt.setText(left.button);
                        no_work_tv1.setText(left.desc);
                        send_bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                                if (!user_id.matches("[0-9]+")) {
                                    lognMQ();
                                }else {
                                    ToWebviewFragment fragment = ToWebviewFragment.newInstance(left.url,
                                            "nostatabar");
                                    getFragmentManager().
                                            beginTransaction().
                                            addToBackStack(null).
                                            replace(R.id.rlroot, fragment)
                                            .commit();
                                }
                            }
                        });

                    }
                    //还有已签收的状态这里也是没有考虑的 6.19 // TODO: 2018/6/19 这里有个巨坑了解一下
                    //  "[\u4e00-\u9fa5]");
                   // else if (left.data._$1.contains("收")||left.data._$1.equals("待收票")){
                    else if (!left.data._$1.equals("")||!left.data._$2.equals("")){
                        status_bar_ry.setVisibility(View.VISIBLE);
                        new_work_excle.setVisibility(View.VISIBLE);
                        no_work_excle1.setVisibility(View.GONE);
                        new_work_excle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToWebviewFragment fragment = ToWebviewFragment.newInstance(left.url,
                                        "nostatabar");
                                getFragmentManager().
                                        beginTransaction().
                                        addToBackStack(null).
                                        replace(R.id.rlroot, fragment)
                                        .commit();
                            }
                        });

                        //有圆圈的工作台的改变
                        word_status_tv.setText(left.data._$1);
                        work_status_time_tv.setText(left.data._$2);
                        docname.setText(left.data._$3);
                        usercompany.setText(left.data._$5);
                        sendername.setText(left.data._$6);
                        receivecompany.setText(left.data._$7);
                        receivename.setText(left.data._$8);
                        circle.setText(left.data._$9);
                        if (!left.data._$4.equals("")){
                            tv_money.setVisibility(View.VISIBLE);
                            tv_money.setText(left.data._$4);
                        }else {
                            tv_money.setVisibility(View.GONE);
                        }

                    }


                    //如果数据是[]都是用集合，实际上这个就是集合。。。想想之前学的java后台。
                    ArrayList<NewHomeBean1> newsbeen=(ArrayList<NewHomeBean1>)GsonUtil.parseJsonToList(count
                            ,new TypeToken<List<NewHomeBean1>>(){}.getType());
                    LogUtil.e(newsbeen+"");
                    statusdata.clear();
                    statusdata.addAll(newsbeen);

                    /**新修改的状态栏（已取件代取件2018.5.24）**/
                     //initstatusrecycler();

                    //=================================常用功能 // TODO: 2018/6/19 这里也有坑 ，这里的名字获取的是（电子发票·常用功能）的名字
                    Newsbean newsbean = GsonUtil.parseJsonToBean(common_app, Newsbean.class);
                    LogUtil.e(newsbean+"");
                    /***这个是包裹快递常用功能的标题***/
                    app1_title.setText(newsbean.title);
                    appdata.clear();
                    appdata.addAll(newsbean.data);
                    LogUtil.e("----"+appdata);
                    //initrecyclerapp();
                    //=========================常用功能

                    //===============================其他应用
                    Newsbean newsbean1 = GsonUtil.parseJsonToBean(other_app, Newsbean.class);
                    app2_title.setText(newsbean1.title);
                    otherappdatas.clear();
                    otherappdatas.addAll(newsbean1.data);
                   // initrecyclerotherapp();
                    //=============================其他应用

                    //轮播图 5.25
                    //// TODO: 2018/5/25 暂时注释 （问题是随切换会不停变化）5.25，以后等轮播图有数据了再来试验
//                    ll_point = (LinearLayout) view.findViewById(R.id.ll_point);
//                    viewPager = (ViewPager) view.findViewById(R.id.vp);
//                    initviewpager();
                   //轮播图 5.25


                    //===================秒签头条
                    //s1(测试的)
                    Newsbean newdatas = GsonUtil.parseJsonToBean(news, Newsbean.class);
                    /***如果集合不清除，界面会越来越卡**/
                    newmiaoqiandata.clear();
                    miaoqiandata.clear();
                    miaoqiandata.addAll(newdatas.data);
                    //仿淘宝头条的轮播
                    //initdata();
                    views.clear();
                    initView();
                    //=================秒签头条

                   // refreshLayout.setRefreshing(false);
                    if (refreshLayout.isRefreshing()){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);
                            }
                        });

                    }

                    //第一次进来才刷新 6.19
                    if (isfrist) {
                        initrecyclerotherapp();
                        initstatusrecycler();
                        initrecyclerapp();
                        refreshLayout.setRefreshing(false);
                        isfrist=false;
                    }


                }catch (Exception e){
                    e.printStackTrace();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToastCenter("后台数据错误，紧急修复中");
                            refreshLayout.setRefreshing(false);
                        }
                    }, 600);


                }
            }



            @Override
            public void fail(Exception e) {
                LogUtil.e("--连接失败--" + e);

                if (e!=null){
//                    if (!Netutils.isNetworkAvalible(getActivity())){
//                        ToastUtil.showToast("当前网络不可用请检查网络");
//                        Netutils.checkNetwork(getActivity());
//
//                    }

                    if (netWorkStateReceiver == null) {
                        netWorkStateReceiver = new NetWorkStateReceiver();
                        netWorkStateReceiver.setrefreashcallback(WorkFragment2.this);

                    }
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                    getActivity().registerReceiver(netWorkStateReceiver, filter);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                        }
                    },600);

                }
            }
        });

    }




    //不断循环的方法 7.4
    private Runnable mRunnable = new Runnable() {
        public void run() {
            while (iscycler) {
                try {
                    Thread.sleep(30000);
                    //mHandler.sendMessage(mHandler.obtainMessage());
                    handler.sendMessage(handler.obtainMessage(1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };






    private void initviewpager() {

        /****
         * 新版的首页展示没加banner的数据，暂时用固定的图片代替 5.25
         *
         *
         * 这个集合最少要四个，如果只有两个的话是不会动的（两张轮播图，头和尾再添加添加一张。6.13）
         */
        imageViews.clear();
        for (int i = 0; i < bannersize.size(); i++) {
            ImageView imageView = new ImageView(getActivity());//要调用图片资源就先要调用ImageView资源
            //因为以前从本地资源加载的是超级大的大图，所以加载起来自然就是全屏，现在这个从网络上传过来的并不是超级大图***
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //glide本身有自己的缓存策略，记得以后网络缓存的时候先用这个1.23**
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.a)
                    .error(R.drawable.a)
                    .priority(Priority.HIGH);
            Glide.with(getActivity())
                    .asBitmap()
                    .load(bannersize.get(i).img)
                    .apply(options)
                    .into(imageView);
            imageViews.add(imageView);//把数据添加到集合中。

        }
        if (bannersize.size()>1) {
            //bannersize //// TODO: 2018/6/28 当bannersize 是2的的时候都是4张起步了，因为首位各添加了一张
            intitPoint();
        }

        viewPager.setAdapter(new MyPageradpter());
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        //tv.setText(imagetitle[preposition]);//这个是为了让第一个标题和xml中的标题同步
        //下面这个参数是为了定位到某个位置
        //int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageViews.size();//要保证是5的整数倍，没有为什么。不然可能会出错;
        if (bannersendmessagefirst) {
            viewPager.setCurrentItem(Fig_current,false);
            handler.sendEmptyMessageDelayed(0, 2500);
            bannersendmessagefirst=false;
        }
        //h2.sendEmptyMessageDelayed(0,4000);

    }

    private void intitPoint() {
        for (int i=0;i<imageViews.size()-2;i++) {
            ImageView point = new ImageView(getActivity());
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);//这个16是像素
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 8;
            }
            point.setLayoutParams(params);

            ll_point.addView(point);//这个addview的意思和前面的add差不多，意思是把指定的资源（自定义图片之类）添加到指定容器中
        }

    }



    /**
     * 沉浸式状态栏的切换
     */
    @Override
    public void changestatebarcallbcak() {
        //StatebarUtils statebar=new StatebarUtils(getActivity());
        statebar.setshowstatebar(isshowstatus);
        //setshowstatebar(isshowstatus);
    }

    /**
     * 这个是新版的登录之后刷新页面 6.13
     *
     */
    @Override
    public void RefreashHomepagecallback() {
        //刷新页面
        String selecttitle = select_page_text.getText().toString();
        String selectname=null;
        if (selecttitle.equals("电子合同")){
            selectname="doc";
        }else if (selecttitle.equals("电子发票")){
            selectname="invoice";
        }else if (selecttitle.equals("包裹快递")){
            selectname="express";
        }
        initnetword1(selectname);
        if (!isfrist) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commomadapter.notifyDataSetChanged();
                    otherappAdapter.notifyDataSetChanged();
                    statusadapter.notifyDataSetChanged();
                }
            }, 300);
        }
//        if (netWorkStateReceiver!=null){
//            getActivity().unregisterReceiver(netWorkStateReceiver);
//        }
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        @Override
        public void onPageSelected(int position) {

//            int realPosition = position % imageViews.size();
//            tv.setText(imagetitle[realPosition]);
//            ll_point.getChildAt(preposition).setEnabled(false);
//            ll_point.getChildAt(realPosition).setEnabled(true);
//
//            preposition = realPosition;


            if (position==imageViews.size()-1){
                currentposition=Fig_current;
            }else if (position==0){
                currentposition=imageViews.size()-2;
            }else {
                currentposition=position;
            }
            viewPager.setCurrentItem(currentposition,false);


            int presspoint=currentposition-1;
            for (int i=0;i<ll_point.getChildCount();i++){
                View childAt = ll_point.getChildAt(i);
                if (i==presspoint){
                    childAt.setEnabled(true);
                }else {
                    childAt.setEnabled(false);
                }
            }
        }


        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == ViewPager.SCROLL_STATE_DRAGGING) {//这个是拖拽状态
                // Log.e(TAG, "-----------SCROLL_STATE_DRAGGING拖拽");

            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {//这个开始
                // Log.e(TAG, "-----------SCROLL_STATE_SETTLING开始");
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {//这个是空闲状态
                //Log.e(TAG, "-----------SCROLL_STATE_IDLE空闲");

            }
        }

    }

    class MyPageradpter extends PagerAdapter {


        @Override
        public int getCount() {
            return imageViews.size();//返回的是集合的总数
            //return Integer.MAX_VALUE;
        }

        @Override//检查视图和对象是否相等
        public boolean isViewFromObject(View view, Object object) {
            if (view == object) {
                return true;
            } else {

                return false;
            }
        }

        @Override//这个默认只能创建两个，然后开始执行销毁
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
            //Log.e(TAG, "destroyItem==" + position + "----==object" + object);
        }



        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {//这个是创建界面

            //int realPosition = position % imageViews.size();//

            //下面是原来的
            final ImageView imageView = imageViews.get(position);//因为在前面已经实例化过，这里直接获取就行了

            //下面是添加返回键的
            // imageView = imageViews.get(realPosition);

            container.addView(imageView);
            //Log.e(TAG, "instantiateItem==" + position + "----imageview==" + imageView);
            imageView.setOnTouchListener(new View.OnTouchListener() {//写这个是为了防止手指在点击图片的时候图片自己动了
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {//其实这下面的写好之后就已经完美了70%，80%了，会自动走，还能手动走，让他更完美就在onPageScrollStateChanged方法
                        case MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);//这个是移除所有消息
                            handler.sendEmptyMessageDelayed(0, 4000);
                            break;//还有一个问题很奇怪，为什么要用getAction()
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            handler.removeCallbacksAndMessages(null);
                            handler.sendEmptyMessageDelayed(0, 4000);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.removeCallbacksAndMessages(null);
                            handler.sendEmptyMessageDelayed(0, 4000);
                            break;
                    }

                    return false;//想要点击有弹窗之类的效果就要把这里写为false
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //ToastUtil.showToastCenter("当前的位置是--"+position);

                    ToWebviewFragment toWebviewFragment=ToWebviewFragment.newInstance(bannersize.get(position).url,"nostatabar");

                    /***暂时没有其他的回退栈可以用这个最大的 1.29 （除非主fragment中有含有回退栈的fragment可能会遇到坑）***/
                    /**注意这个和Activity通信的fragment返回键的接口在监听webview的回退的时候，是“非”！！！ 1.29**/
                    /***1.29***/
                    getFragmentManager().beginTransaction()
                            .replace(R.id.rlroot,toWebviewFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            return imageView;

        }

    }




    @Override
    public void onClick(View v) {
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");

        switch (v.getId()){
            case R.id.searchview1:
                //// TODO: 2018/6/6 这个是原来的能用的
                if (!user_id.matches("[0-9]+")){
                    lognMQ();
                }else {
                    getFragmentManager().
                            beginTransaction().
                            addToBackStack(null).
                            replace(R.id.rlroot, new SearchFragment()).
                            commit();
                }


                break;

            case R.id.iv_more:


                break;

            //网络错误重新加载
            case  R.id.btn_reload:


                break;

            //分享按钮
            case  R.id.share_tv:
                //excel.put("circle_no",doc_status);
                String circle_no = excel.get("circle_no");
                //没有支付的点击分享会显示页面躲喵喵
                if (circle_no.equals("88")){
                    ToastUtil.showToastCenter("尚未支付,不能分享");
                }else {
                    ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(Constants.TEST_SHARE + docno.getText(),"nostatabar");
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.rlroot, toWebviewFragment)
                            .commit();

                }
                break;

            //再次新版首页  2018.5.15
            case R.id.select_page_text:
                if (!user_id.matches("[0-9]+")) {
                    lognMQ();
                }else {
                    SelectStatusFragment fragment = new SelectStatusFragment();
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.rlroot,fragment)
                            .commit();

//                    //// TODO: 2018/5/25  这里切换会延迟是因为数据太快，做一个延时操作（已实验），或者是在adapter中判断为不为空（待判断）5.25
//                    selecttitle = new ArrayList<>();
//                    selecttitle.add("电子合同");
//                    selecttitle.add("电子发票");
//                    selecttitle.add("包裹快递");
//                    if (select_page_text.getText().toString().equals("电子合同")) {
//                        selecttitle.remove(0);
//                    } else if (select_page_text.getText().toString().equals("电子发票")) {
//                        selecttitle.remove(1);
//                    } else if (select_page_text.getText().toString().equals("包裹快递")) {
//                        selecttitle.remove(2);
//                    }
//                    GirdviewselectAdapter girdviewselectAdapter = new GirdviewselectAdapter(selecttitle);
//                    girdviewselectAdapter.notifyDataSetChanged();
//                    LayoutInflater inflater1 = LayoutInflater.from(getActivity());
//                    //因为这个最外层的布局是linearlayout，所以这里要强转为linearlayout
//                    LinearLayout popliner1 = (LinearLayout) inflater1.inflate(R.layout.select_status_root, null);
//                    ListView lv_select = (ListView) popliner1.findViewById(R.id.gv_more);
//                    lv_select.setAdapter(girdviewselectAdapter);
//                    lv_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            select_page_text.setText(selecttitle.get(position));
//                            if (selecttitle.get(position).equals("电子合同")) {
//                                initnetword1("doc");
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        commomadapter.notifyDataSetChanged();
//                                        otherappAdapter.notifyDataSetChanged();
//                                        statusadapter.notifyDataSetChanged();
//                                    }
//                                }, 300);
//                            } else if (selecttitle.get(position).equals("电子发票")) {
//                                initnetword1("invoice");
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        commomadapter.notifyDataSetChanged();
//                                        otherappAdapter.notifyDataSetChanged();
//                                        statusadapter.notifyDataSetChanged();
//                                    }
//                                }, 300);
//                            } else if (selecttitle.get(position).equals("包裹快递")) {
//                                initnetword1("express");
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        commomadapter.notifyDataSetChanged();
//                                        otherappAdapter.notifyDataSetChanged();
//                                        statusadapter.notifyDataSetChanged();
//                                    }
//                                }, 300);
//
//                            }
//                            popupWindow.dismiss();
//
//                        }
//                    });
//
//                    //想要popup全屏其实很简单，只需要改这里就行。。当时怎么这么zz。
//                    popupWindow = new PopupWindow(popliner1,
//                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT, true);
//
//                    popupWindow.setOutsideTouchable(true);
//                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                    popupWindow.showAsDropDown(select_page_text, 0, 10);// 在控件下方显示popwindow
//                    popupWindow.update();
                }

                break;

            case  R.id.scan_photo:
                startActivityForResult(new Intent(getActivity(),CaptureActivity.class),QRCode_Action);
                break;


        }

    }

    private void lognMQ() {
        getFragmentManager().
                beginTransaction().
                addToBackStack(null).
                replace(R.id.rlroot, new LoginFragment()).
                commit();
    }


    public static LoginCallBack loginCallBack;

    public static void setLoginCallBack(LoginCallBack loginCallBack){
        WorkFragment2.loginCallBack=loginCallBack;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=Activity.RESULT_OK){
            return;
        }else {
            if (requestCode==QRCode_Action){
                 String qrcode = data.getStringExtra("qrcode");
               // ToastUtil.showToastCenter(qrcode);
                HistoryDetailFragment newfragment3 = HistoryDetailFragment.newInstance(qrcode);
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.rlroot,newfragment3)
                        .commit();
            }
        }
    }

    @Override
    public void onDestroy() {

        if (bannertitle!=null) {
            bannerImageList.clear();
        }
        if (appiconList!=null) {
            appiconList.clear();
        }
        if (appurl!=null) {
            appurl.clear();
        }
        if (mystatus!=null) {
            mystatus.clear();
        }
        if (bannersize !=null) {
            bannersize.clear();
        }
        if (excel!=null) {
            excel.clear();
        }
        if (bannerClickUrl!=null) {
            bannerClickUrl.clear();
        }
        if (handler!=null) {
            handler.removeCallbacksAndMessages(null);
        }
        getActivity().unregisterReceiver(netWorkStateReceiver);
        super.onDestroy();
    }
}