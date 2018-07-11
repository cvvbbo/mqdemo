package com.emiaoqian.app.mq.fragment;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;

import com.emiaoqian.app.mq.Interface.FragmentCallback;
import com.emiaoqian.app.mq.Interface.WebWiewCallback;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.other.CustomShareListener;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.MessageEvent;
import com.emiaoqian.app.mq.utils.StatebarUtils2;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.jaeger.library.StatusBarUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiong on 2017/10/10.
 */

public abstract class BaseFragment extends Fragment implements FragmentCallback.ChildFragmentWebCallback{


    //父类的共有成员变量子类能够共享
    //然后这种写法是在谷歌市场里面！！！
    public View view;
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    Activity mActivity;
   // Handler handler=new Handler();
    private String sign;
    private StatebarUtils2 statebar;

    // private String getClassname=null;


    /***
     *
     * 这个还是不出现，很神奇很费解。。4.10
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        LogUtil.e("--10086setUserVisibleHint是否返回--"+this.getClass().getSimpleName()+"--"+getUserVisibleHint());

        //super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onDestroy() {
        LogUtil.e("---10086onDestroy---"+getClass().getSimpleName());
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.e("---10086onCreate---"+getClass().getSimpleName());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        LogUtil.e("---10086onPause---"+getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        LogUtil.e("---10086onAttach---"+getClass().getSimpleName());
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        LogUtil.e("---10086onDetach---"+getClass().getSimpleName());
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        LogUtil.e("---10086onDestroyView---"+getClass().getSimpleName());
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        LogUtil.e("---10086onResume---"+getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onStop() {
        LogUtil.e("---10086onStop---"+getClass().getSimpleName());
        super.onStop();
    }

    @Override
    public void onStart() {
        LogUtil.e("---10086onStart---"+getClass().getSimpleName());
        super.onStart();
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtil.e("--10086onHiddenChanged是否隐藏--"+hidden+"--当前的类-"+getClass().getSimpleName());
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        statebar = new StatebarUtils2(this);

        //这里不能添加父容器1.24 只能添加子容器
       // view = View.inflate(getActivity(), getlayout(), container);
        /*********用这种添加布局的方式，！！！！（万无一失）***********/
        view=inflater.inflate(getlayout(),container,false);
        mShareListener = new CustomShareListener();
        //最简单的防止fragment视图点击穿透的方法 （不然就只能用自己那种复杂的方式）1.26
        view.setClickable(true);

        // TODO: 2018/6/26  4.4x沉浸式状态栏修改之前
        if (Build.VERSION.SDK_INT>=21){
            Window window = getActivity().getWindow();
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.white));
            //设置系统状态栏处于可见状态
            //把状态栏的字体改为黑色 https://blog.csdn.net/zhangyiminsunshine/article/details/68064926
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //让view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
       // Mainactivity.getChildFragmentWebviewCallback(this);
        initialize();

    }

    //低于5.0才需要的沉浸式状态栏的改变
    public void lessApi19NeedStatarbarHeight(View view){
            int statusBarHeight = statebar.getStatusBarHeight();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.setMargins(0,statusBarHeight,0,0);
            view.setLayoutParams(marginLayoutParams);


    }


    public static WebWiewCallback webWiewCallback;
    public static void getWebviewlastpager(WebWiewCallback webWiewCallback) {
        BaseFragment.webWiewCallback = webWiewCallback;
    }


    //这个是执行webview不再回退之后，执行fragment的回退 2.3
    @Override
    public void childfragmentwebcancallback() {
        getFragmentManager().popBackStack();

    }

    //这个是判断fragment里面嵌套的webview能不能回退 2.3
    //简单的来说就是 先监听webview里面的回退，这个webview是腾讯的内核，它的回退方式和不同安卓的webview不一样 2.3
    //然后把腾讯的webview全部退完之后再去退出fragment的回退 2.3
    @Override
    public boolean childfragmentwebcallback() {
//        int count = getFragmentManager().getBackStackEntryCount();
//        LogUtil.e("cout------"+count);
        /***
         *
         * 这个版本的是未登录状态下的修改回退的bug。。对回退bug就是这么坑爹。。
         *主要是在basefragment中添加判断webview的返回接口是不是这个类的
         *
         * 因为当从未登录到登录的时候，先是走webview，然后再是界面上的转跳，这时候webview的回退接口记录了一回
         * 这个类引用了webview的接口，然后跳到别的类的时候继续引用着上一个引用过webview接口的类，即使当前类没有引用webvie接口
         * ，然后接口还是记录了上一次的引用。导致不能回退成功。。4.4
         *
         */
        if (webWiewCallback!=null&&this.getClass().getSimpleName().equals(webWiewCallback.getClass().getSimpleName())) {
            LogUtil.e("--当前webview的接口的谁的--"+webWiewCallback.getClass().getSimpleName());
            if (getFragmentManager()!=null&&getFragmentManager().getBackStackEntryCount() != 0 && !webWiewCallback.CanCallback()) {
                //getFragmentManager().popBackStack();
                return true;
            } else {
                return false;
            }
        }else {

            if (getFragmentManager()!=null&&getFragmentManager().getBackStackEntryCount() != 0) {
                //getFragmentManager().popBackStack();
                return true;
            } else {
                return false;
            }

        }
    }



    public abstract int getlayout();

    public  abstract void initialize();







}
