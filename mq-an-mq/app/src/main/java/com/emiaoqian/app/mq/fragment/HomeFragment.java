package com.emiaoqian.app.mq.fragment;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.emiaoqian.app.mq.Interface.FragmentCallback;
import com.emiaoqian.app.mq.Interface.LoginCallBack;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;


/**
 * Created by xiong on 2017/10/10.
 */

public class HomeFragment extends BaseWebFragment {


    @Override
    public void initialize() {
        super.initialize();

        //修改关于秒签的回退，以及登场方式 4.8
        //eventbus当页面发生跳转的时候，再跳回退，好像回档了。。。就是之前的改变的某个状态不见了


        //BaseWebFragment.setgetrealurlCallback(this);

        imagebutton.setVisibility(View.VISIBLE);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ToWebviewFragment toWebviewFragment = ToWebviewFragment.
//                        newInstance(Constants.ABOUT_ME, getResources().getString(R.string.about_me),"nostatabar");

                ToWebviewFragment toWebviewFragment = ToWebviewFragment.
                        newInstance(Constants.ABOUT_ME, "nostatabar");

                toWebviewFragment.setTargetFragment(HomeFragment.this,200);

                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.rlroot,toWebviewFragment)
                        .commit();



            }
        });

        title.setText("个人中心");
        urlDefault =Constants.TESTURL;
        loadUrl(Constants.TESTURL);

//        urlDefault="https://www.emiaoqian.com/sys/pub/login";
//        loadUrl("https://www.emiaoqian.com/sys/pub/login");


        /**
         *  这个获取到的地址是头一回的地址，不是最终的地址。
         *  还是要把最终获取到的地址取出来 4.8
         *
         *
         */


    }


    public static LoginCallBack loginCallBack;

    public static void setLoginCallBack(LoginCallBack loginCallBack){
        HomeFragment.loginCallBack=loginCallBack;

    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e("--233onPause-","onPause");
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK){
            return;
        }else  if (requestCode==200) {
            String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");

            if (!user_id.matches("[0-9]+")) {
                loginCallBack.loginoutcallback(true);
            }
        }
    }
}














