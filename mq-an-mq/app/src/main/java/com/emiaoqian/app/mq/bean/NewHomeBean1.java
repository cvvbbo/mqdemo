package com.emiaoqian.app.mq.bean;

import java.util.List;

/**
 * Created by xiong on 2018/5/14.
 */

public class NewHomeBean1 {

    public NewHomeBean1(String title,String url) {
        this.url = url;
        this.title = title;
    }

    public NewHomeBean1( String title,String url, boolean single) {
        this.url = url;
        this.title = title;
        this.single = single;
    }

    public String title1;
        public String title2;
        public String img;
        public String name;
        public int value;
        public String url;
        public String title;
        public String code;
        public String msg;
        public String scene;
        public String text;
        public String create_time;
        public String id;
        public String mobile;
        public String realname;
        public boolean single=false;
}
