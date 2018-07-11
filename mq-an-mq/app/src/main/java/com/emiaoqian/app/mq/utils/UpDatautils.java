package com.emiaoqian.app.mq.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.emiaoqian.app.mq.activity.GuideActivity;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.application.MyApplication;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by xiong on 2018/7/9.
 */

public class UpDatautils {

    private String mDesc;
    private String mDownloadurl;
    private String desc;
    private DownloadManager manager ;
    private long downloadId;
    private String downurl;
    private static final String DOWNLOAD_FILE_NAME = "app-release.apk";

    Activity mcontext;


    public UpDatautils(Activity mcontext){
        manager =(DownloadManager)mcontext.getSystemService(mcontext.DOWNLOAD_SERVICE);
        this.mcontext=mcontext;
    }


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
                        if (mcontext.getClass().getSimpleName().equals("SplashActivity")) {
                            intofirstActivity(mcontext);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("fail————————");
                    if (mcontext.getClass().getSimpleName().equals("SplashActivity")) {
                        intofirstActivity(mcontext);
                    }
                }

            }

            @Override
            public void fail(Exception e) {
                if (mcontext.getClass().getSimpleName().equals("SplashActivity")) {
                    intofirstActivity(mcontext);
                }

            }
        });

    }

    //只有在指定的activity中才要进入activity中
    public  void intofirstActivity(Activity activity){
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        if (user_id.matches("[0-9]+")){
            Intent intent=new Intent(activity,Mainactivity.class);
            mcontext.startActivity(intent);
            activity.finish();
        }else {
            Intent intent = new Intent(activity, GuideActivity.class);
            mcontext.startActivity(intent);
            activity.finish();
        }

    }

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
            if (mcontext.getClass().getSimpleName().equals("SplashActivity")) {
                intofirstActivity(mcontext);
            }
        }

    }

    public int getVersioncode(){
        PackageManager packageManager = mcontext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mcontext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;

    }


    public void showDiowlog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(mcontext);
        dialog.setTitle("更新提示");
        dialog.setMessage(desc);
        dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载
                downurl();
                if (mcontext.getClass().getSimpleName().equals("SplashActivity")) {
                    intofirstActivity(mcontext);
                }
            }
        });
        dialog.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //进入主界面!!!!!
                if (mcontext.getClass().getSimpleName().equals("SplashActivity")) {
                    intofirstActivity(mcontext);
                }

            }
        });
        //最后这个一定要加上！！！
        //不然的话点击了外部app就走不下去了。
        dialog.setCancelable(false);
        dialog.show();
    }
}
