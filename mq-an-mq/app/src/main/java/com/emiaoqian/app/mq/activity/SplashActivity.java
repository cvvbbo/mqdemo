package com.emiaoqian.app.mq.activity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.Netutils;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.UpDataUtil;
import com.emiaoqian.app.mq.utils.UpDatautils;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by xiong on 2017/9/15.
 */

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_INSTALL = 1000;
    private static final String DOWNLOAD_FILE_NAME = "app-release.apk";
    Handler h =new Handler();
    private String mDesc;
    private String mDownloadurl;
    private String desc;
    private DownloadManager manager ;
    private long downloadId;
    private String downurl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_view);
        manager =(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.e("版本名字"+getVersionName()+"--版本号"+getVersioncode());
                //首先判断当前网络是否可用，
                if (Netutils.isNetworkAvalible(SplashActivity.this)){
                    //checkVersion();
                    UpDatautils upDatautils = new UpDatautils(SplashActivity.this);
                    upDatautils.checkVersion();
                }else {
                    ToastUtil.showToast("当前网络不可用请检查网络");
                    Netutils.checkNetwork(SplashActivity.this);
                }

            }
        }, 2000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /***
     *
     * 1-1联网判断是否有新版本
     *
     */

    public void checkVersion(){

        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        HashMap<String,String> datas=new HashMap<>();
        datas.put("appName","mqsd");
        datas.put("appv","v1");
        datas.put("timestamp",str);
        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString3(s);
        datas.put("sign",sign);
        httphelper.create().NewdataHomePage2(Constants.UPDATA, datas, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e(s);

                JSONObject jonObject;
                try {
                    jonObject = new JSONObject(s);
                    String versionname = jonObject.getString("versionname");
                    String versioncode= jonObject.getString("versioncode");
                    desc = jonObject.getString("desc");
                    downurl = jonObject.getString("downloadurl");
                    //注意json的格式不要写错了，不同的数据之间用逗号隔开
                    System.out.println("版本的名字是："+versionname+" 版本号是："+versioncode+" 内容是："+desc+" 下载地址是："+ downurl);
                    int versioncode1 = getVersioncode();
                    if (getVersioncode()<Integer.valueOf(versioncode)) {
                        showDiowlog();
                    }else {
                        intofirstActivity();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("fail————————");
                    intofirstActivity();
                }

            }

            @Override
            public void fail(Exception e) {
                intofirstActivity();

            }
        });



    }

    /**
     *
     * 1.
     * 获取版本名称
     *
     */
    public String getVersionName(){
        @SuppressWarnings("unused")
        PackageManager packageManager = getPackageManager();
        try {
            //待会试试第一个采参数写上路径名会怎样
            //注意这个packname和某些参数很像，别敲错了
            //PermissionInfo permissionInfo = packageManager.getPermissionInfo(getPackageName(), 0);
            PackageManager packagename = getPackageManager();
            PackageInfo packageInfo = packagename.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;//这个是获取版本的名字
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

    /**
     *
     * 2.
     * 获取版本号
     */
    public int getVersioncode(){
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;

    }

    /****
     * 3.获得是否要更新
     *
     * 使用okhttp框架
     *
     * 在主线程写，因为okhttp是已经封装好的，所以在主线程中写
     */


    /***
     * 4.
     * 在okhttp框架中检测版本是否更新的弹窗！！！
     * 然后对话框也像是图示那样的要显示出来！！！！！
     *
     */
    public void showDiowlog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("更新提示");
        dialog.setMessage(desc);
        dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载
                downurl();
                intofirstActivity();
            }
        });
        dialog.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //进入主界面!!!!!
                intofirstActivity();

            }
        });
        //最后这个一定要加上！！！
        //不然的话点击了外部app就走不下去了。
        dialog.setCancelable(false);
        dialog.show();
    }

    /***
     * 5.下载apk
     *
     */

    public void downurl(){
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING);//正在下载
        Cursor c = manager.query(query);
        if(c.moveToNext()){
            //正在下载中，不重新下载
        }else {
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downurl));
            //设置允许使用的网络类型，这里是移动网络和wifi都可以
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            //显示在下载界面，即下载后的文件在系统下载管理里显示
            down.setVisibleInDownloadsUi(true);
            //设置下载标题
            down.setTitle("秒签速递");
            //显示Notification
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            //设置下载后文件存放的位置，在SDCard/Android/data/你的应用的包名/files/目录下面
            // down.setDestinationInExternalFilesDir(this, null, DOWNLOAD_FILE_NAME);
            //放在sd卡里面，测试可行！！2017.9.30
            down.setDestinationInExternalPublicDir("/emiaoqian/pictures",DOWNLOAD_FILE_NAME);
            //将下载请求放入队列,返回值为downloadId
            downloadId = manager.enqueue(down);
            intofirstActivity();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INSTALL) {// 判断请求码 说明是从安装页面返回的
            // 用户取消 安装 进入主页面
            intofirstActivity();
        }else if (requestCode==33){
            LogUtil.e("--我是返回的","执行了么");
            checkVersion();
        }
    }

    /***
     *
     * 7.进入主界面
     *
     */

    public  void intofirstActivity(){

        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        if (user_id.matches("[0-9]+")){
            Intent intent=new Intent(this,Mainactivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
        }

//        Intent intent = new Intent(this, GuideActivity.class);
//        startActivity(intent);
//        finish();
    }

}

