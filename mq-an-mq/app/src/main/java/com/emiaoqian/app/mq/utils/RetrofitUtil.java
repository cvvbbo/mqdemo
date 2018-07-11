package com.emiaoqian.app.mq.utils;

import android.os.Handler;

import com.emiaoqian.app.mq.Interface.MyNetInterface;
import com.emiaoqian.app.mq.application.MyApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiong on 2018/6/18.
 */

public class RetrofitUtil {


    private Retrofit retrofit;
    private static RetrofitUtil retrofitUtil=new RetrofitUtil();

    public RetrofitUtil(){
        retrofit = new Retrofit.Builder()
                //注意，服务器主机应该以/结束，
                .baseUrl("http://192.168.3.112:8081/")//设置服务器主机
                .addConverterFactory(GsonConverterFactory.create())//配置Gson作为json的解析器
                .build();
    }


    public static RetrofitUtil create(){
        return  retrofitUtil;

    }

    public void NetWord(Map<String,String> datas, final httpcallback h) {
        //真正的网络请求（这里说是什么动态代理）
        MyNetInterface myNetInterface = retrofit.create(MyNetInterface.class);

        Call<ResponseBody> homeData = myNetInterface.getHomeData1(datas);
        homeData.enqueue(new Callback<ResponseBody>() {
            //这里的请求参数不能随便改，能改的都是在外面的 6.19
            @Override
            public void onResponse(Call<ResponseBody> call,  Response<ResponseBody> response) {
                try {

                    //retrofit里面有个errorbody是展示错误信息的！！
                    int code = response.code();
                    if (code==200){
                        String SuccessString = response.body().string();
                        h.success(SuccessString);
                    }else {
                        String ErrorString = response.errorBody().string();
                        h.success(ErrorString);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                h.fail(t.getMessage());


            }
        });
    }


    public  interface  httpcallback{
        void success(String s);
        void fail(String e);
    }






}
