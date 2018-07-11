package com.emiaoqian.app.mq.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.app.mq.Interface.ChangeStateBarCallbcak;
import com.emiaoqian.app.mq.Interface.Logoutcallback;
import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.adapter.Meadapter;
import com.emiaoqian.app.mq.adapter.MeadapterMore;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.bean.Newsbean;
import com.emiaoqian.app.mq.receiver.NetWorkStateReceiver;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.MakephotoUtils;
import com.emiaoqian.app.mq.utils.StatebarUtils2;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.emiaoqian.app.mq.view.CircleImageView;
import com.emiaoqian.app.mq.view.MydialogOnbutton1;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiong on 2018/6/5.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener,
                                                        ChangeStateBarCallbcak,
                                                        MyCallback.RefreashMePage,
        Logoutcallback{

        //
    private RecyclerView top_ry;
    private StatebarUtils2 statebar;
    private boolean isshowstatus;
    private Handler handler=new Handler();
    private ArrayList<NewHomeBean1> medatas=new ArrayList<>();
    private ArrayList<NewHomeBean1> seconddatas=new ArrayList<>();
    private ArrayList<NewHomeBean1> middledatas=new ArrayList<>();
    private ArrayList<NewHomeBean1> buttomdatas=new ArrayList<>();
    private Meadapter meadapter;
    private CircleImageView headim;
    private MydialogOnbutton1 mydialog3;

    //从相册中得到的bitmap图片
    private Bitmap albumbitmap_frontId;
    private Bitmap makephotobitmap_frontId;
    private RecyclerView buttom_ry1;
    private MeadapterMore meadapterMore;
    private MeadapterMore meadapterMore1;
    private MeadapterMore meadapterMore2;
    private RecyclerView buttom_ry2;
    private RecyclerView buttom_ry3;
    private TextView login_tv;
    private TextView username;
    private ImageView setting_im;

    NetWorkStateReceiver netWorkStateReceiver;
    private String title1;
    private String title2;
    private String title3;
    private boolean isfirst=true;

    @Override
    public int getlayout() {
        return R.layout.me_fragment_view;
    }


    @Override
    public void onHiddenChanged( boolean hidden) {
        //显示被切换的fragment，然后才是这个！
        statebar.setshowstatebar(hidden);
        isshowstatus=hidden;
        //statebar.fullScreen(getActivity());
        if (hidden){
            LoginFragment.setChangeStateBarListener(null);
            ToWebviewFragment.setChangeStateBarListener(null);
        }else {
            LoginFragment.setChangeStateBarListener(this);
            ToWebviewFragment.setChangeStateBarListener(this);
        }

        //判断登录
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        if (!user_id.matches("[0-9]+")) {
            username.setVisibility(View.GONE);
            login_tv.setVisibility(View.VISIBLE);
        }else {
            login_tv.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
            String realname = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "realname", "");
            if (!realname.equals("")){
                username.setText(realname);
            }

        }
    }


    public  int dp2px( float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }




    @Override
    public void initialize() {
        headim = (CircleImageView) view.findViewById(R.id.headIcon);
        setting_im = (ImageView) view.findViewById(R.id.setting_im);
        headim.setOnClickListener(this);
        //沉浸式状态栏 6.7
        statebar = new StatebarUtils2(this);
        statebar.fullScreen(getActivity());
        //登录按钮
        login_tv = (TextView) view.findViewById(R.id.login_tv);
        username = (TextView) view.findViewById(R.id.username);
        login_tv.setOnClickListener(this);
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");

        String realname = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "realname", "");

        if (!user_id.matches("[0-9]+")) {
            username.setVisibility(View.GONE);
            login_tv.setVisibility(View.VISIBLE);
        }else {
            login_tv.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
            username.setText(realname);

        }


        top_ry = (RecyclerView) view.findViewById(R.id.top_ry);
        //第三个参数表示是否反转
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        top_ry.setLayoutManager(layoutManager);
        top_ry.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                DisplayMetrics metrics = new DisplayMetrics();
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                display.getMetrics(metrics);
                LogUtil.e("手机", "display is" + metrics.widthPixels + "--" + metrics.heightPixels);

                if (parent.getChildAdapterPosition(view) == 0) {
                    //outRect.left =getResources().getDimensionPixelOffset(R.dimen.x60);
                    outRect.left=dp2px(60);
                }else {
                    //outRect.left=getResources().getDimensionPixelOffset(R.dimen.x67);
                    outRect.left=dp2px(83);
                }
            }
        });


        /***
         *
         * recyclerview里面如何嵌套recyclerview？？
         *
         */
        buttom_ry2 = (RecyclerView) view.findViewById(R.id.buttom_ry2);
        buttom_ry3 = (RecyclerView) view.findViewById(R.id.buttom_ry3);
        buttom_ry1 = (RecyclerView) view.findViewById(R.id.buttom_ry1);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 4);
        GridLayoutManager manager2 = new GridLayoutManager(getActivity(), 4);
        buttom_ry1.setLayoutManager(manager);
        buttom_ry2.setLayoutManager(manager1);
        buttom_ry3.setLayoutManager(manager2);
        GetNetdatas();


        //改变状态栏  （我的页面这里有坑，因为没有延迟 6.21）
        LoginFragment.setChangeStateBarListener(this);

        LoginFragment.setRefreashMePageListener(this);

        ToWebviewFragment.setChangeStateBarListener(this);

        BaseWebFragment.setLogoutcallbacklistener(this);



    }

    public void GetNetdatas(){
        HashMap<String,String> datas=new HashMap<>();
        long l = System.currentTimeMillis();
        String time = String.valueOf(l);
        datas.put("appv","v2");
        datas.put("timestamp",time);
        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString3(s);
        datas.put("sign",sign);
        httphelper.create().NewdataHomePage2(Constants.CONST_HOST+"/v2/homeIndex/getMyMenu", datas,
                new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("100000")) {

                        String data = jsonObject.getString("data");
                        JSONObject datas = new JSONObject(data);
                        //设置按钮的点击地址
                        final String setting_url = datas.getString("setting_url");
                        //头像按钮的点击地址
                        final String headimg_url = datas.getString("headimg_url");
                        String headimg = datas.getString("headimg");
                        //设置按钮点击

                        setting_im.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                                if (!user_id.matches("[0-9]+")) {
                                    lognMQ();
                                }else {
                                    ToWebviewFragment fragment = ToWebviewFragment.newInstance(setting_url, "nostatabar");
                                    getFragmentManager().
                                            beginTransaction().
                                            addToBackStack(null).
                                            replace(R.id.rlroot, fragment)
                                            .commit();
                                }
                            }
                        });
                        headim.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                                if (!user_id.matches("[0-9]+")) {
                                    lognMQ();
                                }else {
                                    ToWebviewFragment fragment = ToWebviewFragment.newInstance(headimg_url, "nostatabar");
                                    getFragmentManager().
                                            beginTransaction().
                                            addToBackStack(null).
                                            replace(R.id.rlroot, fragment)
                                            .commit();
                                }
                            }
                        });
                        //获取头像按钮
                        if (!headimg.equals("")){
                            Picasso.with(getActivity()).load(headimg).into(headim);
                        }
                        String actiondata = datas.getString("action");

                        ArrayList<Newsbean> topdatas=(ArrayList<Newsbean>)GsonUtil.parseJsonToList(actiondata,
                                new TypeToken<List<Newsbean>>(){}.getType());
                        LogUtil.e(topdatas+"");

                        //初始化头像下面的
                        medatas.clear();
                        medatas.addAll( topdatas.get(0).data);


                        //第二部分
                        seconddatas.clear();
                        seconddatas.addAll(topdatas.get(1).data);
                        title1=topdatas.get(1).title;


                        //中间
                        middledatas.clear();
                        middledatas.addAll(topdatas.get(2).data);
                        title2=topdatas.get(2).title;

                        //尾部
                        buttomdatas.clear();
                        buttomdatas.addAll(topdatas.get(3).data);
                        title3=topdatas.get(3).title;


                        //全部数据成功了才会走到这里，
                        if (isfirst) {
                            initadapter();
                            isfirst=false;
                        }

                    }else {
                        ToastUtil.showToastCenter("后台数据错误，正在紧急修复中");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showToastCenter("后台数据错误，正在紧急修复中");

                }


            }

            @Override
            public void fail(Exception e) {
                if (netWorkStateReceiver == null) {
                    netWorkStateReceiver = new NetWorkStateReceiver();
                    netWorkStateReceiver.setRefreashMePageListener(MeFragment.this);
                }
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                getActivity().registerReceiver(netWorkStateReceiver, filter);

            }
        });
    }



    private void notifyadapter(){
        meadapter.notifyDataSetChanged();
        meadapterMore.notifyDataSetChanged();
        meadapterMore1.notifyDataSetChanged();

    }

    //刷新数据适配器
    private void initadapter(){
        meadapter = new Meadapter(medatas,getActivity());
        top_ry.setAdapter(meadapter);
        //第一排
        meadapter.setOnItemClickListener(new Meadapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // ToastUtil.showToastCenter("当前-"+meadapter.getItemString(position));
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ();
                }else {
                    ToWebviewFragment(meadapter.getItemString(position));
                }

            }
        });
        //================第二排
        meadapterMore = new MeadapterMore(seconddatas,getActivity(),title1);
        buttom_ry1.setAdapter(meadapterMore);
        meadapterMore.setOnItemClickListener(new MeadapterMore.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // ToastUtil.showToastCenter("当前-"+meadapterMore.getItemString(position));
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ();
                }else {
                    ToWebviewFragment(meadapterMore.getItemString(position));
                }

            }
        });
        //================中间的
        meadapterMore1 = new MeadapterMore(middledatas,getActivity(),title2);
        buttom_ry2.setAdapter(meadapterMore1);
        meadapterMore1.setOnItemClickListener(new MeadapterMore.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // ToastUtil.showToastCenter("当前-"+meadapterMore1.getItemString(position));
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ();
                }else {
                    ToWebviewFragment(meadapterMore1.getItemString(position));
                }

            }
        });
        //================最下面的
        meadapterMore2 = new MeadapterMore(buttomdatas,getActivity(),title3);
        buttom_ry3.setAdapter(meadapterMore2);
        meadapterMore2.setOnItemClickListener(new MeadapterMore.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //  ToastUtil.showToastCenter("当前-"+meadapterMore2.getItemString(position));
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ();
                }else {
                    ToWebviewFragment(meadapterMore2.getItemString(position));
                }

            }
        });
    }

    private void ToWebviewFragment(String url){

        ToWebviewFragment fragment = ToWebviewFragment.newInstance(url, "nostatabar");
        getFragmentManager().
                beginTransaction().
                addToBackStack(null).
                replace(R.id.rlroot, fragment)
                .commit();
    }


    private void lognMQ() {
        getFragmentManager().
                beginTransaction().
                addToBackStack(null).
                replace(R.id.rlroot, new LoginFragment()).
                commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.headIcon:
//                LogUtil.e("--被点击了么--");
//                mydialog3 = new MydialogOnbutton1(getActivity(), this);
//                mydialog3.show();

                break;

            case R.id.login_tv:
                lognMQ();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=Activity.RESULT_OK){
            return;
        }else if (resultCode==Activity.RESULT_OK) {
                switch (requestCode) {
                    case 100:
                        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
                            makephotobitmap_frontId = MakephotoUtils.compressImage(mActivity, new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                    + "/emiaoqian/pictures/" + "image.jpg").getPath());
                            MakephotoUtils.saveImage(getActivity(), makephotobitmap_frontId, "head_icon.png");
                            headim.setImageBitmap(makephotobitmap_frontId);
                        }else {
                            makephotobitmap_frontId = MakephotoUtils.compressImage(mActivity,
                                    new File(Environment.getExternalStorageDirectory(), "image.jpg").getPath());
                            MakephotoUtils.saveImage(getActivity(), makephotobitmap_frontId, "head_icon.png");
                            headim.setImageBitmap(makephotobitmap_frontId);
                        }

                        break;

                    case 102:
                        ContentResolver resolver = getActivity().getContentResolver();
                        //照片的原始资源地址，这样获取并不是压缩过的，如果拍照的图片也是这样获取就是压缩过的！！
                        Uri originalUri = data.getData();
                        try {
                            albumbitmap_frontId = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                            headim.setImageBitmap(albumbitmap_frontId);
                            new Thread() {
                                @Override
                                public void run() {
                                    // 第一次保存时将系统的保存在自己的文件夹中
                                    File file = MakephotoUtils.saveImageofalbum(getActivity(), albumbitmap_frontId, "head_icon.png");
                                    //最后压缩保存
                                    Bitmap afterbitmap = MakephotoUtils.compressImage(mActivity, file.getPath());

                                    //这个是最终保存的的
                                    MakephotoUtils.saveImage(getActivity(), afterbitmap, "head_icon.png");

                                }
                            }.start();

                        } catch (IOException e) {
                            LogUtil.e(e + "");
                            e.printStackTrace();
                        }

                        break;

                }
            }
        }

    @Override
    public void changestatebarcallbcak() {
        statebar.setshowstatebar(isshowstatus);
    }

    @Override
    public void RefreashMepagecallback() {
        String realname = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "realname", "");
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");

        if (!user_id.matches("[0-9]+")) {
            username.setVisibility(View.GONE);
            login_tv.setVisibility(View.VISIBLE);
        }else {
            login_tv.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
            username.setText(realname);

        }

        //==========防止没有网络请求时候出错
        if (isfirst){
            GetNetdatas();
        }

    }


    @Override
    public void Logoutcallback() {
        //如果不走接口，然后断点发现走到了looper，然后这时候指定让接口里面的方法走线程就行
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                username.setVisibility(View.GONE);
                login_tv.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(netWorkStateReceiver);
        super.onDestroy();
    }
}
