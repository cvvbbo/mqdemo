package com.emiaoqian.app.mq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.adapter.GirdViewStatusApadter;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.RetrofitUtil;
import com.emiaoqian.app.mq.utils.StatebarUtils2;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiong on 2018/5/23.
 */

public class bbb  extends AppCompatActivity {

    private GridView gv;
    HashMap<String, String> datas = new HashMap<>();
    int arr[] = {23,12,46,24,87,65,18,14,43,434,65,76};
    String a="China from am I.";
    ArrayList<String> b=new ArrayList<>();
    private ListView lv;
    private TextView tv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbb);
//        lv = (ListView) findViewById(R.id.lv);
//        lv.setAdapter(new Listviewadapter());
        tv = (TextView) findViewById(R.id.tv);
        ViewParent parent = tv.getParent();
        LogUtil.e(parent+"--父布局是--");
        View rootView = tv.getRootView();
        LogUtil.e(rootView+"--根布局是--");

        //xuanze();
        //maopao();
        changeposition();


        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        HashMap<String, String> datas = new HashMap<>();
        datas.put("appv", "v1");
        datas.put("theme_type", "doc");
        //datas.put("page","1");
        datas.put("timestamp", str);

        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString3(s);
        //  datas.put("sign", sign);

//        RetrofitUtil.create().NetWord(datas, new RetrofitUtil.httpcallback() {
//            @Override
//            public void success(String s) {
//                ToastUtil.showToastCenter(s);
//            }
//
//            @Override
//            public void fail(String e) {
//                ToastUtil.showToastCenter(e);
//            }
//        });


    }


    public void button(View view){


        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                ToastUtil.showToastCenter("开始");

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogUtil.e("weixin001"+map);
                ToastUtil.showToast(map+"-----------success");
                //Set<Map.Entry<String, String>> entries = map.entrySet();
                String unionid = map.get("unionid");
                String name = map.get("name");
                String openid = map.get("openid");
                String iconurl = map.get("iconurl");


                HashMap<String,String> datas=new HashMap<String, String>();
                datas.put("openid",openid);
                datas.put("unionid",unionid);
                datas.put("nickname",name);
                datas.put("headimg",iconurl);
                PushAgent pushAgent = PushAgent.getInstance(getApplicationContext());
                String device_token = pushAgent.getRegistrationId();
                datas.put("device_no",device_token);
                long l = System.currentTimeMillis();
                String timetamp = String.valueOf(l);
                datas.put("timestamp",timetamp);
                datas.put("appv","v1");
                String unsign = EncodeBuilder.javaToJSONNewPager1(datas);
                String sign = EncodeBuilder.newString3(unsign);
                datas.put("sign",sign);
                httphelper.create().NewdataHomePage2(Constants.CONST_HOST + "/v2/homeIndex/wxLogin", datas, new httphelper.httpcallback() {
                    @Override
                    public void success(String s) {
                        ToastUtil.showToastCenter("返回的数据是--"+s);
                        LogUtil.e("weixin001"+s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");
                            if (code.equals("100000")){

                                String data = jsonObject.getString("data");
                                NewHomeBean1 newHomeBean1 = GsonUtil.parseJsonToBean(data, NewHomeBean1.class);
                                if (newHomeBean1!=null){
                                    //已经用微信注册过
                                    ToastUtil.showToastCenter("已经用微信注册过");
                                }else {
                                    //没有用微信注册过
                                    ToastUtil.showToastCenter("没有用微信注册过");
                                }

                            }else {
                                ToastUtil.showToast("程序开了点小差");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void fail(Exception e) {

                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                //result.setText("错误" + throwable.getMessage());
                PushAgent pushAgent = PushAgent.getInstance(getApplicationContext());
                String device_token = pushAgent.getRegistrationId();
                LogUtil.e(device_token);
                LogUtil.e("weixin001"+throwable.getMessage()+"----fail");
                ToastUtil.showToastCenter("微信登录开了点小差，请换其他登录方式");
                ToastUtil.showToast(throwable.getMessage()+"----fail");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                ToastUtil.showToast("取消了");
            }
        });


    }

    //好像微信授权过的第二次就不会跳出授权页 7.10
    public void unbind(View view){
        UMShareAPI.get(this).deleteOauth(this,SHARE_MEDIA.WEIXIN , authListener);

    }


    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            ToastUtil.showToast("开始");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtil.showToast(data+"---------success");
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            ToastUtil.showToast(t.getMessage()+"---失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtil.showToast("取消了--");
        }
    };


    class Listviewadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           // View view = View.inflate(parent.getContext(), R.layout.my_dialog_item_view, parent);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_dialog_item_view, parent, false);

            return view;
        }
    }


    public void xuanze() {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j <= arr.length-1 ; j++) {
                if (arr[i] < arr[j]) {
                    int t = arr[i];
                    arr[i] = arr[j];
                    arr[j] = t;
                }
            }
        }
        for(int i=0;i<arr.length;i++){
            //System.out.print("--排序之后的数组是--"+arr[i]+"\t");
            LogUtil.e("--排序之后的数组是--"+arr[i]);
        }
    }


    //这个冒泡排序是对的
    public void maopao() {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    int t = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = t;
                }
            }
        }

        for(int i=0;i<arr.length;i++){
            //System.out.print("--排序之后的数组是--"+arr[i]+"\t");
            LogUtil.e("--排序之后的数组是--"+arr[i]);
        }
    }

    public void changeposition(){
        String[] aftera = a.split(" ");
        String[] aftera1=new String[aftera.length];
       // LogUtil.e(aftera+"");
        for (int i=aftera.length-1;i>=0;--i){
            b.add(aftera[i]);
        }
        LogUtil.e(b+"");
        for (int i=0;i<b.size();i++){
            aftera1[i]=b.get(i);
        }
        String s = Arrays.toString(aftera1);
        LogUtil.e(s);

    }
}
