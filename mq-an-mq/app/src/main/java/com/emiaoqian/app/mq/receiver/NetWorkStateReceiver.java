package com.emiaoqian.app.mq.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.Interface.RefreashCallBack;
import com.emiaoqian.app.mq.fragment.LoginFragment;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.Netutils;
import com.emiaoqian.app.mq.utils.ToastUtil;

/**
 * Created by xiong on 2018/7/2.
 */

public class NetWorkStateReceiver extends BroadcastReceiver {


    //首页的接口
    public  MyCallback.RefreashHomePage refreashHomePage;
    public  void setrefreashcallback(MyCallback.RefreashHomePage refreashHomePage){
        this.refreashHomePage=refreashHomePage;

    }


    //消息页面的接口
    public  MyCallback.RefreashMessage refreashMessage;
    public  void setRefreashMessageListener(MyCallback.RefreashMessage refreashMessage){
       this.refreashMessage=refreashMessage;
    }

    public MyCallback.RefreashMePage refreashMePage;
    public void setRefreashMePageListener(MyCallback.RefreashMePage refreashMePage){
        this.refreashMePage=refreashMePage;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Netutils.isNetworkAvalible(context)){
            ToastUtil.showToast("当前网络不可用请检查网络");
            Netutils.checkNetwork((Activity) context);
        }else {
            if (refreashHomePage!=null) {
                refreashHomePage.RefreashHomepagecallback();
            }else if (refreashMessage!=null){
                refreashMessage.RefreashMessage();
            }else if (refreashMePage!=null){
                refreashMePage.RefreashMepagecallback();
            }

        }

    }
}
