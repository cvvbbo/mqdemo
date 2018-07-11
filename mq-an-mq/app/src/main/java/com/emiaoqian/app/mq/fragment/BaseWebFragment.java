package com.emiaoqian.app.mq.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.MailTo;
import android.net.Uri;

import com.emiaoqian.app.mq.Interface.Getrealurlcallback;
import com.emiaoqian.app.mq.Interface.LoginCallBack;
import com.emiaoqian.app.mq.Interface.Logoutcallback;
import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.Interface.RefreashCallBack;
import com.emiaoqian.app.mq.Interface.WebWiewCallback;
import com.emiaoqian.app.mq.activity.WebviewtoNewActivity;
import com.emiaoqian.app.mq.activity.bbb;
import com.emiaoqian.app.mq.utils.MyDataCleanManager;
import com.emiaoqian.app.mq.utils.PopupWindowUtil;
import com.tencent.smtt.export.external.interfaces.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;


/***1.10这里还有一个，待会看看***/
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import android.webkit.JavascriptInterface;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;


import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.other.ChomeClient;
import com.emiaoqian.app.mq.other.CustomShareListener;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.ImageUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.MessageEvent;
import com.emiaoqian.app.mq.utils.Netutils;
import com.emiaoqian.app.mq.utils.PermissionUtil;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.umeng.analytics.MobclickAgent;
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

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiong on 2017/10/10.
 */

public  class BaseWebFragment extends BaseFragment implements ChomeClient.OpenFileChooserCallBack,
        WebWiewCallback{
    private PopupWindow mPopupWindow;
    private String sign;
    public static final String CONST_USER_AGENT = " emiaoqian-1.0.2";
    private ProgressBar pg1;
    private FrameLayout fl;
    public FrameLayout webView1;
    private WebSettings settings;
    private static final String TAG = BaseWebFragment.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMsg;
    public ValueCallback<Uri[]> mUploadMsgForAndroid5;
    // permission Code
    private static final int P_CODE_PERMISSIONS = 101;
    private boolean isAnimStart = false;
    private int currentProgress;
    private View mErrorView;
    private LinearLayout webParentView;
    private int height;
    private int width;
    private String find = "/sys/waybill/search";
    private String about_me = "/aboutApp";
    Handler handler = new Handler();
    public ActionBar supportActionBar;
    public Toolbar homeTb;
    private RelativeLayout nouserl;
    public ImageView imagebutton;
    public TextView title;
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private AppCompatActivity mAppCompatActivity;
    public boolean needReloadUrl = false;//重新登陆后所有url需要重新加载
    public String urlDefault = "";
    private String urlPaySucceed = null;
    public WebView webView;
    public ImageView return_im;
    public TextView menu_title;
    public ImageView returnIm;
    public RelativeLayout title_rl;
    private String picUrl;
    public RelativeLayout returnIm_rl;
    public RelativeLayout closeIm_rl;
    public ImageView closeIm;

    // public String Realurl=null;

    @Override
    public int getlayout() {
        return R.layout.new_webview;
    }

    @Override
    public void initialize() {
        closeIm = (ImageView) view.findViewById(R.id.closeIm);
        closeIm_rl = (RelativeLayout) view.findViewById(R.id.closeIm_rl);
        returnIm_rl = (RelativeLayout) view.findViewById(R.id.returnIm_rl);
        title_rl = (RelativeLayout) view.findViewById(R.id.title_rl);
        returnIm = (ImageView) view.findViewById(R.id.returnIm);
        menu_title = (TextView) view.findViewById(R.id.menu_title);
        return_im = (ImageView) view.findViewById(R.id.return_im);
        WebviewtoNewActivity.getWebviewlastpager(this);
        Mainactivity.getWebviewlastpager(this);
        //这里是先监听webview的回退，然后在执行fragment的回退 2.3
        //子类实现基类的方法
        WorkFragment2.getWebviewlastpager(this);
        fl = (FrameLayout) view.findViewById(R.id.fl);
        webView1 = (FrameLayout) view.findViewById(R.id.webView);
        webView = new WebView(getContext());
        webView1.addView(webView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));
        pg1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        mAppCompatActivity = (AppCompatActivity) mActivity;
        homeTb = (Toolbar) view.findViewById(R.id.home_tb1);
        nouserl = (RelativeLayout) view.findViewById(R.id.nouserl);
        imagebutton = (ImageView) view.findViewById(R.id.imagebutton);
        title = (TextView) view.findViewById(R.id.title);
        initErrorPage();
        setupWebview();
        //用这个来初始化能直接隐藏掉，返回的按钮
        inittoolbar3();
        mShareListener = new CustomShareListener();

        //https://blog.csdn.net/bin622/article/details/74453156/
        //长按保存图片
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {


                    picUrl = hitTestResult.getExtra();
                    SavephotoDiaologFragment myDiaolog = SavephotoDiaologFragment.newstance(picUrl);
                    myDiaolog.show(getFragmentManager(), "haha");

                    return true;
                }
                return false;//保持长按可以复制文字

            }
        });

    }


    private void showPopupWindow(View anchorView,View popupwindowscontentview) {
        mPopupWindow = new PopupWindow(popupwindowscontentview,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(anchorView, popupwindowscontentview);
        int xOff = 20; // 可以自己调整偏移
        //windowPos[0] -= xOff;
        mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }






    //。。。
    public void loadUrl(String url) {
        LogUtil.e(TAG, String.format("%s loadUrl url:%s", this.getClass().getSimpleName(), url));
        LogUtil.e("996--"+url);
        //第一个是没有网的条件
        if (!Netutils.isNetworkAvalible(MyApplication.mcontext)) {
            //没网的时候就先加载一遍地址让webview记住地址 1.10
            if(TextUtils.isEmpty(url)){
                webView.loadUrl(urlDefault);
            }else{
                webView.loadUrl(url);
            }

            webView1.setVisibility(View.GONE);
            // webView1.removeAllViews();
            int flchildCount = fl.getChildCount();
            if (flchildCount==0) {
                fl.setVisibility(View.VISIBLE);
                fl.addView(mErrorView);
            }else {
                fl.setVisibility(View.VISIBLE);
            }

            //反正很坑爹！！！，反正子控件不能去掉。。。1.10
        } else {
            int childCount = webView1.getChildCount();
            if (childCount==1){
                webView1.setVisibility(View.VISIBLE);
                fl.setVisibility(View.GONE);
                // fl.removeAllViews();

                if(TextUtils.isEmpty(url)){
                    webView.loadUrl(urlDefault);
                }else{
                    webView.loadUrl(url);
                }
            }

        }
        needReloadUrl = false;
    }


    public static LoginCallBack loginCallBack;

    public static void setLoginCallBack(LoginCallBack loginCallBack){
        BaseWebFragment.loginCallBack=loginCallBack;

    }

    //获取webview的title 6.6   TODO: 2018/6/6
    public static MyCallback.WebviewGettitle getWebviewtitle;
    public static void getWebviewtitlecallback(MyCallback.WebviewGettitle getWebviewtitle){
        BaseWebFragment.getWebviewtitle=getWebviewtitle;
    }

    private void setupWebview() {
        settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);//出现+,-号放大缩小,在wap网页上无效
        settings.setUseWideViewPort(true);//出现+,-号放大缩小,在wap网页上无效
        settings.setJavaScriptEnabled(true);//网页前端 html5  css js jquery  指定js可用
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //下面两个都是demo里面必须添加的（能使拍照弹框生效的）
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true); //缓存机制相关
        //使网页端能够识别是什么客户端使用了代理
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + CONST_USER_AGENT);
        String aa = webView.getSettings().getUserAgentString();
        LogUtil.e(TAG, String.format("%s getUserAgentString=%s", this.getClass().getSimpleName(), webView.getSettings().getUserAgentString()));
        //webView.addJavascriptInterface(new JS(), "android");
        webView.addJavascriptInterface(this, "android");
        webView.setWebChromeClient(new ChomeClient(this) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                currentProgress = pg1.getProgress();
                if (newProgress >= 100 && !isAnimStart) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    pg1.setProgress(newProgress);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(pg1.getProgress());
                } else {
                    // 开启属性动画让进度条平滑递增
                    startProgressAnimation(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title!=null&&!title.equals("")){
                    if (getWebviewtitle!=null) {
                        getWebviewtitle.GetWebviewtitle(title);
                    }
                }
            }
        });

        //让网页确保在webView中开启
        webView.setWebViewClient(new MyWebViewClient(getActivity()));

        /**
         * 嵌套了fragment里面的webview的回退 这个是普通的webview，普通的webview能用这个，但是这个方法在x5的核里面不适用！！！
         */
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                /**
                 *
                 * 这里支付完成之后会走两次返回的方法，第二次返回的地址就不是完成运单的界面了10.18
                 */
                LogUtil.e(TAG, String.format("%s onKey event=%s", this.getClass().getSimpleName(), event.getAction()));
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();
                        LogUtil.e(TAG, String.format("%s goBack", this.getClass().getSimpleName()));
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 当界面重新展示时（fragment.show）,调用onrequest刷新界面
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.e(TAG, String.format("%s onHiddenChanged %s", this.getClass().getSimpleName(), hidden));
        // 前上司写的
//        if (hidden == false && needReloadUrl) {
//            loadUrl(urlDefault);
//        }

        if (hidden){
            LogUtil.e("666注销"+getClass().getSimpleName());
            Mainactivity.getWebviewlastpager(null);
        }else {

            //通过日志发现，（fragment）隐藏了再显示之后，有时候接口是先注销再注册，导致接口还是没注册。所以当非让fragment再次显示的时候延迟注册接口3.6
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtil.e("666注册"+getClass().getSimpleName());
                    Mainactivity.getWebviewlastpager(BaseWebFragment.this);
                }
            },50);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
        LogUtil.e(TAG, String.format("%s onResume", this.getClass().getSimpleName()));
        if (!TextUtils.isEmpty(urlPaySucceed)) {
            loadUrl(urlPaySucceed);
            urlPaySucceed = null;
        }
        LogUtil.e(TAG, String.format("%s onResume urlPaySucceed=%s", this.getClass().getSimpleName(), urlPaySucceed));

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e(TAG, String.format("%s onPause", this.getClass().getSimpleName()));
        //这个可能不是想要的效果，这里已经换成fragment了
        MobclickAgent.onPause(getActivity());
        LogUtil.e(TAG, String.format("%s onPause urlPaySucceed=%s", this.getClass().getSimpleName(), urlPaySucceed));


    }

    /**
     * 这个是使用腾讯的x5内核，它的webview监听不到在fragment中的webview的回退 1.11
     *
     *x5内核回退的方法请查看x5浏览器内核demo
     */
    @Override
    public void BackLastCallback() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }

    @Override
    public boolean CanCallback() {
        if (webView != null && webView.canGoBack()) {
            return true;
        }

        return  false;
    }


    public static Getrealurlcallback getrealurlcallback;
    public static void setGetrealurlcallbacklistener(Getrealurlcallback getrealurlcallback){
        BaseWebFragment.getrealurlcallback=getrealurlcallback;
    }



    //9.28
    public class MyWebViewClient extends WebViewClient {
        private final WeakReference<Activity> mActivityRef;
        public MyWebViewClient(Activity activity) {
            mActivityRef = new WeakReference<Activity>(activity);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        //在本应用中开启网页
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.e(TAG, String.format("%s shouldOverrideUrlLoading url:%s", this.getClass().getSimpleName(), url));

            if (url.startsWith("https://www.emiaoqian.com/sys/order/sendsucc/no")) {
                urlPaySucceed = url;
            }

            //支付宝支付
            if (url.contains("alipays://platformapi")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            //完美解决微信支付问题！！！
            if (url.startsWith("weixin://wap/pay?")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                //startActivityForResult(intent,233);
                startActivity(intent);
                return true;
            }

            if (url.startsWith("mailto:")) {
                final Activity activity = mActivityRef.get();
                if (activity != null) {
                    //捕获异常，解决模拟器没有支持的应用时发生的奔溃问题9.29
                    MailTo mt = MailTo.parse(url);
                    try {
                        Intent i = newEmailIntent(activity, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                        activity.startActivity(i);
                        view.reload();
                    } catch (Exception e) {

                        LogUtil.e(e + "");
                        ToastUtil.showToast("当前手机没有支持的应用类型");
                    }
                    return true;
                }
            }

//            view.loadUrl(url);
//            return  true;
            return super.shouldOverrideUrlLoading(view, url);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            //           String url = request.getUrl().toString();
            //           LogUtil.e(TAG, String.format("%s shouldInterceptRequest url:%s",this.getClass().getSimpleName(),url));
            return super.shouldInterceptRequest(view, request);
        }

        private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
            intent.putExtra(Intent.EXTRA_TEXT, body);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_CC, cc);
            intent.setType("message/rfc822");
            return intent;
        }




        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtil.e("---开始的地址233--"+url);
            LogUtil.e(TAG, String.format("%s onPageStarted url:%s", this.getClass().getSimpleName(), url));
            pg1.setVisibility(View.VISIBLE);
            pg1.setAlpha(1.0f);
            //这里也能检测网络的变化（就是开始进来的以及中间过程中发生跳转的）

            //4.8（修改towebviewfragment，webviewfragment也需要注册那个getchildfragmentcallback这个接口是因为，因为他不是
            // fragment里面的，如何不注册就直接退出来了，（就是直接按返回的时候直接退出来的，为了会退出来，研究下activity中的回退代码就知道）
            // 然后，因为添加fragment的方式是通过replace这样的方式的，所以注册了childfragmentcallback这个接口的，相当于也注册了webview返回的的
            //接口（具体原因看代码），然后towebviewfragment这个类注册了webview的回退接口，towebfragment在退出的时候销毁了（不对，销毁的不彻底！！
            // ，它还引用着接口！！！！找找找，又是一个神坑。。。。4.8），就是查找刚开始进来的类引用着什么接口？？？
            Mainactivity.getWebviewlastpager(BaseWebFragment.this);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.createInstance(mActivity);
                CookieManager cookieManager = CookieManager.getInstance();


                String cookie = cookieManager.getCookie(url);//从H5获取cookie
                LogUtil.e("1024（5.0<sdk）之前onPageFinished cookie :" + cookie);

                if (cookie != null) {
                    String phone_num = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "phone_num", "");
                    String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                    boolean isfirst = sharepreferenceUtils.getbooleandata(MyApplication.mcontext, "isfirst", false);
                    if (!user_id.equals("")) {
                        //在重新设置的cooker中加空格和分号都是无法加入新cookie中的
                        cookieManager.setCookie(url, "mq_user_id=" + user_id);
                        cookieManager.setCookie(url, "mq_mobile=" + phone_num);

                    }
                    if (!user_id.equals("")&&isfirst){
                        loadUrl(url);
                        sharepreferenceUtils.saveBooleandata(MyApplication.mcontext,"isfirst",false);
                    }

                    if (getrealurlcallback!=null) {
                        String realurl = BaseWebFragment.getrealurlcallback.Getrealurlcallback();
                        //这里判断如果是包含"login"的地址都是发生请求重定向的，然后就加载真实的地址
                        if (!user_id.equals("") && url.equals(Constants.LOGIN)) {
                            loadUrl(realurl);
                        }
                    }
                    // loadUrl(url);
                    CookieSyncManager.getInstance().sync();
                    LogUtil.e("1024（5.0<sdk）之后onPageFinished cookie :" + cookie);
                }

            } else {
                CookieManager cookieManager = CookieManager.getInstance();

                String cookie = cookieManager.getCookie(url);//从H5获取cookie
                LogUtil.e("1024onPageFinished cookie1 :" + cookie);

                if (cookie != null) {
                    String phone_num = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "phone_num", "");
                    String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                    boolean isfirst = sharepreferenceUtils.getbooleandata(MyApplication.mcontext, "isfirst", false);

                    //// TODO: 2018/5/17 当没有这个值的时候就不添加这个值，暂时完美，感觉以后还是有问题的！！！5.17
                    //// TODO: 2018/5/17 不然会添加两次，一次是空的，另一个是有值的 5.17
                    if (!user_id.equals("")) {
                        cookieManager.setCookie(url, "mq_user_id=" + user_id);
                        cookieManager.setCookie(url, "mq_mobile=" + phone_num);
                        LogUtil.e("添加cookier时候的地址1--"+url);

                    }
                    if (!user_id.equals("")&&isfirst){
                        LogUtil.e("添加cookier时候的地址2--"+url);
                        loadUrl(url);
//                        String realurl = BaseWebFragment.getrealurlcallback.Getrealurlcallback();
//                        loadUrl(realurl);
                        sharepreferenceUtils.saveBooleandata(MyApplication.mcontext,"isfirst",false);
                    }
                    if (getrealurlcallback!=null) {
                        String realurl = BaseWebFragment.getrealurlcallback.Getrealurlcallback();
                        //这里判断如果是包含"login"的地址都是发生请求重定向的，然后就加载真实的地址
                        if (!user_id.equals("") && url.equals(Constants.LOGIN)) {
                            loadUrl(realurl);
                        }
                    }



                    CookieManager.getInstance().flush();
                    CookieManager cookieManager1 = CookieManager.getInstance();
                    String cookie1 = cookieManager1.getCookie(url);//从H5获取cookie
                    LogUtil.e("1024添加之后onPageFinished cookie1 :" + cookie1);
                }


//                HashMap<String,String> cookemap=new HashMap<>();
//                String[] split = cookie.split(";");
//                String newsplit="";
//             for (int i=0;i<split.length;i++){
//                String [] s1=split[i].split("=");
//                 for (int j=0;j<s1.length-1;j++){
//                     cookemap.put(s1[j],s1[j+1]);
//                 }
//             }
//                LogUtil.e("1024onPageFinished cookie1 集合:" + cookemap);
//
//                CookieManager.getInstance().flush();

            }

        }

        //这个方法是最终监听页面的变化
        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtil.e("---最后的地址233--"+url);

            if (view.canGoBack()){
                closeIm_rl.setVisibility(View.VISIBLE);
            }else {
                closeIm_rl.setVisibility(View.GONE);
            }


        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            showErrorPage();
            LogUtil.e("onReceivedError errorCode=" + errorCode + "failingUrl=" + failingUrl);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            showErrorPage();
        }
    }


    private void inittoolbar2(final String url) {
        ((Mainactivity) getActivity()).setSupportActionBar(homeTb);
        supportActionBar = ((Mainactivity) getActivity()).getSupportActionBar();

        //不然的话这里会出现两个返回按钮
        if(getClass().getSimpleName().equals("ToWebviewFragment")){

            return;

        }

        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        homeTb.setTitleTextColor(Color.BLACK);
        homeTb.setNavigationIcon(R.drawable.return_im);
        title.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.x45), 0);
        homeTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportActionBar.setDisplayShowHomeEnabled(false);
                supportActionBar.setDisplayHomeAsUpEnabled(false);
                title.setPadding(0, 0, 0, 0);
                homeTb.setTitle("");
                LogUtil.e("toolbar执行了么");
                if (url.equals(Constants.LOGIN)) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.URL_SECOND));
                }
            }
        });
    }

    /**
     * 这个是隐藏toolbar的方法
     *
     */
    private void inittoolbar3() {
        //注意这里的获取activity不能强转为某个特定的activity，因为这个fragment是基类可能会被共用！！！2018.6.27
        ((AppCompatActivity) getActivity()).setSupportActionBar(homeTb);
        supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        supportActionBar.setDisplayShowHomeEnabled(false);
        supportActionBar.setDisplayHomeAsUpEnabled(false);
        homeTb.setTitleTextColor(Color.BLACK);

       // title.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.x45), 0);

    }

    //如果第一次没有加载成功显示的界面，直接加载的是显示失败的界面 1.11
    private void showErrorPage() {
        // fl.removeAllViews();
        webView1.setVisibility(View.GONE);
        int flchildCount = fl.getChildCount();
        if (flchildCount==0) {
            fl.setVisibility(View.VISIBLE);
            fl.addView(mErrorView);
        }else {
            fl.setVisibility(View.VISIBLE);
        }
    }

    /***
     * 显示加载失败时自定义的网页
     */
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(getActivity(), R.layout.page_error, null);
            Button button = (Button) mErrorView.findViewById(R.id.btn_reload);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Netutils.isNetworkAvalible(MyApplication.mcontext)) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                int childCount = webView1.getChildCount();
                                if (childCount==1) {
                                    webView1.setVisibility(View.VISIBLE);
                                    fl.setVisibility(View.GONE);
                                    //  fl.removeAllViews();

                                }
                            }
                        }, 1200);
                        webView.reload();
                        // setupWebview();
                    } else {
                        return;
                    }
                }
            });
        }
    }

    //进度条消失动画
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(pg1, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator());     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                int offset = 100 - progress;
                pg1.setProgress((int) (progress + offset * fraction));
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                pg1.setProgress(0);
                pg1.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }

    //进度条开始动画
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(pg1, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();
    }

    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]>
            filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        mUploadMsgForAndroid5 = filePathCallback;
        showOptions();
        return true;
    }

    //在网页中打开相机和相册的选择器
    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setOnCancelListener(new DialogOnCancelListener());
        alertDialog.setTitle("请选择操作");
        // gallery, camera.
        String[] options = {"相册", "拍照"};
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    ToastUtil.showToast("请去\"设置\"中开启本应用的相机和图片媒体访问权限");
                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }
                            }

                            try {
                                mSourceIntent = ImageUtil.choosePicture();
                                startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtil.showToast("请去\"设置\"中开启本应用的相机和图片媒体访问权限");
                                restoreUploadMsg();
                            }

                        } else {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    ToastUtil.showToast("请去\"设置\"中开启本应用的相机和图片媒体访问权限");
                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }

                                if (!PermissionUtil.isPermissionValid(getActivity(), Manifest.permission.CAMERA)) {
                                    ToastUtil.showToast("请去\"设置\"中开启本应用的相机权限");
                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }
                            }

                            try {
                                mSourceIntent = ImageUtil.takeBigPicture();
                                startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtil.showToast("请去\"设置\"中开启本应用的相机和图片媒体访问权限");
                                restoreUploadMsg();
                            }
                        }
                    }
                }
        );
        alertDialog.show();
    }

    private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            restoreUploadMsg();
        }
    }

    private void restoreUploadMsg() {
        if (mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;
        } else if (mUploadMsgForAndroid5 != null) {
            mUploadMsgForAndroid5.onReceiveValue(null);
            mUploadMsgForAndroid5 = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case P_CODE_PERMISSIONS:
                requestResult(permissions, grantResults);
                restoreUploadMsg();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermissionsAndroidM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> needPermissionList = new ArrayList<>();
            needPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.CAMERA);
            PermissionUtil.requestPermissions(getActivity(), P_CODE_PERMISSIONS, needPermissionList);
        } else {
            return;
        }
    }

    public void requestResult(String[] permissions, int[] grantResults) {
        ArrayList<String> needPermissions = new ArrayList<String>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (PermissionUtil.isOverMarshmallow()) {
                    needPermissions.add(permissions[i]);
                }
            }
        }

        if (needPermissions.size() > 0) {
            StringBuilder permissionsMsg = new StringBuilder();
            for (int i = 0; i < needPermissions.size(); i++) {
                String strPermissons = needPermissions.get(i);
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append(",文件");
                } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append(",文件");
                } else if (Manifest.permission.CAMERA.equals(strPermissons)) {
                    permissionsMsg.append(",摄像头");
                }
            }
            String strMessage = "请允许使用\"" + permissionsMsg.substring(1).toString() + "\"权限, 以正常使用APP的所有功能.";
            ToastUtil.showToast(strMessage);
        } else {
            return;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
            }
            if (mUploadMsgForAndroid5 != null) {         // for android 5.0+
                mUploadMsgForAndroid5.onReceiveValue(null);
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsg == null) {
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(getActivity(), mSourceIntent, data);
                        Bitmap bitmap = ImageUtil.compressImage(sourcePath, mAppCompatActivity);
                        String newpath = ImageUtil.saveImage(mAppCompatActivity, bitmap);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            LogUtil.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(newpath));
                        mUploadMsg.onReceiveValue(uri);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsgForAndroid5 == null) {        // for android 5.0+
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(getActivity(), mSourceIntent, data);
                        Bitmap bitmap = ImageUtil.compressImage(sourcePath, mAppCompatActivity);
                        String newpath = ImageUtil.saveImage(mAppCompatActivity, bitmap);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            LogUtil.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(newpath));
                        mUploadMsgForAndroid5.onReceiveValue(new Uri[]{uri});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    @Override
    public void onDestroy() {
        webView.setVisibility(View.GONE);
        webView.clearHistory();
        webView.removeAllViews();
        webView.destroy();
        Mainactivity.getWebviewlastpager(null);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    //第一版的接口，没用了就删了 6.27
    public static RefreashCallBack refreashCallBack;
    public static void setRefreashCallBack(RefreashCallBack refreashCallBack){
        BaseWebFragment.refreashCallBack=refreashCallBack;
    }


    @JavascriptInterface
    public void ReturnNew(final String a){

        LogUtil.e(a);
        if (a!=null&&!a.equals("")) {
            if (getClass().getSimpleName().equals("ToWebviewFragment")){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadUrl(Constants.CONST_HOST1+a);
                    }
                });

                return;
            }
            //在webview的js跳转activity的时候，即使不在manifest文件中注册他即使这样写了也不会报错
            Intent intent = new Intent(getActivity(), WebviewtoNewActivity.class);
            if (!a.contains("http")) {
                intent.putExtra("jump_url", Constants.CONST_HOST1 + a);
            }else {
                intent.putExtra("jump_url", a);
            }
            startActivity(intent);
        }
    }

    @JavascriptInterface
    public void callphone(String a){
        LogUtil.e(a);
       // ToastUtil.showToastCenter(a);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+a));
        startActivity(intent);
    }


    public static Logoutcallback logoutcallback;
    public static void setLogoutcallbacklistener(Logoutcallback logoutcallback){
        BaseWebFragment.logoutcallback=logoutcallback;
    }

    //个人感觉作用不大，，，
    @JavascriptInterface
    public void ClearCache(String a){
        LogUtil.e(a);
        if (a.equals("clear")){
            //webView.clearCache(true);
            try {
                String cacheSize = MyDataCleanManager.getTotalCacheSize(getActivity());
                MyDataCleanManager.clearAllCache(getActivity());
                //ToastUtil.showToastCenter("当前缓存的大小是--"+cacheSize);
                ClearWebCacheDialogFragment fragment = ClearWebCacheDialogFragment.newstance(cacheSize);
                fragment.show(getFragmentManager(),"haha");


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @JavascriptInterface
    public void logout(String a){
        LogUtil.e("退出有值么"+a);

        if (a.equals("logout")){
            sharepreferenceUtils.saveStringdata(MyApplication.mcontext,"phone_num","");
            sharepreferenceUtils.saveStringdata(MyApplication.mcontext,"user_id","");
            //首先把侧滑菜单退出
            sharepreferenceUtils.saveStringdata(MyApplication.mcontext,"realname","");
            //清除webview上的cookier
            CookieSyncManager.createInstance(getActivity());
            //CookieManager.getInstance().removeAllCookie();
            CookieSyncManager cookieSyncManager =  CookieSyncManager.createInstance(MyApplication.mcontext);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            //如果这里是removeallcookier将会移除所有cookier，包括不重要的cookier 6.5
            // cookieManager.removeAllCookie();
            cookieSyncManager.sync();
            CookieManager.getInstance().flush();
            //最后把fragment退出
            getFragmentManager().popBackStack();
            //这个只针对会请求重定向的，比如已经登录到退出登录（清除了cookier就是退出登录，会跳到另一个url，
            // 然后因为这个fragment销毁了，他打开的是外部浏览器。。）2018.6.28
            logoutcallback.Logoutcallback();
            if (webView.getUrl().equals(Constants.CONST_HOST1)){
                return;
            }
        }

    }


    //public class JS {
        /**
         * 分享接口
         * @param a
         */
        @JavascriptInterface
        public void share(String a) {
            LogUtil.e(a);

            try {
                LogUtil.e("---分享按钮");
                // final String icon=null;
                JSONObject jsonObject = new JSONObject(a);
                final String title = jsonObject.optString("title");
                final String content = jsonObject.optString("content");
                final String url = jsonObject.optString("url");
                final String icon=jsonObject.optString("icon");

                //友盟分享面板的配置10.23
                ShareBoardConfig config = new ShareBoardConfig();
                config.setIndicatorColor(Color.WHITE,Color.WHITE);
                config.setShareboardBackgroundColor(Color.WHITE);
                mShareAction = new ShareAction(getActivity()).setDisplayList(
                        /***
                         * 下面这些都就平台的选择
                         *
                         */
                        SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ,
                        SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.SMS,
                        SHARE_MEDIA.DINGTALK,
                        SHARE_MEDIA.MORE

                )

                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (snsPlatform.mShowWord.equals("umeng_sharebutton_copy")) {

                                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    // 将文本内容放到系统剪贴板里。
                                    cm.setText(title+content+url);
                                    ToastUtil.showToast("复制成功");
                                } else if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    // 将文本内容放到系统剪贴板里。
                                    cm.setText(url);
                                    ToastUtil.showToast("复制成功");
                                } else if (share_media == SHARE_MEDIA.SMS) {
                                    android.util.Log.e("==", "-执行了么第二个");
                                    new ShareAction(getActivity()).withText(title+content+url)
                                            .setPlatform(share_media)
                                            .setCallback(mShareListener)
                                            .share();
                                }  else if (share_media == SHARE_MEDIA.MORE){
                                    Intent share_intent = new Intent();
                                    share_intent.setAction(Intent.ACTION_SEND);
                                    share_intent.setType("text/plain");
                                    share_intent.putExtra(Intent.EXTRA_SUBJECT, "f分享");
                                    //下面的就是分享的内容
                                    share_intent.putExtra(Intent.EXTRA_TEXT,title+content+url);
                                    share_intent = Intent.createChooser(share_intent, "分享");
                                    startActivity(share_intent);


                                }else {
                                    android.util.Log.e("==", "-执行了么第三个");
                                    UMWeb web = new UMWeb(url);
                                    web.setTitle(title);
                                    web.setDescription(content);
                                    //下面这个是放分享卡片的图片的
                                    if(TextUtils.isEmpty(icon)){
                                        web.setThumb(new UMImage(getContext(), R.drawable.url_logo));
                                    }else {
                                        web.setThumb(new UMImage(getContext(),icon));
                                    }

                                    //最后一个是分享的类型
                                    new ShareAction(getActivity()).withMedia(web)
                                            .setPlatform(share_media)
                                            .setCallback(mShareListener)
                                            .share();
                                }
                            }
                        });//开启分享的面板
                mShareAction.open(config);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //LogUtil.e(a+"---");
        }


        // // TODO: 2018/6/22 为了避免报错，暂时注释掉！！！
        //登录的时候和js的交互，获取用户信息
//        @JavascriptInterface
//        public void login(String a){
//            try {
//                LogUtil.e("--登录按钮-");
//                JSONObject jsonObject = new JSONObject(a);
//                final String user_id = jsonObject.getString("user_id");
//                if (!user_id.isEmpty()) {
//                    sharepreferenceUtils.saveStringdata(MyApplication.mcontext, "user_id", user_id);
//                    loginCallBack.logincallback();
//                    sharepreferenceUtils.saveBooleandata(MyApplication.mcontext,"status",true);
//                }
//
//                if (getClass().getSimpleName().equals("ToWebviewFragment")){
//                    LogUtil.e("--------当前是这个页面-----");
//                    refreashCallBack.refreashcallback();
//
//                }
//
//                LogUtil.e("---登录之后能取到id的值么--"+user_id);
//                //取出token的值
//                // final String token = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "token", " ");
//
//                //友盟官方这个随时能够获得token的东西
//                String token = MyApplication.mPushAgent.getRegistrationId();
//                //LogUtil.e("registrationId--"+registrationId+"--equals--"+token.equals(registrationId));
//
//                LogUtil.e("--umtoken--"+token);
//                final String umtoken="1&&"+token;
//                long l = System.currentTimeMillis();
//                String  str=String.valueOf(l);
//                LogUtil.e("----时间戳=="+str);
//                final String s = EncodeBuilder.javaToJSON(user_id, umtoken,str);
//                LogUtil.e("添加进去的字符串--"+s);
//                sign = EncodeBuilder.newString(s);
//                httphelper.create().saveDeviceNo2(Constants.GET_USE_INFOR,
//                        umtoken, user_id, sign,str,new httphelper.httpcallback() {
//                            @Override
//                            public void success(String s) {
//                                LogUtil.e("---"+sign);
//                                LogUtil.e("获取用户信息返回的值---"+s);
//                                LogUtil.e("token---"+umtoken);
//                                LogUtil.e("user_id--"+user_id);
//                            }
//
//                            @Override
//                            public void fail(Exception e) {
//
//                            }
//                        });
//
//                //清空token的值
//                //sharepreferenceUtils.saveStringdata(MyApplication.mcontext,"token"," ");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }



}
