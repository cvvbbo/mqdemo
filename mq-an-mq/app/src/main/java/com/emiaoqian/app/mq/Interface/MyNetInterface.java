package com.emiaoqian.app.mq.Interface;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xiong on 2018/6/18.
 */

public interface MyNetInterface {


    //使用post请求需要这个编码(如果表单的参数不正确那么返回的也是respone为空)
    @FormUrlEncoded
    @POST("v2/homeIndex/getHomeData")
    Call<ResponseBody> getHomeData(@Field("appv") String v1,
                                   @Field("theme_type") String theme_type,
                                   @Field("timestamp") String timestamp);


    //只有参数传递都是正确的时候返回的才是正确的（但是这样有个缺点，就是根本就不知道哪里错了。。。6.18）
    //上面说的返回为空是因为retrofit里面当往服务器提交参数不正确的时候
    @FormUrlEncoded
    @POST("v2/homeIndex/getHomeData")
    Call<ResponseBody> getHomeData1(@FieldMap Map<String,String> datas);





}
