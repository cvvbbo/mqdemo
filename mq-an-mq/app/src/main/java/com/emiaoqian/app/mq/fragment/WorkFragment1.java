package com.emiaoqian.app.mq.fragment;


import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.emiaoqian.app.mq.Interface.FragmentCallback;
import com.emiaoqian.app.mq.Interface.LoginCallBack;
import com.emiaoqian.app.mq.Interface.RefreashCallBack;
import com.emiaoqian.app.mq.Interface.ViewPagerCallBack;
import com.emiaoqian.app.mq.Interface.WebWiewCallback;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.adapter.GirdViewStatusApadter;
import com.emiaoqian.app.mq.adapter.GirdviewMoreAdapter;
import com.emiaoqian.app.mq.adapter.GridViewAppAdapter;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.Appdata;
import com.emiaoqian.app.mq.bean.BannerInfo;
import com.emiaoqian.app.mq.bean.HomeBean;
import com.emiaoqian.app.mq.bean.HomeExcel;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.Netutils;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;




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
public class WorkFragment1 extends BaseFragment implements ViewPagerCallBack,View.OnClickListener,
        RefreashCallBack{


    public static ArrayList<String> bannerImageList = new ArrayList<>();

    public static ArrayList<String> bannerClickUrl=new ArrayList<>();

    public static ArrayList<String> appiconList = new ArrayList<>();

    public static ArrayList<String> appurl=new ArrayList<>();

    public static ArrayList<String> mystatus=new ArrayList<>();

    private ArrayList<BannerInfo> banner;
    public static HashMap<String, String> excel=new HashMap<>();

    public static ArrayList<String> bannertitle=new ArrayList<>();
    public static ArrayList<String>  apptitle=new ArrayList<>();

    //这个是分享旁边的按钮
    public static ArrayList<BannerInfo>  buttonmore=new ArrayList<>();

    public PopupWindow popupWindow;


    private GridView gv_status;
    private GridView gv_app;
    private RecyclerView ry;
    private ViewPager viewPager;
    private LinearLayout ll_point;

    private int Fig_current=1;

    private int currentposition=0;

    private boolean isconn;


    private ArrayList<ImageView> imageViews = new ArrayList<>();

    private ArrayList<String> my_status_details_url=new ArrayList<>();

    private int item_tag=1;

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

            if (msg.what==1){
                networdcycle();
            }else if (msg.what==0) {
                int item = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(item);//这个先写，int item后写

                //这里的发送消息是能够让viewpager自动轮播的关键 2017.1.4
                handler.sendEmptyMessageDelayed(0, 2500);

            }

        }
    };

    boolean iscycler=true;


    private int preposition = 0;

    private RelativeLayout success_rl;

    private GestureDetector gesture; //手势识别



    private TextView textView;
    private GirdViewStatusApadter girdViewApadter;
    private SwipeRefreshLayout refreshLayout;
    private FragmentManager appfragmentManager ;
    private ImageView more;
    private RelativeLayout home_excle;
    private FragmentManager statusfragmentmanger;
    private int current_item=0;
    private TextView sendername;
    private TextView usercompany;
    private TextView createtime;
    private TextView receivename;
    private TextView receivecompany;
    private TextView docname;
    private TextView docno;
    private TextView circle;
    private TextView expresstype;
    private LinearLayout excelbuttom;
    private ImageView scanphoto;
    private FrameLayout fail_fl;
    private Button btn_reload;
    private TextView share_tv;
    private TextView tv_color;
    private GestureDetector mygesture;
    private NestedScrollView nestedscollview;
    //private String user_id;
    private GridViewAppAdapter gridViewAppAdapter;
    private RelativeLayout no_work_excle;
    private TextView note_tv;
    //private ArrayList<String> bannerImageList;


    //String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");


    @Override
    public int getlayout() {
        return R.layout.new_page;
    }


    /***
     *
     * 动态计算gridview的高度，目前的条件是，gridview里面没有文字，只有图片
     * 然后一个gridview里面的图标是110*110px（在手机分辨率是1280*720的情况下）
     *
     *
     *
     * @return
     */
    public double calculateGridviewHeight(){
        //秒签应用的图标获取 1.24
        double i = appiconList.size() / 4.0;
        if (i>1.0||appiconList.size()%4==0){
            //下面这个是在gridview里面，只要图标不要字（他的图标带了文字）
            //因为第二行的数据在1.2到1.7之间。要让显示都显示出一个固定的高度
            //这个条件是大于.5以上的

            double ceil=Math.ceil(i);
            //double girdheight = 106 * (Math.ceil(i) + 0.85);
            double girdheight = getResources().getDimensionPixelSize(R.dimen.y33) * (Math.ceil(i) + (0.45)*ceil);
            return girdheight;

        }else {
            //这个条目是小于一行（一行4个）

            //return 106*1.45;
            return getResources().getDimensionPixelSize(R.dimen.y33) * 1.45;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {

        //头一次是不会执行这个方法的，切换到别的页面之后加才会执行这个方法。显示为false，隐藏为true
        LogUtil.e("--233当前页面的状态--"+hidden);
        /**
         * 从未登录到登录的时候
         */

        if (hidden){
            LogUtil.e("--接口隐藏注销--"+getClass().getSimpleName());
            Mainactivity.getChildFragmentWebviewCallback(null);
        }else {
            LogUtil.e("--接口显示注册--"+getClass().getSimpleName());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Mainactivity.getChildFragmentWebviewCallback(WorkFragment1.this);
                }
            },50);

        }

            //sharepreferenceUtils.saveBooleandata(MyApplication.mcontext,"status",true);
            boolean status = sharepreferenceUtils.getbooleandata(MyApplication.mcontext, "status", false);
            String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");
            LogUtil.e("--登录的状态是--" + status + "-------user_id是---" + user_id);
            /**
             *
             * 这个是-->没有登录-->登录的时候
             *
             */
            if (user_id.matches("[0-9]+") && status) {

                //因为这个fragment是走show和add方法
                networdcycle();

                sharepreferenceUtils.saveBooleandata(MyApplication.mcontext, "status", false);


            }


            //下面这个状态是 已登录-->退出3.1
            else if (!user_id.matches("[0-9]+") && status) {
                no_work_excle.setVisibility(View.VISIBLE);
                home_excle.setVisibility(View.GONE);

                //轮播图下面的状态栏，清空
                if (mystatus.size() != 0) {

                    mystatus.set(1, "0");
                    mystatus.set(2, "0");
                    mystatus.set(0, "0");
                    girdViewApadter.notifyDataSetChanged();


                }

                note_tv.setText("我要登录/注册");
                note_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginCallBack.loginoutcallback(false);

                    }
                });


//                if (handler != null) {
//                    // handler.removeCallbacksAndMessages(null);
//                   handler.removeMessages(1);
//                }



            }

        }



    @Override
    public void initialize() {

        textView = (TextView) view.findViewById(R.id.searchview1);
        scanphoto = (ImageView) view.findViewById(R.id.scan_photo);
        more = (ImageView) view.findViewById(R.id.iv_more);
        more.setOnClickListener(this);
        share_tv = (TextView) view.findViewById(R.id.share_tv);
        ry = (RecyclerView) view.findViewById(R.id.ry);
        share_tv.setOnClickListener(this);
        excelbuttom = (LinearLayout) view.findViewById(R.id.excel_buttom);
        sendername = (TextView) view.findViewById(R.id.sender_name);
        usercompany = (TextView) view.findViewById(R.id.user_company);
        createtime = (TextView) view.findViewById(R.id.create_time);
        receivename = (TextView) view.findViewById(R.id.receive_name);
        receivecompany = (TextView) view.findViewById(R.id.receive_company);
        docname = (TextView) view.findViewById(R.id.doc_name);
        docno = (TextView) view.findViewById(R.id.doc_no);
        circle = (TextView) view.findViewById(R.id.circle);
        expresstype = (TextView) view.findViewById(R.id.express_type);
        tv_color = (TextView) view.findViewById(R.id.tv_color);

        home_excle = (RelativeLayout) view.findViewById(R.id.rl_home);
        no_work_excle = (RelativeLayout) view.findViewById(R.id.no_work_excle);
        note_tv = (TextView) view.findViewById(R.id.note_tv);
        textView.setOnClickListener(this);
        ViewTreeObserver vto = home_excle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                home_excle.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                LogUtil.e("----高度是----"+home_excle.getHeight());
                //home_excle.getHeight();

            }
        });

        //user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");

        viewPager = (ViewPager) view.findViewById(R.id.vp);
        gv_status = (GridView) view.findViewById(R.id.gv_status);
        gv_app = (GridView) view.findViewById(R.id.gv_app);
        gridViewAppAdapter = new GridViewAppAdapter();

        //girdViewApadter = new GirdViewStatusApadter();

        ll_point = (LinearLayout) view.findViewById(R.id.ll_point);




        //因为有无网和有网的判断，这个要提前初始化 2.5
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.bottom_color));

        //没网或者是网络错误（服务器连接错误的时候）的时候显示这个2.5
        fail_fl = (FrameLayout) view.findViewById(R.id.fail_fl);
        btn_reload = (Button) view.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(this);
        success_rl = (RelativeLayout) view.findViewById(R.id.success_rl);
        nestedscollview = (NestedScrollView) view.findViewById(R.id.nestedscollview);


        LogUtil.e("--77-scrollview下拉的距离是--"+nestedscollview.getScaleY());

        //下拉刷新冲突的解决办法（swiperefreash和scrollview嵌套冲突，导致swiperefreash拉下很灵敏）
        nestedscollview.getViewTreeObserver().addOnScrollChangedListener(new  ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                refreshLayout.setEnabled(nestedscollview.getScrollY()==0);
            }
        });

        //refreshLayout.setNestedScrollView(nestedscollview);



        //下面这个是在没有网络的情况下2.5
        if (!Netutils.isNetworkAvalible(MyApplication.mcontext)) {
            success_rl.setVisibility(View.GONE);
            fail_fl.setVisibility(View.VISIBLE);
            return;
        }


        //提前加载swipeRefreshlayout 2.5
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                initnetword1();

            }
        });

        //showdata();

        BaseWebFragment.setRefreashCallBack(this);


        //轮询只在这里走一次
           new Thread(mRunnable).start();


        Mainactivity.getChildFragmentWebviewCallback(this);


    }

    private void showdata() {
        //网络加载和数据的展示都是异步的，如果是同步会报控制针或者，ui为空2.9
        //这个是初次进来后下拉刷新的方法
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initdate();
                //数据全部显示之后隐藏进度条
                refreshLayout.setRefreshing(false);

            }
        }, 300);
    }

    private void initdate() {

        no_work_excle.setVisibility(View.GONE);
        home_excle.setVisibility(View.VISIBLE);


        String lastDocType = excel.get("lastDocType");
        if (lastDocType.equals("实物包裹")){
            excelbuttom.setVisibility(View.GONE);
        }else {
            excelbuttom.setVisibility(View.VISIBLE);
        }

        String doc_status = excel.get("doc_status");
        String receive_company = excel.get("receive_company");
        String receive_name = excel.get("receive_name");
        String user_company = excel.get("user_company");
        String sender_name = excel.get("sender_name");
        String doc_name = excel.get("doc_name");
        String doc_no = excel.get("doc_no");
        String create_time = excel.get("create_time");
        String role = excel.get("role");
        receivecompany.setText(receive_company);
        receivename.setText(receive_name);
        usercompany.setText(user_company);
        sendername.setText(sender_name);
        docname.setText(doc_name);
        docno.setText(doc_no);
        if (role.equals("1")){
            circle.setBackgroundResource(R.drawable.status_sign);
            tv_color.setBackgroundResource(R.color.excel_status_blue);
            circle.setTextColor(getResources().getColor(R.color.excel_status_blue));
        }else if (role.equals("2")){
            circle.setBackgroundResource(R.drawable.status_send);
            tv_color.setBackgroundResource(R.color.excel_status_orange);
            circle.setTextColor(getResources().getColor(R.color.excel_status_orange));
        }
        circle.setText(doc_status);
        expresstype.setText(lastDocType);
        createtime.setText(create_time);


        initStatusData();

        //轮询请求网络数据 2.3



        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.bottom_color));



        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        networdcycle();
                        refreshLayout.setRefreshing(false);
                    }
                },300);

            }
        });

        ViewGroup.LayoutParams layoutParams = gv_app.getLayoutParams();
        double i = calculateGridviewHeight();

        LogUtil.e("-----"+i);
        layoutParams.height=(int) i;




        ry.setLayoutManager(new LinearLayoutManager(getActivity()));
        ry.setAdapter(new Myadapter());
        //解决recyclerview和scrollview嵌套起来会造成卡顿的问题 2018.1.4
        ry.setNestedScrollingEnabled(false);


        gv_status.setAdapter(girdViewApadter);
        // GridViewAppAdapter gridViewAppAdapter = new GridViewAppAdapter();
        gv_app.setAdapter(gridViewAppAdapter);


        gv_app.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //果然在跳转之前移除消息之后，再次回来就不会发生消息发送错乱 1.26
                // handler.removeCallbacksAndMessages(null);

                //这里有坑，天坑不是自己fragment里面嵌套webview回退的问题 1.30。目测是网页的问题
                ToWebviewFragment toWebviewFragment=ToWebviewFragment.newInstance(Constants.CONST_HOST1+"/"+appurl.get(position)
                        ,"nostatabar");

                /***暂时没有其他的回退栈可以用这个最大的 1.29 （除非主fragment中有含有回退栈的fragment可能会遇到坑）***/
                /**注意这个和Activity通信的fragment返回键的接口在监听webview的回退的时候，是“非”！！！ 1.29**/
                /***1.29***/
                getFragmentManager().beginTransaction()
                        .replace(R.id.rlroot,toWebviewFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        //既然这里连接网络会延迟那么就放在别的地方连接网络！！！！
        //initnetword1();



        initviewpager();



        ((Mainactivity)getActivity()).setShowViewPagerListener(this);

        //上面的状态点击
        Mystatus();



    }





    //网络请求部分 2.5（原来是在Activity中，原来也是在fragment中，但是之前没有做延迟，导致数据的集合为空）
    public void initnetword1() {
        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        //记得如果要修改的话javatojson这个方法里面也是要修改的 1.17
        final String s = EncodeBuilder.javaToJSON2( str);
        final String sign = EncodeBuilder.newString2(s);
        httphelper.create().saveDeviceNo1(Constants.TEST_HOME, sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                //这个暂时注释了
                LogUtil.e("---有数据么--"+s);
                HomeBean homeBean=new HomeBean();

                HomeBean homeBean1 = GsonUtil.parseJsonToBean(s, HomeBean.class);
                if (homeBean1==null){
                    //然后记得设置为空不可点击2.13
                    fail_fl.setVisibility(View.VISIBLE);
                    success_rl.setVisibility(View.GONE);
                    return;
                }

                if (homeBean1.status!=1){
                    fail_fl.setVisibility(View.VISIBLE);
                    success_rl.setVisibility(View.GONE);
                    return;
                }

                if (homeBean1.lastDocType==1){
                    excel.put("lastDocType","电子合同");
                }else if (homeBean1.lastDocType==2){
                    excel.put("lastDocType","实物包裹");

                    /**
                     *
                     * 这里不能为0，当没有值的时候默认就是0
                     * 这个lastDocType只有在用户登录才有，没登录的时候是没有的
                     * 3.5
                     *
                     */
                }else if (homeBean1.lastDocType==0){
                    excel.put("lastDocType","0");
                }

                try {

                    if (s.contains("waitReceive")&&s.contains("waitPayCount")&&s.contains("waitSign")) {
                        JSONObject jsonObject = new JSONObject(s);
                        String waitSign = jsonObject.getString("waitSign");
                        String waitPayCount = jsonObject.getString("waitPayCount");
                        String waitReceive = jsonObject.getString("waitReceive");

                        LogUtil.e(waitPayCount+"----"+waitReceive+"---"+waitSign);


                        //这里顺序不能乱
                        mystatus.add(waitReceive);
                        mystatus.add(waitSign);
                        mystatus.add(waitPayCount);



                        /***
                         *
                         *    这个是什么都没有的情况，没有登录的情况 2.28
                         *
                         *    下面未登录，和新用户登录的顺序不能变，变了也会报错，因为未登录的api没有lastdoctype这个字段
                         *
                         *
                         */

                        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");

                        //没登录没有lastdoctype，只能通过这个判断
                        if (waitPayCount.equals("0")&&waitReceive.equals("0")&&waitSign.equals("0")&&
                                !user_id.matches("[0-9]+")){

                            FirstComeAndNewUser(homeBean1);

                            //这个是未登录的时候
                            note_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loginCallBack.loginoutcallback(false);

                                }
                            });


                            //这里直接enable不能直接关掉swiperefreash，因为之前有过初始化操作
                            //因为没有登录，所以也不用再刷新了
                            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                   handler.postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           refreshLayout.setRefreshing(false);
                                       }
                                   },300);


                                }
                            });



                            return;

                        }

                        //这个是新用户，没有任何发件记录
                        /***
                         *
                         *  //这个是新用户，没有任何发件记录
                         *
                         */
                        if (excel.get("lastDocType").equals("0")){

                            note_tv.setText("我要寄件");
                            FirstComeAndNewUser(homeBean1);

                            note_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(Constants.QUICK,"nostatabar");
                                    getFragmentManager().beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.rlroot,toWebviewFragment)
                                            .commit();
                                }
                            });



                            networdcycle();



                            return;
                        }


                    }
                    //它这里写的有点乱，就自己拿出来解析吧1.30
                    JSONObject jsonObject = new JSONObject(s);
                    String lastDoc = jsonObject.getString("lastDoc");
                    //LogUtil.e("里面的数据是--"+lastDoc);
                    HomeExcel homeExcel = GsonUtil.parseJsonToBean(lastDoc, HomeExcel.class);

                    List<BannerInfo> button = homeExcel.button;
                    int size = button.size();
                    if (size!=0){
                        buttonmore.addAll(button);
                    }

//                    HomeExcel.receive_company;
//                    HomeExcel.create_time;
//                    HomeExcel.doc_no;
                    // excel = new HashMap<String, String>();
                    excel.put("receive_company",homeExcel.receive_company);
                    excel.put("receive_name",homeExcel.receive_name);
                    excel.put("user_company",homeExcel.user_company);
                    excel.put("sender_name",homeExcel.sender_name);
                    excel.put("doc_name",homeExcel.doc_name);
                    excel.put("doc_no",homeExcel.doc_no);
                    excel.put("create_time",homeExcel.create_time);
                    String doc_status = homeExcel.doc_status;
                    //这个是自己加的，用于保存寄件的状态 1.31
                    excel.put("circle_no",doc_status);
                    int role = homeExcel.role;
                    excel.put("role",String.valueOf(role));
                    switch (doc_status){
                        case "3":
                            excel.put("doc_status","已签收");
                            break;
                        case "88":
                            excel.put("doc_status","待支付");
                            break;
                        case "2":
                            excel.put("doc_status","拒收");
                            break;
                        case "0":
                            excel.put("doc_status","草稿");
                            break;
                        case "1":
                            if (role==1){
                                excel.put("doc_status","待他签收");
                            }else if (role==2){
                                excel.put("doc_status","待我签收");
                            }
                            break;
                        default:
                            excel.put("doc_status","草稿");
                            break;
                    }
                    LogUtil.e("----------"+homeExcel.receive_company+"--"+homeExcel.create_time+"--"+homeExcel.doc_no);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //因为之前再没网的时候加载过banner，所以这里就不在加载（这里只是简单判断bannerimage加在过的情况，没考虑bannerimager以后会增加的情况）

                    banner = homeBean1.banner;
                    for (int i = 0; i < banner.size(); i++) {
                        //BannerInfo bannerInfo = banner.get(i);
                        bannerImageList.add(Constants.IMAGE + banner.get(i).value);
                        //bannerImageList.add(Constants.IMAGE+banner.get(i).value);
                        bannerClickUrl.add(Constants.CONST_HOST1 + "/" + banner.get(i).url);

                        bannertitle.add(banner.get(i).desc);
                    }
                    //分别在集合的头和尾各加一张,实现无线轮播 2.27
                    bannerImageList.add(banner.size(), Constants.IMAGE + banner.get(0).value);
                    bannerImageList.add(0, Constants.IMAGE + banner.get(banner.size() - 1).value);



                    ArrayList<Appdata> app = homeBean1.app;
                    for (int i = 0; i < app.size(); i++) {
                        appiconList.add(app.get(i).value);
                        appurl.add(app.get(i).url);
                        apptitle.add(app.get(i).desc);

                    }


                showdata();

            }



            @Override
            public void fail(Exception e) {

                LogUtil.e("--连接失败--" + e);

                if (e!=null){
                    ToastUtil.showToast("hahaha");
                    success_rl.setVisibility(View.GONE);
                    fail_fl.setVisibility(View.VISIBLE);
                }




            }
        });

    }

    private void FirstComeAndNewUser(HomeBean homeBean1) {
        home_excle.setVisibility(View.GONE);
        no_work_excle.setVisibility(View.VISIBLE);

        banner = homeBean1.banner;
        for (int i = 0; i < banner.size(); i++) {
            //BannerInfo bannerInfo = banner.get(i);
            bannerImageList.add(Constants.IMAGE + banner.get(i).value);
            //bannerImageList.add(Constants.IMAGE+banner.get(i).value);
            bannerClickUrl.add(Constants.CONST_HOST1 + "/" + banner.get(i).url);

            bannertitle.add(banner.get(i).desc);
        }
        //分别在集合的头和尾各加一张,实现无线轮播 2.27
        bannerImageList.add(banner.size(), Constants.IMAGE + banner.get(0).value);
        bannerImageList.add(0, Constants.IMAGE + banner.get(banner.size() - 1).value);

        ArrayList<Appdata> app = homeBean1.app;
        for (int i = 0; i < app.size(); i++) {
            appiconList.add(app.get(i).value);
            appurl.add(app.get(i).url);
            apptitle.add(app.get(i).desc);

        }


        ViewGroup.LayoutParams layoutParams = gv_app.getLayoutParams();
        double i = calculateGridviewHeight();

        LogUtil.e("-----" + i);
        layoutParams.height = (int) i;

        gv_status.setAdapter(girdViewApadter);
        // GridViewAppAdapter gridViewAppAdapter = new GridViewAppAdapter();
        gv_app.setAdapter(gridViewAppAdapter);


        //状态栏的三个按钮
        initStatusData();

        initviewpager();


        gv_app.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //果然在跳转之前移除消息之后，再次回来就不会发生消息发送错乱 1.26
                // handler.removeCallbacksAndMessages(null);

                //这里有坑，天坑不是自己fragment里面嵌套webview回退的问题 1.30。目测是网页的问题
                ToWebviewFragment toWebviewFragment=ToWebviewFragment.newInstance(Constants.CONST_HOST1+"/"+appurl.get(position)
                        ,"nostatabar");



                /***暂时没有其他的回退栈可以用这个最大的 1.29 （除非主fragment中有含有回退栈的fragment可能会遇到坑）***/
                /**注意这个和Activity通信的fragment返回键的接口在监听webview的回退的时候，是“非”！！！ 1.29**/
                /***1.29***/
                getFragmentManager().beginTransaction()
                        .replace(R.id.rlroot,toWebviewFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        //上面的状态点击
        Mystatus();

        //初次进来即使是新用户，不卡顿（已登录）
        handler.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });



    }


    //轮询1.31
    public void networdcycle() {

        if (!Netutils.isNetworkAvalible(MyApplication.mcontext)) {
            ToastUtil.showToast("暂无网络");
            return;

        }
        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        //记得如果要修改的话javatojson这个方法里面也是要修改的 1.17
        final String s = EncodeBuilder.javaToJSON2( str);
        String sign = EncodeBuilder.newString2(s);
        httphelper.create().saveDeviceNo1(Constants.TEST_HOME, sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                //这个暂时注释了
                LogUtil.e("---有数据么--"+s);

                HomeBean homeBean1 = GsonUtil.parseJsonToBean(s, HomeBean.class);

                if (homeBean1==null){
                    //然后记得设置为空不可点击2.13
                    fail_fl.setVisibility(View.VISIBLE);
                    success_rl.setVisibility(View.GONE);
                    return;
                }

                if (homeBean1.status!=1){
                    fail_fl.setVisibility(View.VISIBLE);
                    success_rl.setVisibility(View.GONE);
                    return;
                }

                if (homeBean1.lastDocType==1){
                    excel.put("lastDocType","电子合同");
                }else if (homeBean1.lastDocType==2){
                    excel.put("lastDocType","实物包裹");
                }

                try {

                    JSONObject jsonObject=new JSONObject(s);

                    String waitSign = jsonObject.getString("waitSign");
                    String waitPayCount = jsonObject.getString("waitPayCount");
                    String waitReceive = jsonObject.getString("waitReceive");



                    mystatus.set(1,waitSign);
                    mystatus.set(0,waitReceive);
                    mystatus.set(2,waitPayCount);
                    girdViewApadter.notifyDataSetChanged();

                    /***
                     *
                     * 新用户没有最新数据的时候
                     *
                     */
                    if (homeBean1.lastDocType==0){
                        no_work_excle.setVisibility(View.VISIBLE);
                        home_excle.setVisibility(View.GONE);
                        note_tv.setText("我要寄件");
                        note_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(Constants.QUICK,"nostatabar");
                                getFragmentManager().beginTransaction()
                                        .addToBackStack(null)
                                        .replace(R.id.rlroot,toWebviewFragment)
                                        .commit();

                            }
                        });



                        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        networdcycle();
                                        refreshLayout.setRefreshing(false);
                                    }
                                },300);

                            }
                        });


                        return;
                    }

                    String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");

                    //没登录没有lastdoctype，只能通过这个判断（未登录的时候）
                    if (waitPayCount.equals("0")&&waitReceive.equals("0")&&waitSign.equals("0")&&
                            !user_id.matches("[0-9]+")){


                        return;

                    }

                    no_work_excle.setVisibility(View.GONE);
                    home_excle.setVisibility(View.VISIBLE);

                    String lastDoc = jsonObject.getString("lastDoc");
                    LogUtil.e("里面的数据是--"+lastDoc);
                    HomeExcel homeExcel = GsonUtil.parseJsonToBean(lastDoc, HomeExcel.class);

                    //这个是点击查看更多按钮
                    List<BannerInfo> button = homeExcel.button;
                    String receive_company = homeExcel.receive_company;
                    String receive_name = homeExcel.receive_name;
                    String sender_name = homeExcel.sender_name;
                    String doc_name = homeExcel.doc_name;
                    String doc_no = homeExcel.doc_no;
                    String create_time = homeExcel.create_time;
                    String user_company = homeExcel.user_company;
                    String doc_status = homeExcel.doc_status;

                   // if(excel.size()!=0&&(excel.get("lastDocType").equals("电子合同")||excel.get("lastDocType").equals("实物包裹"))) {

                    //进来不是新用户，已经发过文件的用户
                    if(excel.size()!=0&&excel.size()>1){
                        //旧数据
                       // BannerInfo oldbannerInfo = buttonmore.get(0);
                        String oldreceive_company = excel.get("receive_company");
                        String oldreceive_name = excel.get("receive_name");
                        String olduser_company = excel.get("user_company");
                        String oldsender_name = excel.get("sender_name");
                        String olddoc_name = excel.get("doc_name");
                        String olddoc_no = excel.get("doc_no");
                        String oldcreate_time = excel.get("create_time");
                        String olddoc_status = excel.get("circle_no");

                        int role = homeExcel.role;
                        //circle.put("circle_no",doc_status);

                        //订单号变了一般就是全部都变了 1.31
                        if (!olddoc_no.equals(doc_no)) {
                            excel.put("receive_company", homeExcel.receive_company);
                            excel.put("receive_name", homeExcel.receive_name);
                            excel.put("user_company", homeExcel.user_company);
                            excel.put("sender_name", homeExcel.sender_name);
                            excel.put("doc_name", homeExcel.doc_name);
                            excel.put("doc_no", homeExcel.doc_no);
                            excel.put("create_time", homeExcel.create_time);
                            //记录圈圈里面的状态
                            excel.put("circle_no", doc_status);
                            buttonmore.clear();
                            buttonmore.addAll(button);

                            switch (doc_status) {
                                case "3":
                                    excel.put("doc_status", "已签收");
                                    break;
                                case "88":
                                    excel.put("doc_status", "待支付");
                                    break;
                                case "2":
                                    excel.put("doc_status", "拒收");
                                    break;
                                case "0":
                                    excel.put("doc_status", "草稿");
                                    break;
                                case "1":
                                    if (role == 1) {
                                        excel.put("doc_status", "待他签收");
                                    } else if (role == 2) {
                                        excel.put("doc_status", "待我签收");
                                    }
                                    break;
                                default:
                                    excel.put("doc_status", "草稿");
                                    break;
                            }

                            String lastDocType = excel.get("lastDocType");
                            if (lastDocType.equals("实物包裹")){
                                excelbuttom.setVisibility(View.GONE);
                            }else {
                                excelbuttom.setVisibility(View.VISIBLE);
                            }

                            createtime.setText(create_time);
                            receivecompany.setText(receive_company);
                            receivename.setText(receive_name);
                            usercompany.setText(user_company);
                            sendername.setText(sender_name);
                            docname.setText(doc_name);
                            docno.setText(doc_no);
                            String newlastDocType = excel.get("lastDocType");
                            expresstype.setText(newlastDocType);
                            String newdoc_status = excel.get("doc_status");
                            circle.setText(newdoc_status);



                        }
                        //寄件状态的改变（这个就只用改中间的状态就行了）
                        else if (!olddoc_status.equals(doc_status)) {

                            buttonmore.clear();
                            buttonmore.addAll(button);

                            switch (doc_status) {
                                case "3":
                                    excel.put("doc_status", "已签收");
                                    break;
                                case "88":
                                    excel.put("doc_status", "待支付");
                                    break;
                                case "2":
                                    excel.put("doc_status", "拒收");
                                    break;
                                case "0":
                                    excel.put("doc_status", "草稿");
                                    break;
                                case "1":
                                    if (role == 1) {
                                        excel.put("doc_status", "待他签收");
                                    } else if (role == 2) {
                                        excel.put("doc_status", "待我签收");
                                    }
                                    break;
                                default:
                                    excel.put("doc_status", "草稿");
                                    break;
                            }
                            String newdoc_status = excel.get("doc_status");
                            circle.setText(newdoc_status);


                        }


                    }else{

                        //当从未登录头一回进入到已登录的时候，走的是这个情况3.5

                        int role = homeExcel.role;
                        //circle.put("circle_no",doc_status);

                        excel.put("role",String.valueOf(role));

                        //订单号变了一般就是全部都变了 1.31
                        excel.put("receive_company",homeExcel.receive_company);
                        excel.put("receive_name",homeExcel.receive_name);
                        excel.put("user_company",homeExcel.user_company);
                        excel.put("sender_name",homeExcel.sender_name);
                        excel.put("doc_name",homeExcel.doc_name);
                        excel.put("doc_no",homeExcel.doc_no);
                        excel.put("create_time",homeExcel.create_time);
                        //记录圈圈里面的状态
                        excel.put("circle_no",doc_status);
                        buttonmore.clear();
                        buttonmore.addAll(button);

                        switch (doc_status){
                            case "3":
                                excel.put("doc_status","已签收");
                                break;
                            case "88":
                                excel.put("doc_status","待支付");
                                break;
                            case "2":
                                excel.put("doc_status","拒收");
                                break;
                            case "0":
                                excel.put("doc_status","草稿");
                                break;
                            case "1":
                                if (role==1){
                                    excel.put("doc_status","待他签收");
                                }else if (role==2){
                                    excel.put("doc_status","待我签收");
                                }
                                break;
                            default:
                                excel.put("doc_status","草稿");
                                break;
                        }


                        //这里为了避免重名写个1
                        String role1 = excel.get("role");

                        if (role1.equals("1")){
                            circle.setBackgroundResource(R.drawable.status_sign);
                            tv_color.setBackgroundResource(R.color.excel_status_blue);
                            circle.setTextColor(getResources().getColor(R.color.excel_status_blue));
                        }else if (role1.equals("2")){
                            circle.setBackgroundResource(R.drawable.status_send);
                            tv_color.setBackgroundResource(R.color.excel_status_orange);
                            circle.setTextColor(getResources().getColor(R.color.excel_status_orange));
                        }

                        createtime.setText(create_time);
                        receivecompany.setText(receive_company);
                        receivename.setText(receive_name);
                        usercompany.setText(user_company);
                        sendername.setText(sender_name);
                        docname.setText(doc_name);
                        docno.setText(doc_no);

                        String lastDocType = excel.get("lastDocType");
                        if (lastDocType.equals("实物包裹")){
                            excelbuttom.setVisibility(View.GONE);
                        }else {
                            excelbuttom.setVisibility(View.VISIBLE);
                        }

                        String newlastDocType = excel.get("lastDocType");
                        expresstype.setText(newlastDocType);
                        String newdoc_status = excel.get("doc_status");
                        circle.setText(newdoc_status);

                        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        networdcycle();
                                        refreshLayout.setRefreshing(false);
                                    }
                                },300);

                            }
                        });



                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void fail(Exception e) {
                LogUtil.e("--连接失败--" + e);

            }
        });

    }






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

    //banner下面数据的具体地址 1.29（待支付里面的具体的地址） 1.31
    public void initStatusData(){
        /***下面这个地址并不是从api里面获取的，但是显示在外面的数字是从api里面获取的。。。2.27**/
        my_status_details_url.add(Constants.WITE_ME_RECEIVER);
        my_status_details_url.add(Constants.WITE_HE_RECEIVER);
        my_status_details_url.add(Constants.WITE_TO_PAY);

    }

    //按两下才能回退？ 1.29
    public void Mystatus(){
        gv_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");

                if (!user_id.matches("[0-9]+")){

                    /**
                     *
                     * 如果没有userid就直接，跳到登录页面
                     *
                     * */
                    loginCallBack.loginoutcallback(false);

                }else {
                    ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(my_status_details_url.get(position),"nostatabar");

                    getFragmentManager().beginTransaction()
                            .replace(R.id.rlroot, toWebviewFragment)
                            .addToBackStack(null)
                            .commit();

                }
            }
        });
    }


    //搜索全部文件 1.19
    public void initnetword(){

        //下面这个是搜索全部文件的方法
        //它这个userid和这里的userid是不同的,之前取的是value的值
        long l = System.currentTimeMillis();
        String  str=String.valueOf(l);
        //记得如果要修改的话javatojson这个方法里面也是要修改的 1.17
        String s = EncodeBuilder.javaToJSON3("84", str,"0","123","1","send");
        String sign = EncodeBuilder.newString2(s);
        httphelper.create().saveDeviceNo3(Constants.TEST_DOCLIST, "0","84","123","1","send", sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                Log.e("-Log data success-",s);

            }

            @Override
            public void fail(Exception e) {
                Log.e("-Fail data-",e+"");

            }
        });
    }





    private void initviewpager() {

        for (int i = 0; i < bannerImageList.size(); i++) {
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
                    .load(bannerImageList.get(i))
                    .apply(options)
                    .into(imageView);

            imageViews.add(imageView);//把数据添加到集合中。

        }
        intitPoint();
        viewPager.setAdapter(new MyPageradpter());
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        //tv.setText(imagetitle[preposition]);//这个是为了让第一个标题和xml中的标题同步
        //下面这个参数是为了定位到某个位置
        //int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageViews.size();//要保证是5的整数倍，没有为什么。不然可能会出错
        viewPager.setCurrentItem(Fig_current,false);
        handler.sendEmptyMessageDelayed(0, 2500);
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

    @Override
    public void refreashcallback() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networdcycle();
            }
        });

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
                    ToWebviewFragment toWebviewFragment=ToWebviewFragment.newInstance(bannerClickUrl.get(position-1),"nostatabar");

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


    /***
     *
     * 这个接口当时想的是banner跳转了之后不是全屏的效果
     * （但是现在的banner跳转之后是全屏的效果） 1.26（）
     * fragment点击事件的穿透是发生在子fragment里面没有布局的时候
     * 如果在xml属性里面设置了那个不会让点击事件穿透的简单做法，可以会导致子fragment本身的点击事件无法响应！！
     *
     *
     */
    @Override
    public void showviewpager() {
        viewPager.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(0,2500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchview1:

                //  handler.removeCallbacksAndMessages(null);


                getFragmentManager().
                        beginTransaction().
                        addToBackStack(null).
                        replace(R.id.rlroot, new SearchFragment()).
                        commit();

                break;

            case R.id.iv_more:
                /**
                 *
                 * 下面这个的灵感是来自文件管理的popup弹窗
                 *
                 */
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                //因为这个最外层的布局是linearlayout，所以这里要强转为linearlayout
                LinearLayout popliner= (LinearLayout) inflater.inflate(R.layout.home_excel_do1,null,false);
                GridView gv_more= (GridView) popliner.findViewById(R.id.gv_more);
                GirdviewMoreAdapter girdviewMoreAdapter=new GirdviewMoreAdapter();
                gv_more.setAdapter(girdviewMoreAdapter);
                gv_more.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView pop_tv_more= (TextView) view.findViewById(R.id.pop_tv_more);
                        // ToastUtil.showToastCenter(pop_tv_more.getText().toString().trim());
                        ToWebviewFragment toWebviewFragment =
                                ToWebviewFragment.newInstance(Constants.CONST_HOST1 + buttonmore.get(position).url,"nostatabar");
                        getFragmentManager().beginTransaction()
                                .add(R.id.rlroot,toWebviewFragment)
                                .addToBackStack(null)
                                .commit();
                        popupWindow.dismiss();

                    }
                });

                popupWindow = new PopupWindow(popliner,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,true);

                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(more,0,10);// 在控件下方显示popwindow
                popupWindow.update();


                break;

            //网络错误重新加载
            case  R.id.btn_reload:

                if (Netutils.isNetworkAvalible(MyApplication.mcontext)) {
                    fail_fl.setVisibility(View.GONE);
                    success_rl.setVisibility(View.VISIBLE);
                    refreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(true);
                            initialize();

                        }
                    });

                } else {
                    return;
                }

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
                            .add(R.id.rlroot, toWebviewFragment)
                            .commit();

                }
                break;
        }

    }



    //这里暂时不需要这个模块（秒签文章）
    //下面这个是单个布局的（recyclerview单布局的显示）
    class Myadapter extends RecyclerView.Adapter<Myholder> {

        @Override
        public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
            //View view = View.inflate(parent.getContext(), R.layout.recycler_view, null);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);
            return new Myholder(view);
        }

        @Override
        public void onBindViewHolder(Myholder holder, int position) {
            holder.articleContent.setText("赞");
            holder.articleTitle.setText("秒签文章");
            holder.iv.setImageResource(R.drawable.url_logo);

        }

        @Override
        public int getItemCount() {
            return 4;
        }


    }

    class Myholder extends RecyclerView.ViewHolder {

        @BindView(R.id.article_title)
        TextView articleTitle;
        @BindView(R.id.article_content)
        TextView articleContent;
        @BindView(R.id.iv)
        ImageView iv;

        public Myholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public static LoginCallBack loginCallBack;

    public static void setLoginCallBack(LoginCallBack loginCallBack){
        WorkFragment1.loginCallBack=loginCallBack;

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
        if (banner!=null) {
            banner.clear();
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
        super.onDestroy();
    }
}