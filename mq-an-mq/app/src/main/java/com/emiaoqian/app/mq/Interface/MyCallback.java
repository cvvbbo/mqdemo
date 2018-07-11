package com.emiaoqian.app.mq.Interface;

/**
 * Created by xiong on 2018/5/31.
 */

/**
 *
 * 如果多个类都注册了接口会导致混乱，一定要反注册，有条件那就写不同的新接口，省事。。6.21
 *
 */
public class MyCallback {

    //获取webview标题
    public interface WebviewGettitle{
        void GetWebviewtitle(String webviewtitle);
    }

    //刷新首页数据
    public interface RefreashHomePage{
        void RefreashHomepagecallback();
    }

    //刷新我的页面
    public interface RefreashMePage{
        void RefreashMepagecallback();
    }

    //刷新消息页面
    public interface RefreashMessage{
        void RefreashMessage();
    }

    //首页工作台的切换
    public interface SelectStatus{
        void SelectStatuscallback(String status);
    }

    public interface KeepConnection{
        void KeepConnectioncallback();
    }
}
