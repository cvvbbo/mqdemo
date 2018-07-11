package com.emiaoqian.app.mq.utils;

import android.os.Handler;
import android.util.Log;

import com.emiaoqian.app.mq.application.MyApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xiong on 2017/8/15.
 */

public class httphelper {

    //以后凡是自己写的每一个类都要仔细检查！！！。。。

    //校验json数据的
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    private static httphelper helpter=new httphelper();
    private final OkHttpClient okHttpClient;
    //尝试在联网的工具类里面就试着让更新界面执行在主线程
    Handler handler=new Handler();
    private FileOutputStream fos;


    public  static httphelper create(){

        return helpter;

    }


    private httphelper(){
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(7,TimeUnit.SECONDS)
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor()).build();

    }




    //验证签名。
    public void dopost(String url, String newsign, final httpcallback h){
        final Request request=new Request.Builder().url(url).post(RequestBody.create(JSON,newsign)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });


            }
        });

    }



    /***记住签名在另一个类里面，自己挖的坑。。。***/
    //公司名companyName（普通网页的）这里有个坑啊，后台貌似吧app改成appv了，在别的请求里面，不知道这个不能能用2.6
    public void saveDeviceNo2(String url, String umtoken, String user_id, String sign,String time,final httpcallback h){

        //往请求里面添加参数
//        RequestBody formBody = new FormBody.Builder()
//                .add("user_id",user_id)
//                .add("device_no", umtoken)
//                .add("mid","abcdefg")
//                .add("app","android")
//                .add("timestamp",time)
//                .add("sign", sign)
//                .add("app","android")
//                .build();
        RequestBody formBody = new FormBody.Builder()
                .add("userId",user_id)
                .add("deviceNo", umtoken)
                .add("mid","abcdefg")
                .add("appv","android")
                .add("timestamp",time)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }


    //获取首页信息
    public void saveDeviceNo1(String url,String sign,String time,final httpcallback h){

        LogUtil.e("----地址是-"+url);

        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");

        if (user_id.matches("[0-9]+")){
            RequestBody formBody = new FormBody.Builder()
                    .add("userId",user_id)
                    //.add("device_no", umtoken)
                    .add("mid","abcdefg")
                    .add("appv","android")
                    .add("timestamp",time)
                    .add("sign", sign)
                    .build();

            Request request=new Request.Builder().url(url).post(formBody).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.fail(e);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    ResponseBody body = response.body();
                    //待会测试下这个响应码2.5
                    int code = response.code();
                    LogUtil.e("---响应码是多少---"+code);
                    final String string = body.string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.success(string);
                        }
                    });
                }
            });


        }else {
            RequestBody formBody = new FormBody.Builder()
                    //.add("userId",user_id)
                    //.add("device_no", umtoken)
                    .add("mid","abcdefg")
                    .add("appv","android")
                    .add("timestamp",time)
                    .add("sign", sign)
                    .build();

            Request request=new Request.Builder().url(url).post(formBody).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.fail(e);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    ResponseBody body = response.body();
                    //待会测试下这个响应码2.5
                    int code = response.code();
                    LogUtil.e("---响应码是多少---"+code);
                    final String string = body.string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.success(string);
                        }
                    });
                }
            });

        }


//        RequestBody formBody = new FormBody.Builder()
//                    .add("userId","90")
//                    //.add("device_no", umtoken)
//                    .add("mid","abcdefg")
//                    .add("appv","android")
//                    .add("timestamp",time)
//                    .add("sign", sign)
//                    .build();
//
//            Request request=new Request.Builder().url(url).post(formBody).build();
//
//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, final IOException e) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            h.fail(e);
//                        }
//                    });
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                    ResponseBody body = response.body();
//                    //待会测试下这个响应码2.5
//                    int code = response.code();
//                    LogUtil.e("---响应码是多少---"+code);
//                    final String string = body.string();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            h.success(string);
//                        }
//                    });
//                }
//            });



    }


    //companyName 公司名（这个是文件管理的）
    public void saveDeviceNo3(String url, String pagenum,String user_id,String fileid,String companyName,String type, String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
                .add("userId",user_id)
                .add("mid","abcdefg")
                .add("pageNum",pagenum)
                .add("id",fileid)
                .add("companyName",companyName)
                .add("type",type)
                .add("app","android")
                .add("timestamp",time)
                //sign字段是其他几个字段加密之后的字符串
                .add("sign", sign)
                .add("app","android")
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }


    //这个是搜索2.6
    public void saveDeviceNo4(String url, String data,String pageNum,String user_id,String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
                .add("userId",user_id)
                //.add("device_no", umtoken)
                .add("data",data)
                .add("mid","abcdefg")
                .add("appv","android")
                .add("timestamp",time)
                .add("pageNum",pageNum)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }

    //新版首页 5.13 再次改版(这里的user_id也是以前的userId,因为写接口的人变了  5.13)
    public void NewdataHomePage(String url, String theme_type,String page,String user_id,String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
               // .add("user_id",user_id)
                //.add("device_no", umtoken)
                .add("appv","android")
                .add("theme_type",theme_type)
                .add("timestamp",time)
                .add("page",page)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }

    //新版首页 5.13 再次改版(这里的user_id也是以前的userId,因为写接口的人变了  5.13)
    public void NewdataHomePage2(String url, Map<String ,String> datas, final httpcallback h){
        LogUtil.e("--请求的地址是--"+url);
        RequestBody formBody=null;
        FormBody.Builder builder = new FormBody.Builder();
        // FormBody.Builder add=null;
        Iterator iter = datas.entrySet().iterator();
        while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry) iter.next();
        String key = (String) entry.getKey();
        String val = (String) entry.getValue();

            //这里不能直接就new FormBody.Builder().add("xxx","xxx").build();
            //这样的话会每次都new一个，只能先把new FormBody.Builder()这个提取出来
            formBody=builder.add(key,val).build();

        }

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }



   public  interface  httpcallback{
       void success(String s);
       void fail(Exception e);
   }


   //请求里面添加sessionid然后保存下来，方便下次请求验证！
    public class AddCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences= sharepreferenceUtils.getHashsetdata(MyApplication.mcontext, "cookie", new HashSet<String>());
            for (String cookie : preferences) {
                //后台cookie的开头就是叫cookie。。。
                builder.addHeader("Cookie", cookie);
                Log.e("OkHttp", "Adding Header: " + cookie);
            }
            return chain.proceed(builder.build());
        }
    }

    //这里是接收cookie
    public class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                    Log.e("---Received",cookies+"");
                }
                sharepreferenceUtils.saveHashsetdata(MyApplication.mcontext,"cookie",cookies);
            }

            return originalResponse;
        }
    }

}
