package com.emiaoqian.app.mq.fragment;


import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.StatebarUtils2;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;


/**
 * Created by xiong on 2017/10/10.
 */

public class FileFragment extends BaseWebFragment implements MyCallback.WebviewGettitle{

    private StatebarUtils2 statebar;
    private boolean isfirst=true;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden==false) {
            statebar.whitestatus(this);
            String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
            if (user_id.matches("[0-9]+")&&isfirst){
                loadUrl(Constants.MQNEWS);
                isfirst=false;
            }
        }

    }

    @Override
    public void initialize() {
        super.initialize();
        statebar = new StatebarUtils2(this);
        if (Build.VERSION.SDK_INT<21){

            lessApi19NeedStatarbarHeight(title_rl);
        }
        BaseWebFragment.getWebviewtitlecallback(this);

        loadUrl(Constants.MQNEWS);
        returnIm.setVisibility(View.GONE);

       // loadUrl("https://blog.csdn.net/xiaoxiaobian3310903/article/details/6632490");
    }

    @Override
    public void GetWebviewtitle(String webviewtitle) {
        if (webviewtitle.length()>8){
            menu_title.setText(webviewtitle.substring(0,8)+"...");
        }else {
            menu_title.setText(webviewtitle);
        }
    }
}