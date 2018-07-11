package com.emiaoqian.app.mq.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import com.tencent.smtt.sdk.CookieManager;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;

import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;


import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.other.ChomeClient;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.ImageUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.Netutils;
import com.emiaoqian.app.mq.utils.PermissionUtil;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by xiong on 2017/9/15.
 */

public class WebviewActivity extends AppCompatActivity implements ChomeClient.OpenFileChooserCallBack, View.OnClickListener {

    @BindView(R.id.rdbt1)
    RadioButton rdbt1;
    @BindView(R.id.rdbt2)
    RadioButton rdbt2;
    @BindView(R.id.rdbt3)
    RadioButton rdbt3;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.imagebutton)
    ImageView imagebutton;
    @BindView(R.id.rdbt4)
    RadioButton rdbt4;
    @BindView(R.id.progressBar1)
    ProgressBar pg1;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.nouserl)
    RelativeLayout nouserl;
    @BindView(R.id.home_tb)
    Toolbar homeTb;


    //private String url;
    private ImageView ivBack;
    private ImageView ivFav;
    private ImageView ivShare;
    private ImageView ivTextSize;
    private WebView webView;
    private ImageView ivSpeak;
    private WebSettings settings;
    private String content;

    static String innerurl;


    private static final String TAG = "Setttingactivity2";

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
    private ActionBar supportActionBar;
    private String newurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_detail);
        ButterKnife.bind(this);
        initErrorPage();
        rdbt1.setOnClickListener(this);
        rdbt2.setOnClickListener(this);
        rdbt3.setOnClickListener(this);
        rdbt4.setOnClickListener(this);
        imagebutton.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.webView);
        title.setText("秒签消息");
        radiogroup.setVisibility(View.GONE);
    }

    //除了拍照，从相册中获取也是
    //2017.9.26解决微信支付返回空白问题
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("onResume");
        MobclickAgent.onResume(this);
        if (newurl != null) {
            webView.loadUrl(" https://www.emiaoqian.com/sys/order/sendsucc/no/" + newurl);
            newurl = null;
        }
        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            Set<String> keySet = bun.keySet();
            for (String key : keySet) {
                String value = bun.getString(key);
                if ("title".equals(key)) {
                    title.setText(value);
                }
                if ("url".equals(key) && !TextUtils.isEmpty(value)) {
                    checkone(value);
                }
            }
        }

        LogUtil.e("newurl--" + newurl);
    }


    //拍照的时候会执行暂停方法
    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("onPause");
        MobclickAgent.onPause(this);
        LogUtil.e("newurl--" + newurl);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rdbt1:
                imagebutton.setVisibility(View.INVISIBLE);

                checkone(Constants.LOGIN);


                title.setText("工作台");
                break;
            case R.id.rdbt2:
                imagebutton.setVisibility(View.INVISIBLE);

                checkone(Constants.QUICK);
                title.setText("我要寄件");

                break;
            case R.id.rdbt3:
                imagebutton.setVisibility(View.INVISIBLE);

                checkone(Constants.FIND);

                title.setText("运单跟踪");

                break;
            case R.id.rdbt4:
                imagebutton.setVisibility(View.VISIBLE);
                checkone(Constants.TESTURL);
                title.setText("个人中心");
                break;
            case R.id.imagebutton:

                checkone(Constants.ABOUT_ME);
                imagebutton.setVisibility(View.INVISIBLE);
                title.setText("关于秒签");

                break;

        }

    }


    private void checkone(String url) {
        //第一个是没有网的条件
        if (!Netutils.isNetworkAvalible(MyApplication.mcontext)) {
            fl.removeAllViews();
            webView.loadUrl(url);
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            fl.setVisibility(View.VISIBLE);
            fl.addView(mErrorView);

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fl.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    fl.removeAllViews();
                }
            }, 1000);
            Map extraHeaders = new HashMap();
            extraHeaders.put("Referer", "www.emiaoqian.com");
            webView.loadUrl(url, extraHeaders);
            //将+号和-号的按钮显示出来,供放大缩小
            settings = webView.getSettings();
            settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);
            settings.setJavaScriptEnabled(true);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setSaveFormData(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
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

                    if (title.contains("404")) {
                        showErrorPage();
                    }
                }
            });

            //下面两个都是demo里面必须添加的（能使拍照弹框生效的）
            settings.setAllowFileAccess(true);
            settings.setAllowContentAccess(true);
            //缓存机制相关
            settings.setDomStorageEnabled(true);
            //使网页端能够识别是什么客户端使用了代理
            String ua = webView.getSettings().getUserAgentString();
            webView.getSettings().setUserAgentString(ua.replace("Android", "emiaoqian"));
            //大概解决的方法就是每次添加之前，判断之前加过没，就是比较，如果加了，把原来的打印出来，然后再吧原来的值放回去
            //让网页确保在webView中开启
            webView.setWebViewClient(new MyWebViewClient(this));
            fixDirPath();
            // webParentView = (LinearLayout) webView.getParent(); //获取父容器
        }


    }


    //9.28
    public class MyWebViewClient
            extends WebViewClient {

        private final WeakReference<Activity> mActivityRef;

        public MyWebViewClient(Activity activity) {
            mActivityRef = new WeakReference<Activity>(activity);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pg1.setVisibility(View.VISIBLE);
            pg1.setAlpha(1.0f);
            //这里也能检测网络的变化（就是开始进来的以及中间过程中发生跳转的）
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }



        //在本应用中开启网页
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            /**
             * 关于在fragment中会在有网切无网的时候，再次点击时会在app外部打开的问题，可以吧永远放在app内打开的条件
             * 写在最前面。10.12
             *
             */


            //完美解决微信支付问题！！！
            if (url.startsWith("weixin://wap/pay?")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                //startActivityForResult(intent,233);
                startActivity(intent);
                return true;
            } else if (url.startsWith("mailto:")) {
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
            return super.shouldOverrideUrlLoading(view, url);

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

        //这个方法是最终监听页面的变化
        @Override
        public void onPageFinished(WebView view, String url) {

            //webview的加速，貌似能解决白屏问题，但是下面的是硬件加速。。
            // webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);

            //这个是软件加速
            //webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);

            //这个是支付成功返回的地址。。
            if (url.startsWith("https://www.emiaoqian.com/sys/order/sendsucc/no/")) {
                newurl = url.substring(url.length() - 12, url.length());
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync();
            } else {
                CookieManager.getInstance().flush();
            }

            //新加的
            if (!(url.equals(Constants.LOGIN)) && !(url.equals(Constants.LOGINOUT))) {
                if (homeTb != null && supportActionBar != null) {
                    supportActionBar.setDisplayHomeAsUpEnabled(false);
                    supportActionBar.setDisplayShowHomeEnabled(false);
                    homeTb.setTitle("");
                }
                radiogroup.setVisibility(View.GONE);
                nouserl.setVisibility(View.VISIBLE);

                //新加的2017.9.19（这里是回退处理，然后回退的话图标会乱10.11）
                if (url.equals(Constants.QUICK)) {
                    title.setText("我要寄件");
                    rdbt2.setChecked(true);
                    imagebutton.setVisibility(View.INVISIBLE);
                } else if (url.equals(Constants.FIND)) {
                    title.setText("运单跟踪");
                    rdbt3.setChecked(true);
                    imagebutton.setVisibility(View.INVISIBLE);
                } else if (url.equals(Constants.ALRAEDY_LOGIN)) {
                    title.setText("工作台");
                    rdbt1.setChecked(true);
                    imagebutton.setVisibility(View.INVISIBLE);
                } else if (url.equals(Constants.TESTURL)) {
                    title.setText("个人中心");
                    rdbt4.setChecked(true);
                    imagebutton.setVisibility(View.VISIBLE);
                } else if (url.equals(Constants.ABOUT_ME)) {
                    title.setText("关于秒签");
                    imagebutton.setVisibility(View.INVISIBLE);
                    rdbt4.setChecked(true);
                } else if (url.startsWith(Constants.ABOUT_WORK)) {
                    title.setText("工作台");
                    rdbt1.setChecked(true);
                    imagebutton.setVisibility(View.INVISIBLE);

                }

                //登录的时候隐藏下面的按钮（这个就是底部状态栏能够切换的核心2017.10.10）
            } else if (url.equals(Constants.LOGIN) || url.equals(Constants.LOGINOUT)) {
                inittoolbar2();
                radiogroup.setVisibility(View.GONE);
                nouserl.setVisibility(View.GONE);

            }


            LogUtil.e("--最后", url);
            sharepreferenceUtils.saveStringdata(MyApplication.mcontext, "haha", " ");


        }

        //以下两个是替换原生当网页加载错误时候的显示
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            showErrorPage();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            showErrorPage();
        }


    }

    private void inittoolbar2() {
        setSupportActionBar(homeTb);
        supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        homeTb.setTitle("秒签速递");
        homeTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkone(Constants.CONST_HOST);
                rdbt2.setChecked(true);
                title.setText("我要寄件");
                imagebutton.setVisibility(View.GONE);
                radiogroup.setVisibility(View.GONE);
                nouserl.setVisibility(View.VISIBLE);
                supportActionBar.setDisplayShowHomeEnabled(false);
                supportActionBar.setDisplayHomeAsUpEnabled(false);
                homeTb.setTitle("");
            }
        });
    }


    private void showErrorPage() {
        fl.removeAllViews();
        webView.setVisibility(View.GONE);
        fl.setVisibility(View.VISIBLE);
        fl.addView(mErrorView);
    }

    /***
     * 显示加载失败时自定义的网页
     */
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.page_error, null);
            Button button = (Button) mErrorView.findViewById(R.id.btn_reload);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Netutils.isNetworkAvalible(MyApplication.mcontext)) {
                        // fl.removeAllViews();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.setVisibility(View.VISIBLE);
                                fl.setVisibility(View.GONE);
                                fl.removeAllViews();
                            }
                        }, 1200);
                        webView.reload();
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

    private void fixDirPath() {
        String path = ImageUtil.getPicturePath();
        File file = new File(path);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            return;
        }
    }


    //点击照片之后还能返回上一个页面！！（按回退键返回上一个页面）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (homeTb != null && supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
            supportActionBar.setDisplayShowHomeEnabled(false);
            homeTb.setTitle("");
        }

        /**
         * 这里监控的是物理返回或者设置了该接口的点击事件
         * 当按钮事件为返回时，且WebView可以返回，即触发返回事件
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                //这里检测历史记录获取的url是当前页面的url，并不是上一个页面的url
//                WebBackForwardList webBackForwardList = webView.copyBackForwardList();

                webView.goBack();

            } else {
                this.finish();
            }

        }
        return false;
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

    //在网页中弹出
    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new DialogOnCancelListener());

        alertDialog.setTitle("请选择操作");
        // gallery, camera.
        String[] options = {"相册", "拍照"};

        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(WebviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    Toast.makeText(WebviewActivity.this,
                                            "请去\"设置\"中开启本应用的图片媒体访问权限",
                                            Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(WebviewActivity.this,
                                        "请去\"设置\"中开启本应用的图片媒体访问权限",
                                        Toast.LENGTH_SHORT).show();
                                restoreUploadMsg();
                            }

                        } else {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(WebviewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    Toast.makeText(WebviewActivity.this,
                                            "请去\"设置\"中开启本应用的图片媒体访问权限",
                                            Toast.LENGTH_SHORT).show();
                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }

                                if (!PermissionUtil.isPermissionValid(WebviewActivity.this, Manifest.permission.CAMERA)) {
                                    Toast.makeText(WebviewActivity.this,
                                            "请去\"设置\"中开启本应用的相机权限",
                                            Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(WebviewActivity.this,
                                        "请去\"设置\"中开启本应用的相机和图片媒体访问权限",
                                        Toast.LENGTH_SHORT).show();
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
            PermissionUtil.requestPermissions(WebviewActivity.this, P_CODE_PERMISSIONS, needPermissionList);
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
            Toast.makeText(WebviewActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        } else {
            return;
        }
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

                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            LogUtil.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsg.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsgForAndroid5 == null) {        // for android 5.0+
                            return;
                        }

                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            LogUtil.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
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
    protected void onDestroy() {
        if (webView!=null && webView.getParent()!=null){
            webView.setVisibility(View.GONE);
            webView.clearHistory();
            webView.removeAllViews();
            ((ViewGroup)webView.getParent()).removeView(webView);
            webView.destroy();
            webView=null;
        }
        super.onDestroy();
    }
}

