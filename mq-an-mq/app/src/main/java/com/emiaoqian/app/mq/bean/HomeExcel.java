package com.emiaoqian.app.mq.bean;

import java.util.List;

/**
 * Created by xiong on 2018/1/30.
 */

public class HomeExcel {


    /**
     * id : 1248
     * doc_no : 849872947999
     * user_id : 84
     * sender_name : 熊政
     * receive_id : 84
     * receive_name : 熊政
     * doc_name : QQ图片20170908142808
     * user_company : 熊政
     * receive_company : 个梵蒂冈
     * create_time : 2017-10-23 14:04:12
     * update_time : 2018-01-29 17:01:58
     * doc_status : 3
     * rec_status : 3
     * doc_type : 1
     * doc_relation_id : null
     * sign_time : 1508738699
     * pay_time : 1508738707
     * send_time : 1508738706
     * receive_time : 1517216518
     * role : 1
     * sign_return : 0
     * button : [{"name":"查看","url":"/sys/doc/docDetail/no/849872947999#item3"}]
     */

    public String id;
    public String doc_no;
    public String user_id;
    public String sender_name;
    public String receive_id;
    public String receive_name;
    public String doc_name;
    public String user_company;
    public String receive_company;
    public String create_time;
    public String update_time;
    public String doc_status;
    public String rec_status;
    public String doc_type;
    public Object doc_relation_id;
    public String sign_time;
    public String pay_time;
    public String send_time;
    public String receive_time;
    public int role;
    public int sign_return;
    public List<BannerInfo> button;



}