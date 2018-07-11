package com.emiaoqian.app.mq.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.emiaoqian.app.mq.Interface.ChangeStateBarCallbcak;
import com.emiaoqian.app.mq.Interface.FragmentCallback;
import com.emiaoqian.app.mq.Interface.LoginCallBack;
import com.emiaoqian.app.mq.Interface.ViewPagerCallBack;
import com.emiaoqian.app.mq.Interface.WebWiewCallback;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.Appdata;
import com.emiaoqian.app.mq.bean.BannerInfo;
import com.emiaoqian.app.mq.bean.HomeBean;
import com.emiaoqian.app.mq.bean.Newsbean;
import com.emiaoqian.app.mq.fragment.AboutFragment;
import com.emiaoqian.app.mq.fragment.BaseWebFragment;
import com.emiaoqian.app.mq.fragment.FileFragment;
import com.emiaoqian.app.mq.fragment.HomeFragment;
import com.emiaoqian.app.mq.fragment.LoginFragment;
import com.emiaoqian.app.mq.fragment.MeFragment;
import com.emiaoqian.app.mq.fragment.MessageFragment;
import com.emiaoqian.app.mq.fragment.Mymessagefragment;
import com.emiaoqian.app.mq.fragment.WorkFragment;
import com.emiaoqian.app.mq.fragment.WorkFragment2;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.MessageEvent;
import com.emiaoqian.app.mq.utils.PopupMenuUtil;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.google.gson.reflect.TypeToken;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMShareAPI;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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


public class Mainactivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener ,
        LoginCallBack{


    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.rdbt1)
    RadioButton rdbt1;
    @BindView(R.id.rdbt2)
    RadioButton rdbt2;
    @BindView(R.id.rdbt3)
    RadioButton rdbt3;
    @BindView(R.id.rdbt4)
    RadioButton rdbt4;
    @BindView(R.id.add_more)
    ImageView addMore;
    private ActionBar supportActionBar;
    public ArrayList<Fragment> fragments;
    private boolean isexit = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isexit = false;
        }
    };
    public RadioGroup radioGroup;

    public static ArrayList<String> bannerImageList = new ArrayList<>();

    public static ArrayList<String> appiconList = new ArrayList<>();

    public static ArrayList<String> appurl=new ArrayList<>();

    public static ArrayList<String> mystatus=new ArrayList<>();

    public static ArrayList<String>  addbutton=new ArrayList<>();
    public static ArrayList<String>  addbuttonurl=new ArrayList<>();

    public static ArrayList<String>  addbuttontitle=new ArrayList<>();

    //轮播图的回调
    static ViewPagerCallBack ViewPagerCallBack;
    private ArrayList<BannerInfo> banner;
    public static HashMap<String, String> excel=new HashMap<>();
//    private WorkFragment1 workFragment1  = new WorkFragment1();
//    WorkFragment1 newworkFragment1=new WorkFragment1();

    // public static HashMap<String,String> circle=new HashMap<>();

    public static void setShowViewPagerListener(ViewPagerCallBack ViewPagerCallBack) {
        Mainactivity.ViewPagerCallBack = ViewPagerCallBack;

    }

    public  int dp2px( float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public  int px2dp( float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail1);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        ButterKnife.bind(this);


        //友盟推送10.24
        PushAgent.getInstance(this).onAppStart();
        rdbt1.setOnClickListener(this);
        rdbt2.setOnClickListener(this);
        rdbt3.setOnClickListener(this);
        rdbt4.setOnClickListener(this);
        addMore.setOnClickListener(this);

        Drawable drawable = getResources().getDrawable(R.drawable.select_work);
        drawable.setBounds(0,0,getResources().getDimensionPixelOffset(R.dimen.x18),getResources().getDimensionPixelOffset(R.dimen.x18));
        rdbt1.setCompoundDrawables(null,drawable,null,null);

        Drawable drawable1 = getResources().getDrawable(R.drawable.select_send);
        drawable1.setBounds(0,0,getResources().getDimensionPixelOffset(R.dimen.x18),getResources().getDimensionPixelOffset(R.dimen.x18));
        rdbt2.setCompoundDrawables(null,drawable1,null,null);


        Drawable drawable2 = getResources().getDrawable(R.drawable.select_find);
        drawable2.setBounds(0,0,getResources().getDimensionPixelOffset(R.dimen.x15),getResources().getDimensionPixelOffset(R.dimen.x18));
        rdbt3.setCompoundDrawables(null,drawable2,null,null);


        Drawable drawable3 = getResources().getDrawable(R.drawable.select_me);
        drawable3.setBounds(0,0,getResources().getDimensionPixelOffset(R.dimen.x15),getResources().getDimensionPixelOffset(R.dimen.x18));
        rdbt4.setCompoundDrawables(null,drawable3,null,null);


        rdbt1.setOnLongClickListener(this);
        rdbt2.setOnLongClickListener(this);
        rdbt3.setOnLongClickListener(this);
        rdbt4.setOnLongClickListener(this);
        //imagebutton.setOnClickListener(this);
        initFragment();

        //注册
        EventBus.getDefault().register(this);

        switchFragment(0);
        rdbt1.setChecked(true);

        //这里的网络请求是为了“+”号数据的加载，不能省去！！
        //initnetword1("express");

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        //is 华为手机刘海分辨率1080--2150
        LogUtil.e("手机", "display is" + metrics.widthPixels + "--" + metrics.heightPixels);

        LogUtil.e("--四舍五入的这个数是多少---", Math.round(3.0) + "");
        //aa();

        BaseWebFragment.setLoginCallBack(this);

       // WorkFragment1.setLoginCallBack(this);

        //修改关于秒签 4.8
        HomeFragment.setLoginCallBack(this);

        LoginFragment.setLoginCallBackListener(this);

    }





    @Override
    protected void onResume() {
        super.onResume();
       // initnetword1();
        LogUtil.e("---1onresume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("---1onpause");
    }




    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        addbutton.clear();
        addbuttonurl.clear();

        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void urlChanged(MessageEvent messageEvent) {
        if (messageEvent.code == MessageEvent.URL_LOGIN) {
            toogleTitleAndToolbar(false);
            setNeedReloadUrl(messageEvent);
            LogUtil.e("eventbus change--" + messageEvent.getClass().getSimpleName());
        } else if (messageEvent.code == MessageEvent.URL_LOGOUT) {
            setNeedReloadUrl(messageEvent);
            toogleTitleAndToolbar(true);
            LogUtil.e("eventbus change--" + messageEvent.getClass().getSimpleName());
        } else if (messageEvent.code == MessageEvent.URL_SECOND) {
            setNeedReloadUrl(messageEvent);
            rdbt1.setChecked(true);
            switchFragment(0);
            toogleTitleAndToolbar(true);
            LogUtil.e("eventbus change--" + messageEvent.getClass().getSimpleName());
        } else if (messageEvent.code == MessageEvent.URL_COMMON) {
            //setNeedReloadUrl(messageEvent); 只要这个eventbus，这个是普通连接的消息，然后基类就不会走onhiddenchange方法11.16
            toogleTitleAndToolbar(true);
            LogUtil.e("eventbus change--" + messageEvent.getClass().getSimpleName());
        }
    }

    public void setNeedReloadUrl(MessageEvent messageEvent) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment f = fragments.get(i);
            if (f instanceof BaseWebFragment) {
                if (!f.getClass().getSimpleName().equals(messageEvent.message)) {
                    //是为了能让Activity能引用BaseWebFragment里面的变量
                    ((BaseWebFragment) f).needReloadUrl = true;
                    LogUtil.e(MessageEvent.TAG, String.format("setNeedReloadUrl: %s", f.getClass().getSimpleName() + "--" + f.equals(true)));
                }
            }
        }
    }

    public void toogleTitleAndToolbar(boolean show) {
        if (show) {
            radioGroup.setVisibility(View.VISIBLE);
            addMore.setVisibility(View.VISIBLE);
        } else {
            radioGroup.setVisibility(View.GONE);
            addMore.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rdbt1:

                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                LogUtil.e("--为空KKKKKKKKKKKKKKKKKKKKKKKKKKKKK---"+user_id.isEmpty());
                /**
                 *这个空很奇怪，不是字符串的那种空，也不为null。2.24
                 *
                 */
//                if (user_id.matches("[0-9]+")){
//                    switchFragment(0);
//                }
//                else {
//                    switchFragment(5);
//                }

                switchFragment(0);



                LogUtil.e("执行了么按钮1");
                break;
            case R.id.rdbt2:
                switchFragment(1);
                LogUtil.e("执行了么按钮2");
                break;
            case R.id.rdbt3:
                switchFragment(2);
                LogUtil.e("执行了么按钮3");
                break;
            case R.id.rdbt4:
                switchFragment(3);
                LogUtil.e("执行了么按钮4");
                break;
            case R.id.imagebutton:
                switchFragment(4);
                radioGroup.setVisibility(View.VISIBLE);
                break;

            /**底部导航栏的添加更多按钮**/
            case R.id.add_more:


//                LogUtil.w("----被点击了么");
//                PopupWindow popupWindow = new PopupWindow(this);
//                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_style1, null));
//                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
//                popupWindow.setOutsideTouchable(false);
//                popupWindow.setFocusable(true);
//                //关于现实有两种方法，一种方法是在某个控件的下方 1.25
//                //showAtLocation(addMore, Gravity.NO_GRAVITY,0,0) 这个就是正常显示添加的布局，添加的布局长什么样就怎么添加 1.25
//                popupWindow.showAtLocation(addMore, Gravity.NO_GRAVITY,0,0);


                PopupMenuUtil.getInstance()._show(Mainactivity.this, addMore);

                break;

        }

    }

    @Override
    public boolean onLongClick(View v) {
        if (v instanceof RadioButton && ((RadioButton) v).isChecked()) {
            if (v.getId() == R.id.rdbt1) {
                BaseWebFragment bwf = (BaseWebFragment) fragments.get(0);
                bwf.loadUrl(null);
            } else if (v.getId() == R.id.rdbt2) {
                BaseWebFragment bwf = (BaseWebFragment) fragments.get(1);
                bwf.loadUrl(null);
            } else if (v.getId() == R.id.rdbt3) {
                BaseWebFragment bwf = (BaseWebFragment) fragments.get(2);
                bwf.loadUrl(null);
            } else if (v.getId() == R.id.rdbt4) {
                BaseWebFragment bwf = (BaseWebFragment) fragments.get(3);
                bwf.loadUrl(null);
            }
        }
        return false;
    }

    public  void switchFragment(int j) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment f = fragments.get(i);
            if (i == j) {
                if (f.isAdded()) {
                    //已经添加过，直接展示
                    beginTransaction.show(f);
                } else {
                    //并没有添加，则把fragment添加进来
//                    if (i == 4) {
//                        beginTransaction.add(R.id.fl, f).addToBackStack(null);
//                    }
//                    else if (i==0){
//                        beginTransaction.add(R.id.fl,f,"first");
//                    }
//                    else {
//                        beginTransaction.add(R.id.fl, f);
//                        //beginTransaction.addToBackStack(null);
//                    }
                    beginTransaction.add(R.id.fl,f,f.getClass().getSimpleName());

                }
            } else {
                if (f.isAdded()) {
                    //当遍历的和当前的不相等的时候，把这些全部隐藏起来。
                    beginTransaction.hide(f);
                }
            }
        }
        //最后一定要记得提交
        beginTransaction.commit();
    }


    public void initFragment() {
        LogUtil.e("BeginActivity", "执行了么");
        fragments = new ArrayList<>();



        //fragments.add(workFragment1);

        fragments.add(new WorkFragment2());

        //fragments.add(new MessageFragment());
        fragments.add(new Mymessagefragment());

        //fragments.add(new aaa());
        fragments.add(new FileFragment());
        //fragments.add(new FileMangerFragment1());
        // fragments.add(new TimeClickFragment());

       // fragments.add(new HomeFragment());  //1.25
        fragments.add(new MeFragment());


        //fragments.add(new newfragment());
        fragments.add(new AboutFragment());


        fragments.add(new WorkFragment());



    }


    public static WebWiewCallback webWiewCallback;

    public static void getWebviewlastpager(WebWiewCallback webWiewCallback) {
        Mainactivity.webWiewCallback = webWiewCallback;
    }

    public static FragmentCallback.ChildFragmentWebCallback childFragmentWebCallback;
    public static void getChildFragmentWebviewCallback(FragmentCallback.ChildFragmentWebCallback childFragmentWebCallback){
        Mainactivity.childFragmentWebCallback=childFragmentWebCallback;

    }








    @Override
    public void onBackPressed() {
      /**
       *
       * 就是把fragment里面的webview的回退在popstackbcak要执行之前再监听一遍，没有值的时候再退出去 1.28完美解
       * 决子fragment中的webview直接退出到付fragment，而不走webview的回退 1.28
       *
       *
       * （下面的判断顺序不能换啊，换了可能会出错 2.3）
       *
       *
       * */
        if (childFragmentWebCallback!=null&&childFragmentWebCallback.childfragmentwebcallback()){
            //childFragmentWebCallback.childfragmentwebcallback();
            //注意下面这两个方法是有区别的，一个是childfragmentwebcancallback一个是childfragmentwebcallback 2.3
            childFragmentWebCallback.childfragmentwebcancallback();

            LogUtil.e("--233当前的接口是--"+childFragmentWebCallback.getClass().getSimpleName());
        }

        else if (webWiewCallback != null && webWiewCallback.CanCallback()) {
        //else if (webWiewCallback.CanCallback()) {

            //super.onBackPressed();
            LogUtil.e("--666当前的接口是--"+webWiewCallback.getClass().getSimpleName());
            webWiewCallback.BackLastCallback();
        }

        //当popupwindows显示的时候按物理回退键能够返回
        else if (PopupMenuUtil.getInstance()._isShowing()){
            PopupMenuUtil.getInstance()._rlClickAction();
        }


        else if (isexit) {
                super.onBackPressed();
            } else {
                isexit = true;
                ToastUtil.showToast("再按一次退出");
                handler.sendEmptyMessageDelayed(0, 2000);
                //super.onBackPressed();
                LogUtil.e("栈里面的数量--" + getSupportFragmentManager().getBackStackEntryCount());
            }
    }

    /***友盟分享**/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }


    @Override
    public void logincallback() {

//        rdbt1.setChecked(true);
//        switchFragment(0);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rdbt1.setChecked(true);
                switchFragment(0);

            }
        });

    }

    @Override
    public void loginoutcallback(boolean isshow) {
        if (!isshow) {
            switchFragment(5);
        }
        else {
            radioGroup.setVisibility(View.GONE);
            addMore.setVisibility(View.GONE);
        }
    }




}
