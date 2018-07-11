package com.emiaoqian.app.mq.fragment;


import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.LogUtil;


/**
 * Created by xiong on 2017/10/10.
 */

public class WorkFragment extends BaseWebFragment {

    @Override
    public void initialize() {
        super.initialize();

        title.setText("工作台");
        urlDefault =Constants.LOGIN;
        loadUrl(Constants.LOGIN);

//        urlDefault="https://www.emiaoqian.com";
//        loadUrl("https://www.emiaoqian.com");
    }



}