package com.emiaoqian.app.mq.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.emiaoqian.app.mq.BuildConfig;
import com.emiaoqian.app.mq.utils.ChannelUtil;
import com.emiaoqian.app.mq.utils.FetchPatchHandler;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by xiong on 2017/9/8.
 */

public class MyApplication extends Application {

    public static Context mcontext;
    public static final String TAG = "UMENGPUSH";
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    public static boolean isDebug = true;
    public static PushAgent mPushAgent;

    //微信热修复
    private ApplicationLike tinkerApplicationLike;

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = this;
        //友盟统计，9月底加的，现在11.3api已经修改了
        UMConfigure.setLogEnabled(true);
        String channel = ChannelUtil.getChannel(this);

        //改版之后应该是这个(友盟统计),最后一个参数是友盟推送的 （线上的）
        UMConfigure.init(this, "59c093d875ca355a7b000039", channel, UMConfigure.DEVICE_TYPE_PHONE, "17ac0b47feb620a7975963cc00c61b20");
        //自己环境的
        //UMConfigure.init(this, "5ad8b78af29d9803c7000325", channel, UMConfigure.DEVICE_TYPE_PHONE, "00351a83ad4480c44f501265c39ec4b2");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setSessionContinueMillis(3000);

        //AjnLBV5S3uM9yG0xngVTvl7ONIGeNLyjaEsPG3vDPQd_
        //AomTTyYo_fIh1MLsr0e5cr1gTIE0gbAXaahxTaLQVM9H

        //友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtil.e("设备号是"+deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                LogUtil.e(s,s1);
            }
        });


        /**腾讯的内核**/
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " 是否是腾讯的内核 " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);


        if (BuildConfig.TINKER_ENABLE) {

            // 我们可以从这里获得Tinker加载过程的信息
            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

            // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
            TinkerPatch.init(tinkerApplicationLike)
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true);

            // 每隔3个小时去访问后台时候有更新,通过handler实现轮训的效果
            //这里也很操蛋，事件必须是1到24小时之间
            new FetchPatchHandler().fetchPatchWithInterval(1);
            Log.i("TAG", "tinker init");
        }


    }

    //友盟分享
    {
        PlatformConfig.setQQZone("1106318875", "9AXym16SgAD8Oh6n");
        PlatformConfig.setDing("dingoaxae4ifbacy180aph");
        PlatformConfig.setAlipay("2015111700822536");
        PlatformConfig.setWeixin("wxe4712bad72a62d4b", "59a65269ddde009c5b219eb587292ef2");
        PlatformConfig.setSinaWeibo("2868127155", "43ac30862f7426532f157beb3b39c49e", "https://api.weibo.com/oauth2/default.html");
    }

}
