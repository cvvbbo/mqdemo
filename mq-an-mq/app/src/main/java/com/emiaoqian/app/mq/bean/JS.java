package com.emiaoqian.app.mq.bean;

import android.webkit.JavascriptInterface;

import com.emiaoqian.app.mq.utils.LogUtil;

/**
 * Created by xiong on 2017/10/19.
 */

public class JS {

    @JavascriptInterface
    public void get(String a){
        // System.out.println(jsonObject);
        LogUtil.e(a+"a--------------");
    }
}
