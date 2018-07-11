package com.emiaoqian.app.mq.fragment;


import com.emiaoqian.app.mq.utils.Constants;


/**
 * Created by xiong on 2017/10/10.
 */

public class AboutFragment extends BaseWebFragment {

    @Override
    public void initialize() {
        super.initialize();
        title.setText("关于秒签");
        urlDefault =Constants.ABOUT_ME;
        loadUrl(Constants.ABOUT_ME);
    }
}