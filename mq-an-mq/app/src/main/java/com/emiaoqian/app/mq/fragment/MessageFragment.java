package com.emiaoqian.app.mq.fragment;


import com.emiaoqian.app.mq.utils.Constants;

/**
 * Created by xiong on 2017/10/10.
 */

public class MessageFragment extends BaseWebFragment {

    @Override
    public void initialize() {
        super.initialize();
        title.setText("我的消息");
        urlDefault =Constants.MESSAGE;
        loadUrl(Constants.MESSAGE);


//        urlDefault="https://www.emiaoqian.com";
//        loadUrl("https://www.emiaoqian.com");
    }
}