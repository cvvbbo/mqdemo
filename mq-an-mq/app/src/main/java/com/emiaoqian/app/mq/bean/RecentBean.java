package com.emiaoqian.app.mq.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiong on 2018/5/14.
 */

public class RecentBean {


        public LeftBean left;
        public RightBean right;



        public static class LeftBean {
            /**
             * name : 最新发票
             * data : {"1":"待收票","2":"收件时间：2018-05-11","3":"隐藏式偏光夹片太阳镜","4":"￥128","5":"百智慧生活","6":"legend","7":"秒签速递","8":"zpq","9":"待收票"}
             */

            /***
             * 这个是当用户未登录或者新用户登录的时候 6.13
             * "img": "http://192.168.3.112/static/mobile/sys/img/pracel_pick3.png",
             "  desc": "无最新寄收文件",
             "  button": "我要寄件",
             "  url": "http://192.168.3.112/mobile/createdoc/index"
             *
             *
             */


            public String img;
            public String desc;
            public String button;
            public String url;
            //以上字段都是当用户没登录获取是因用户的时候。6.13

            public String name;
            public DataBean data;


            public static class DataBean {


                @SerializedName("1")
                public String _$1;
                @SerializedName("2")
                public String _$2;
                @SerializedName("3")
                public String _$3;
                @SerializedName("4")
                public String _$4;
                @SerializedName("5")
                public String _$5;
                @SerializedName("6")
                public String _$6;
                @SerializedName("7")
                public String _$7;
                @SerializedName("8")
                public String _$8;
                @SerializedName("9")
                public String _$9;

            }
        }

        public static class RightBean {


            public String name;
            public String url;


        }
    }
