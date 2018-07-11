package com.emiaoqian.app.mq.utils;

import com.emiaoqian.app.mq.application.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xiong on 2017/8/21.
 */

public class EncodeBuilder {

    public static String Mylord(String unsign){
        //其实不叫封装的封装。。
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        String encode = EncodeUtils.encode(sortmap);
        String token = t3.addString(encode, "MIAOQIAN_API_TOKEN");
        String upwrite = t3.upwrite(token);
        String encode1 = EncodeUtils.encode(upwrite);
        String upwrite1 = t3.upwrite(encode1);
        String s=(unsign.substring(0,unsign.length()-1));
        String newsign=s+",\"sign\":\""+upwrite1+"\"}";
        return newsign;

    }

    public static String Yougrace(String unsign){
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        String encode = EncodeUtils.encode(sortmap);
        String token = t3.addString(encode, "MIAOQIAN_API_TOKEN");
        String upwrite = t3.upwrite(token);
        String encode1 = EncodeUtils.encode(upwrite);
        String upwrite1 = t3.upwrite(encode1);
        return  upwrite1;
    }

    //这个是网页的。。。
    public static  String newString(String unsign){
        unsign=(unsign.substring(0,(unsign.length()-1))+",\"mid\":\"abcdefg\",\"appv\":\"android\"}");
        LogUtil.e("拼接字符串的时候--"+unsign);
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        LogUtil.e("---字典排序之后--"+sortmap);
        //md5加密
        String encode = EncodeUtils.encode(sortmap);
        LogUtil.e("--没加token之前"+encode);
        String upwrite = t3.upwrite(encode);
        String s = t3.addString(upwrite, "MIAOQIAN_API_TOKEN");
        String encode1 = EncodeUtils.encode(s);
        String upwrite1 = t3.upwrite(encode1);
        return upwrite1;
    }

    //这个是获取登录信息的
    public static String javaToJSON(String userid,String Umtoken,String time) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userid);
            jsonObj.put("deviceNo", Umtoken);
            jsonObj.put("timestamp",time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    //测试新页面，获取log的，我也不知道什么是log。。1.17
    public static String javaToJSON2(String time) {
        JSONObject jsonObj = new JSONObject();
        try {

            //这里是初次登录和非初次登录的判断
            String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");
            if (user_id.matches("[0-9]+")){
                jsonObj.put("userId", user_id);
                jsonObj.put("timestamp",time);

            }else {

                //下面这个是初次登录，没有获取userid
                //jsonObj.put("userId", userid);
                jsonObj.put("timestamp", time);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    public static String javaToJSON3(String userid,String time,String pageNum,String fileid,String companyName,String type) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userid);
            jsonObj.put("timestamp",time);
            jsonObj.put("pageNum",pageNum);
            jsonObj.put("id",fileid);
            jsonObj.put("companyName",companyName);
            jsonObj.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    public static String javaToJSONNewPager(String userid,String time,String pageNum,String theme_type) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("appv","android");
           // jsonObj.put("user_id", userid);
            jsonObj.put("timestamp",time);
            jsonObj.put("page",pageNum);
            jsonObj.put("theme_type",theme_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    public static String javaToJSONNewPager1(Map<String,String> datas) {
        JSONObject jsonObj = new JSONObject();

        Iterator iter = datas.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            try {
                jsonObj.put(key,val);
            }catch (JSONException e){

            }

        }

//        try {
//            jsonObj.put("appv","android");
//            // jsonObj.put("user_id", userid);
//            jsonObj.put("timestamp",time);
//            jsonObj.put("page",pageNum);
//            jsonObj.put("theme_type",theme_type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return jsonObj.toString();
    }


    //搜索的拼接字符串
    public static String javaToJSON4(String userid,String time,String data,String PageNum) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userid);
            jsonObj.put("timestamp",time);
            jsonObj.put("data",data);
            jsonObj.put("pageNum",PageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }



    //新页面1.17
    public static  String newString2(String unsign){
        unsign=(unsign.substring(0,(unsign.length()-1))+",\"mid\":\"abcdefg\",\"appv\":\"android\"}");
        LogUtil.e("拼接字符串的时候--"+unsign);
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        LogUtil.e("---字典排序之后--"+sortmap);
        //md5加密
        String encode = EncodeUtils.encode(sortmap);
        LogUtil.e("--没加token之前"+encode);
        String upwrite = t3.upwrite(encode);
        String s = t3.addString(upwrite, "MIAOQIAN_API_TOKEN");
        String encode1 = EncodeUtils.encode(s);
        String upwrite1 = t3.upwrite(encode1);
        LogUtil.e("--全部加密之后--"+upwrite1);
        return upwrite1;
    }

    public static  String newString3(String unsign){
        //unsign=(unsign.substring(0,(unsign.length()-1))+",\"appv\":\"android\"}");
        LogUtil.e("拼接字符串的时候--"+unsign);
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        LogUtil.e("---字典排序之后--"+sortmap);
        //md5加密
        String encode = EncodeUtils.encode(sortmap);
        LogUtil.e("--没加token之前"+encode);
        String upwrite = t3.upwrite(encode);
        String s = t3.addString(upwrite, "MIAOQIAN_API_TOKEN");
        String encode1 = EncodeUtils.encode(s);
        String upwrite1 = t3.upwrite(encode1);
        LogUtil.e("--全部加密之后--"+upwrite1);
        return upwrite1;
    }



}
