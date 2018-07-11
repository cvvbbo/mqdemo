package com.emiaoqian.app.mq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.bean.Newsbean;
import com.emiaoqian.app.mq.bean.RecentBean;
import com.emiaoqian.app.mq.bean.otherbean;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiong on 2018/3/5.
 */

public class aaa extends AppCompatActivity {

    public int a=3;


    //[{"name":"待开具","value":0,"url":""},{"name":"已开具","value":0,"url":""},{"name":"库存","value":0,"url":""}]

    String s="[\n" +
            "        {\n" +
            "            \"title\": \"6\",\n" +
            "            \"text\": \"我是你爸爸\",\n" +
            "            \"url\": \"http://www.baidu.com\",\n" +
            "            \"create_time\": \"1970-01-01\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\": \"5\",\n" +
            "            \"text\": \"我是你爸爸\",\n" +
            "            \"url\": \"http://www.sina.com\",\n" +
            "            \"create_time\": \"1970-01-01\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\": \"4\",\n" +
            "            \"text\": \"我是你爸爸\",\n" +
            "            \"url\": \"http://www.jd.com\",\n" +
            "            \"create_time\": \"2017-12-21\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\": \"标题3\",\n" +
            "            \"text\": \"我是你爸爸2\",\n" +
            "            \"url\": \"http://www.taobao.com\",\n" +
            "            \"create_time\": \"2017-12-23\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\": \"测试标题2\",\n" +
            "            \"text\": \"我是你爸爸吧\",\n" +
            "            \"url\": \"http://www.qq.com\",\n" +
            "            \"create_time\": \"2017-12-21\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\": \"aa1\",\n" +
            "            \"text\": \"百度呵呵\",\n" +
            "            \"url\": \"http://www.baidu.com\",\n" +
            "            \"create_time\": \"2017-12-21\"\n" +
            "        }\n" +
            "    ]";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa);

        if (a==3){
            LogUtil.e("--66"+"ss");
        }else if (a==3){
            LogUtil.e("--66"+"zz");
        }
        //aa();
        bb();

    }


    public void aa(){
        long l = System.currentTimeMillis();
        String  str=String.valueOf(l);
        HashMap<String,String> datas=new HashMap<>();
        datas.put("appv","android");
        datas.put("theme_type","invoice");
        datas.put("page","1");
        datas.put("timestamp",str);
        datas.put("user_id","84");

        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString3(s);
        datas.put("sign", sign);



        httphelper.create().NewdataHomePage2("http://192.168.3.112:8081/v1/homeIndex/getHomeData",
                datas,  new httphelper.httpcallback() {
                    @Override
                    public void success(String s) {
                        try {
                            LogUtil.e("数据是--"+s);
                            JSONObject jsonObject=new JSONObject(s);
                            String data = jsonObject.getString("data");
                            JSONObject jsonObject1 = new JSONObject(data);
                            //用这个获取每一个字段
                            String count = jsonObject1.getString("recent");
                            RecentBean recentBean = GsonUtil.parseJsonToBean(count, RecentBean.class);
                            RecentBean.LeftBean.DataBean data1 = recentBean.left.data;
                            LogUtil.e(recentBean+"");

//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }

                    @Override
                    public void fail(Exception e) {

                    }
                });
    }


    //[{"day":"05-22","data":[{"AcceptTime":"13:47","AcceptStation":"暂无物流轨迹"}]}]
    public void bb(){
        ArrayList<NewHomeBean1> newsbeen=(ArrayList<NewHomeBean1>)GsonUtil.parseJsonToList(s,new TypeToken<List<NewHomeBean1>>(){}.getType());
        LogUtil.e(newsbeen+"");
    }
}
