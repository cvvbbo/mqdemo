package com.emiaoqian.app.mq.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.emiaoqian.app.mq.Interface.ChangeStateBarCallbcak;
import com.emiaoqian.app.mq.Interface.FragmentCallback;
import com.emiaoqian.app.mq.Interface.Getrealurlcallback;
import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.Interface.WebWiewCallback;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.activity.WebviewtoNewActivity;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.StatebarUtils2;

/**
 * Created by xiong on 2018/1/25.
 *
 * 关于左上角的返回键只是在刚这个页面需要显示出来！！！
 * 大不了不用eventbus。。。
 * 把toolbar直接显示出来！！！2.6
 *
 *
 */

public class ToWebviewFragment extends BaseWebFragment implements MyCallback.WebviewGettitle,Getrealurlcallback{


    private StatebarUtils2 statebar;
    private String mParam1;

    public static ToWebviewFragment newInstance(String url, String statabarshow) {
      ToWebviewFragment fragment = new ToWebviewFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("statabarshow",statabarshow);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void initialize() {
        super.initialize();
        BaseWebFragment.getWebviewtitlecallback(this);
        Mainactivity.getChildFragmentWebviewCallback(this);
       // WebviewtoNewActivity.getWebviewlastpager(this);

        if (getArguments()!=null) {
            statebar = new StatebarUtils2(this);
            if (Build.VERSION.SDK_INT<21){
                lessApi19NeedStatarbarHeight(title_rl);
            }
            //在这里加上这个回退时为了中间的那个按钮，不然的话中间的按钮直接退出了
            mParam1 = getArguments().getString("url");
            LogUtil.w("转跳的地址是--233"+ mParam1);

            //loadUrl(mParam1);
            loadUrl(mParam1);

        }


        returnIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        returnIm_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        closeIm_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        closeIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        //获取webview上真正的地址，用于登录和退出。7.5
        BaseWebFragment.setGetrealurlcallbacklistener(this);

    }

    //沉浸式状态栏的切换
    public static ChangeStateBarCallbcak changeStateBarCallbcak;
    public static void setChangeStateBarListener(ChangeStateBarCallbcak changeStateBarCallbcak){
        ToWebviewFragment.changeStateBarCallbcak=changeStateBarCallbcak;

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e("233ToWebviewFragment--"+"onPause");
    }

    @Override
    public void onResume() {
        LogUtil.e("233ToWebviewFragment--"+"onResume");
        super.onResume();

    }

    @Override
    public void onDestroy() {

        if (getTargetFragment()!=null) {

            Intent intent = new Intent();
            intent.putExtra("isshow", "show");
            getTargetFragment().onActivityResult(200, Activity.RESULT_OK, intent);
        }

        String statabarshow = getArguments().getString("statabarshow");
        if (statabarshow.equals("nostatabar")){
            if (changeStateBarCallbcak!=null) {
                changeStateBarCallbcak.changestatebarcallbcak();
            }
        }

       // WebviewtoNewActivity.getWebviewlastpager(null);
        LogUtil.e(getActivity()+"");
            Mainactivity.getChildFragmentWebviewCallback(null);
        LogUtil.e("233ToWebviewFragment--"+"onDestroy");
        super.onDestroy();
    }

    /***
     * 这里来获取webview
     *
     * @param webviewtitle
     */
    @Override
    public void GetWebviewtitle(String webviewtitle) {
        if (webviewtitle.length()>8){
            menu_title.setText(webviewtitle.substring(0,8)+"...");
        }else {
            menu_title.setText(webviewtitle);
        }
    }

    @Override
    public String Getrealurlcallback() {
        return mParam1;
    }


}
