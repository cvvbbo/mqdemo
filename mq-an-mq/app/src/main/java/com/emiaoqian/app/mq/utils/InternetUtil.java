package com.emiaoqian.app.mq.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by xiong on 2017/9/8.
 */

public class InternetUtil {

    private static final int REQUEST_CODE_INSTALL = 1000;
    private static final String DOWNLOAD_FILE_NAME = "app-release.apk";
    Handler h =new Handler();
    private String mDesc;
    private String mDownloadurl;
    private String desc;

    private DownloadManager manager ;


    private long downloadId;
    static Activity a;


    public InternetUtil(Activity a) {
        this.a=a;
        manager =(DownloadManager)a.getSystemService(DOWNLOAD_SERVICE);
    }

//    public  void checkVersion(){
//        Log.e("---","执行了么");
//        OkHttpClient okHttpClient=new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS).build();
//        //然后这里要创建一个保存常量的接口
//        Request request=new Request.Builder().get()
//                .url(Constants.update).build();
//
//        Call newCall = okHttpClient.newCall(request);
//        //异步请求
//        newCall.enqueue(new Callback() {
//            //不要抛异常，都要捕获下来！！！！！
//            @Override
//            public void onResponse(Call arg0, Response arg1) throws IOException {
//                if (!arg1.isSuccessful()) {// 200是正常
//                    a.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(a, "数据异常", Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    });
//
//                    return;
//                }
//
//                ResponseBody body = arg1.body();
//                String result = body.string();
//                System.out.println("结果是"+result);
//
//                //通过json解析数据
//                JSONObject jonObject;
//                try {
//                    jonObject = new JSONObject(result);
//                    int versionname = jonObject.getInt("versionname");
//                    int versioncode= jonObject.getInt("versioncode");
//                    desc = jonObject.getString("desc");
//                    String downurl = jonObject.getString("downloadurl");
//                    //注意json的格式不要写错了，不同的数据之间用逗号隔开
//                    System.out.println("版本的名字是："+versionname+" 版本号是："+versioncode+" 内容是："+desc+" 下载地址是："+downurl);
//                    if (getVersioncode()<versioncode) {
//
//                        //这里记得改掉！！
//                        //showDiowlog();
//                        a.runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                showDiowlog();
//
//                            }
//                        });
//
//                    }else {
//                        //进入主界面
//                        Toast.makeText(a,"当前已是最新版",Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                    Toast.makeText(a,"获取服务器失败",Toast.LENGTH_SHORT).show();
//
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call arg0, IOException arg1) {
//                //打印这个异常
//                arg1.printStackTrace();
//                //即使联网失败了也要进入主界面
//                Toast.makeText(a,"获取服务器失败",Toast.LENGTH_SHORT).show();
//
//
//            }
//        });
//    }

    //检查版本
    private  int getVersioncode(){
        PackageManager packageManager = a.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(a.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;

    }

    public String getVersionName(){

        @SuppressWarnings("unused")
        PackageManager packageManager = a.getPackageManager();

        try {
            //待会试试第一个采参数写上路径名会怎样
            //注意这个packname和某些参数很像，别敲错了
            //PermissionInfo permissionInfo = packageManager.getPermissionInfo(getPackageName(), 0);
            PackageManager packagename = a.getPackageManager();
            PackageInfo packageInfo = packagename.getPackageInfo(a.getPackageName(), 0);
            return packageInfo.versionName;//这个是获取版本的名字
        } catch (Exception e) {

            e.printStackTrace();
        }

        return "";

    }



    //吐司提示
    private  void showDiowlog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(a);
        dialog.setTitle("更新提示");
        dialog.setMessage(desc);
        dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载
                //downurl();
            }
        });

        dialog.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();

            }
        });
        //最后这个一定要加上！！！
        dialog.show();
    }


//    //下载
//    public void downurl(){
//
//        DownloadManager.Query query = new DownloadManager.Query();
//        query.setFilterById(downloadId);
//        query.setFilterByStatus(DownloadManager.STATUS_RUNNING);//正在下载
//        Cursor c = manager.query(query);
//        if(c.moveToNext()){
//            //正在下载中，不重新下载
//        }else {
//            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(Constants.downurl));
//            //设置允许使用的网络类型，这里是移动网络和wifi都可以
//            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//            //显示在下载界面，即下载后的文件在系统下载管理里显示
//            down.setVisibleInDownloadsUi(true);
//            //设置下载标题
//            down.setTitle("秒签速递");
//            //显示Notification
//            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//            //设置下载后文件存放的位置，在SDCard/Android/data/你的应用的包名/files/目录下面
//            down.setDestinationInExternalFilesDir(a, null, DOWNLOAD_FILE_NAME);
//            //down.setDestinationInExternalPublicDir(null,DOWNLOAD_FILE_NAME);
//            //将下载请求放入队列,返回值为downloadId
//            downloadId = manager.enqueue(down);
//
//
//
//
//        }
//
//    }


}

