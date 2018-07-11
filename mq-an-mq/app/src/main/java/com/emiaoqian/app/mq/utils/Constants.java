package com.emiaoqian.app.mq.utils;

public class Constants {

    //public static final String CONST_HOST = "https://www.emiaoqian.com";

//    public static final String CONST_HOST = "http://192.168.3.112:8081";
//
//    public static final String CONST_HOST1 = "http://192.168.3.112"; //所有被当成网页的都走这个地址 。。1.29

    public static final String CONST_HOST = "https://cpi.emiaoqian.com";

    public static final String CONST_HOST1 = "https://www.emiaoqian.com"; //所有被当成网页的都走这个地址 。。1.29

    //登录的时候提交表单的地址
     public static final String GET_USE_INFOR= CONST_HOST+"/v1/app/saveDeviceNo";

    //新版的下载地址
    public static final String UPDATA=CONST_HOST+"/v1/appVersion";


    //192.168.3.112 //永久地址

   // public static final String CONST_HOST = "http://192.168.3.112";

    //真正下载的更新地址
    public static final String UP_DATA_URL="https://www.emiaoqian.com/v1/appVersion";




    public static final String LOGIN = CONST_HOST1 + "/sys/pub/login";
    public static final String QUICK = CONST_HOST1 + "/sys/waybill/quick";
    public static final String FIND = CONST_HOST1+"/sys/waybill/search";
    public static final String TESTURL = CONST_HOST1+"/sys/index/myApp";
    public static final String ABOUT_ME=CONST_HOST1+"/aboutApp";

    public static final String REGIST=CONST_HOST+"/sys/pub/regist";
    public static final String LOGINOUT=CONST_HOST1+"/sys/pub/logout";

    //新版消息界面
    public static final  String MESSAGE=CONST_HOST1+"/sys/message/index";

    //新版文件管理界面
    public static final  String FILEMANGER=CONST_HOST1+"/sys/mydoc/documentManage";

    public static final String MQNEWS=CONST_HOST1+"/mobile/index/news";




    //已登录
    public static final  String ALRAEDY_LOGIN=CONST_HOST+"/sys/index";

    //工作台首页相关
    public static final String ABOUT_WORK=CONST_HOST+"/sys/profile";



    //=================原生工作台的数据======================= 17.17

    public static final String TEST_HOME=CONST_HOST+"/v1/app/home";

    public static final String TEST_LOG=CONST_HOST+"/v1/app/log";

    public static final String TEST_DOCLIST =CONST_HOST+"/v1/app/docList";

    //搜索的接口
    public static final String TEST_QUERY=CONST_HOST+"/v1/app/queryDoc";

    //新界面里面的分享按钮
    public static final String TEST_SHARE=CONST_HOST1+"/sys/Mobileshare/sendShare/no/";

    public static final String TEST_HISTORY_DATA=CONST_HOST1+"/sys/myDoc/docHandle/id/";

    //首页banner，秒签app图标的获取 2.25
    public static final String IMAGE="http://mqsd.oss-cn-beijing.aliyuncs.com/";

   /**我的签收状态**/
    ///sys/mydoc/documentManage/state/receiveUnsign
    public static final String WITE_ME_RECEIVER=CONST_HOST1+"/sys/mydoc/documentManage/state/receiveUnsign";
    ///sys/mydoc/documentManage/state/sendUnsign
    public static final String WITE_HE_RECEIVER=CONST_HOST1+"/sys/mydoc/documentManage/state/sendUnsign";

    ///sys/mydoc/documentManage/state/toPay
    public static final String WITE_TO_PAY=CONST_HOST1+"/sys/mydoc/documentManage/state/toPay";
    /***我的签收状态***/

    //2018.5.21再次改版的工作台
    public static final String NEW_HOME_PAGE=CONST_HOST+"/v2/homeIndex/getHomeData";


    //新版工作台的登录接口  TODO: 2018/6/7
    public static final String NEW_LOGIN=CONST_HOST+"/v2/homeIndex/login";

    public static final String NEW_SEND_CODE=CONST_HOST+"/v1/user/sendCode";


}
