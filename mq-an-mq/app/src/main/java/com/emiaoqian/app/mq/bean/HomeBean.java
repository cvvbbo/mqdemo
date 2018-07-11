package com.emiaoqian.app.mq.bean;

import java.util.ArrayList;

/**
 * Created by xiong on 2018/1/19.
 */

public class HomeBean {

    public int status;
    public int lastDocType=-1; //文件的类型
    public ArrayList<BannerInfo> banner;
    public ArrayList<Appdata> app;
    //因为bannerinfor返回的数据和 add字段里面需要的是一样的，就直接用了2.8
    public ArrayList<BannerInfo> add;



}
