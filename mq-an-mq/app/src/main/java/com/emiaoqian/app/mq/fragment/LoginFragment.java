package com.emiaoqian.app.mq.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.emiaoqian.app.mq.Interface.ChangeStateBarCallbcak;
import com.emiaoqian.app.mq.Interface.LoginCallBack;
import com.emiaoqian.app.mq.Interface.MyCallback;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.utils.Constants;
import com.emiaoqian.app.mq.utils.EncodeBuilder;
import com.emiaoqian.app.mq.utils.GsonUtil;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.utils.Netutils;
import com.emiaoqian.app.mq.utils.StatebarUtils2;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.emiaoqian.app.mq.utils.httphelper;
import com.emiaoqian.app.mq.utils.sharepreferenceUtils;
import com.emiaoqian.app.mq.view.XiongProgressDialog;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by xiong on 2018/4/22.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private ImageView im1;
    private EditText phonenum;
    private TextView send_code;
    private View v1;
    private ImageView im2;
    private EditText passwordnum;
    private View v2;
    private TextView tv;
    private TextView tvnote;
    private TextView nexttv;
    private ImageView im3;
    private LinearLayout changeloginstate;
    private StatebarUtils2 statebar;

    String login_type=null;

    private static final int UN_CHECK = 500;
    private static final int CAN_CHECK = 600;
    private int time = 60;


    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UN_CHECK:
                    time--;
                    send_code.setText("请稍后" + time + "s");
                    send_code.setTextColor(Color.GRAY);
                    send_code.setBackgroundResource(R.drawable.shape_send_code_press);
                    break;
                case CAN_CHECK:
                    //下面第一个判断防止崩溃 5.13
                    if (LoginFragment.this.isAdded()) {
                        time = 60;
                        send_code.setTextColor(getResources().getColor(R.color.button_color));
                        send_code.setBackgroundResource(R.drawable.shape_send_code);
                        send_code.setText("重新获取");
//                    //把按钮状态设置为按下
//                    send_code.setPressed(false);
//                    //最开始这个是不可点击状态
                        send_code.setEnabled(true);
                    }
                    break;
            }
        }
    };
    private ImageView returnIm;
    private TextView change_login_state_tv;
    private RelativeLayout returnIm_rl;
    private TextView agree_note;
    private TextView agree_note1;
    private RelativeLayout title_rl;


    public static LoginFragment newInstance(String fragmentname){
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nfragment",fragmentname);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止输入框上移底部布局 4.25
        getActivity().getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    //登录成功之后刷新页面（首页里面的刷新页面）
    public static MyCallback.RefreashHomePage refreashHomePage;
    public static void setrefreashcallback(MyCallback.RefreashHomePage refreashHomePage){
        LoginFragment.refreashHomePage=refreashHomePage;

    }

    //我的页面里面的登录 6.21
    public static MyCallback.RefreashMePage refreashMePage;
    public static void setRefreashMePageListener(MyCallback.RefreashMePage refreashMePage){
        LoginFragment.refreashMePage=refreashMePage;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // SecondActivity.getChildFragmentWebviewCallback(this);

    }

    @Override
    public int getlayout() {
        return R.layout.password_word_view;
    }

    @Override
    public void initialize() {
        statebar = new StatebarUtils2(this);
        if (Build.VERSION.SDK_INT<21){
            title_rl = (RelativeLayout) view.findViewById(R.id.title);
            lessApi19NeedStatarbarHeight(title_rl);
        }
        agree_note1 = (TextView) view.findViewById(R.id.agree_note1);
        agree_note = (TextView) view.findViewById(R.id.agree_note);
        agree_note.setOnClickListener(this);
        agree_note1.setOnClickListener(this);
        returnIm_rl = (RelativeLayout) view.findViewById(R.id.returnIm_rl);
        change_login_state_tv = (TextView) view.findViewById(R.id.change_login_state_tv);
        //im1 = (ImageView) findViewById(R.id.im1);
        phonenum = (EditText) view.findViewById(R.id.phone_num);
        send_code = (TextView) view.findViewById(R.id.send_code);
        // v1 = (View) findViewById(R.id.v1);
        //这个是输入密码获取输入验证码的图标 4.22
        im2 = (ImageView) view.findViewById(R.id.im2);
        //输入验证码或者输入密码 4.22
        passwordnum = (EditText)view.findViewById(R.id.password_num);
        passwordnum.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //v2 = (View) findViewById(R.id.v2);
        //这个是提示，输验证码才有
        tv = (TextView)view.findViewById(R.id.tv);
        //点击获取语音验证码
        tvnote = (TextView) view.findViewById(R.id.tv_note);
        // 下一步
        nexttv = (TextView) view.findViewById(R.id.next_tv);

        //最下面验证码和密码登录的切换
        im3 = (ImageView) view.findViewById(R.id.im3);
        //验证码和密码登录的切换
        changeloginstate = (LinearLayout) view.findViewById(R.id.change_login_state);

        returnIm = (ImageView) view.findViewById(R.id.returnIm);
        returnIm.setOnClickListener(this);

        changeloginstate.setOnClickListener(this);
        nexttv.setOnClickListener(this);
        tvnote.setOnClickListener(this);
        send_code.setOnClickListener(this);
        returnIm_rl.setOnClickListener(this);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LogUtil.e("1.666"+"loginfragment");
//                Mainactivity.getChildFragmentWebviewCallback(LoginFragment.this);
//            }
//        },300 );
        Mainactivity.getChildFragmentWebviewCallback(LoginFragment.this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点这个改图标，改提示
            case R.id.change_login_state:
                if (change_login_state_tv.getText().equals("验证码登录")){
                    passwordnum.setText("");
                    change_login_state_tv.setText("密码登录");
                    im3.setImageResource(R.drawable.loginpassim_p);
                    im2.setImageResource(R.drawable.loginsignim);
                    passwordnum.setHint("请输入验证码");
                    passwordnum.setInputType(InputType.TYPE_NULL|InputType.TYPE_CLASS_TEXT);
                    send_code.setVisibility(View.VISIBLE);
                    //暂时是没有这个接口的 4.28
//                    tv.setVisibility(View.VISIBLE);
//                    tvnote.setVisibility(View.VISIBLE);
                }else if (change_login_state_tv.getText().equals("密码登录")){
                    passwordnum.setText("");
                    passwordnum.setHint("请输入密码");
                    passwordnum.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    send_code.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    tvnote.setVisibility(View.GONE);
                    change_login_state_tv.setText("验证码登录");
                    im3.setImageResource(R.drawable.loginsignim_p);
                    im2.setImageResource(R.drawable.loginpassim);
                }
                break;

            //点这个登录
            case R.id.next_tv:

                if (change_login_state_tv.getText().equals("验证码登录")){
                    login_type="password";
                }else if (change_login_state_tv.getText().equals("密码登录")){
                    login_type="code";
                }

                //首先是判断网络 5.15
                boolean networkAvalible = Netutils.isNetworkAvalible(getActivity());
                if (!networkAvalible){
                    ToastUtil.showToastCenter("当前无网络");
                    Netutils.checkNetwork(getActivity());
                    return;
                }

                final String phone_num = phonenum.getText().toString().trim();
                String passwor_num = passwordnum.getText().toString().trim();

                long l = System.currentTimeMillis();
                String str = String.valueOf(l);

                // String phone = phonenum.getText().toString().trim();
                //正则的匹配
                String num="[1][234567890]\\d{9}";//注意正则表达式这里是

                //首先输入了11位就能正常显示，然后再正则()

                if (TextUtils.isEmpty(phone_num)) {
                    ToastUtil.showToastCenter("号码不能为空");
                    return;
                }

                if (phone_num.matches(num)==false){
                    ToastUtil.showToastCenter("输入号码不正确");
                    return;

                }

                HashMap<String,String> datas=new HashMap<>();
                PushAgent mPushAgent = PushAgent.getInstance(getActivity());
                String device_no = mPushAgent.getRegistrationId();
                datas.put("device_no","1&&"+device_no);
                datas.put("login_type",login_type);
                datas.put("mobile",phone_num);
                datas.put("password",passwor_num);
                datas.put("appv","v1");
                datas.put("timestamp",str);
                String s1 = EncodeBuilder.javaToJSONNewPager1(datas);
                String sign1 = EncodeBuilder.newString3(s1);
                datas.put("sign",sign1);
                final Dialog mDialog = XiongProgressDialog.createLoadingDialog(getActivity(), "登陆中");
                mDialog.show();//显示
//                httphelper.create().NewdataHomePage2(Constants.LOGIN, datas, new httphelper.httpcallback() {
                httphelper.create().NewdataHomePage2(Constants.NEW_LOGIN, datas, new httphelper.httpcallback() {
                    @Override
                    public void success(String s) {
                        LogUtil.e(s);
                        // mDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            String code = jsonObject.getString("code");
                            if (code.equals("100000")){
                                String data = jsonObject.getString("data");
                                NewHomeBean1 newHomeBean1 = GsonUtil.parseJsonToBean(data, NewHomeBean1.class);
                                sharepreferenceUtils.saveStringdata(MyApplication.mcontext,"phone_num",newHomeBean1.mobile);
                                sharepreferenceUtils.saveStringdata(MyApplication.mcontext,"user_id",newHomeBean1.id);
                                //当新用户首次进来的时候不知道有没有这个值。
                                if (newHomeBean1.realname!=null) {
                                    sharepreferenceUtils.saveStringdata(MyApplication.mcontext, "realname", newHomeBean1.realname);
                                }
                                sharepreferenceUtils.saveBooleandata(MyApplication.mcontext,"isfirst",true);
                                handler.removeCallbacksAndMessages(null);
                                mDialog.dismiss();
                                refreashHomePage.RefreashHomepagecallback();
                                if (refreashMePage!=null){
                                    refreashMePage.RefreashMepagecallback();
                                    LogUtil.e("当前接口是谁的111--"+refreashMePage.getClass().getName());
                                }

                                getFragmentManager().popBackStack();
                            }
                            else if (code.equals("200006")){
                                ToastUtil.showToast("请先用短信注册并登陆");
                                mDialog.dismiss();
                            }
                            else if (code.equals("200008")){
                                ToastUtil.showToast("用户密码错误");
                                mDialog.dismiss();
                            }
                            else {
                                ToastUtil.showToast("登陆失败");
                                mDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showToast("登陆失败");
                            mDialog.dismiss();
                        }

                    }




                    @Override
                    public void fail(Exception e) {
                        ToastUtil.showToastCenter("网络异常");
                        mDialog.dismiss();

                    }
                });


                break;

            //点这个获取语音验证码
            case R.id.tv_note:

                break;


            case R.id.send_code:
                LogUtil.e("--点击了么-");
                Getsignnum();

                break;

            case R.id.returnIm:
            case R.id.returnIm_rl:
                getFragmentManager().popBackStack();
                break;

            case R.id.agree_note:

            case R.id.agree_note1:
                ToWebviewFragment fragment = ToWebviewFragment.newInstance("https://www.emiaoqian.com/terms.html", "12");
                getFragmentManager().
                        beginTransaction().
                        addToBackStack(null).
                        replace(R.id.rlroot,fragment).
                        commit();


                break;

        }
    }

    public static ChangeStateBarCallbcak changeStateBarCallbcak;
    public static void setChangeStateBarListener(ChangeStateBarCallbcak changeStateBarCallbcak){
        LoginFragment.changeStateBarCallbcak=changeStateBarCallbcak;

    }





    //获取验证码(和快递加获取短信验证码相同 6.8)
    public void Getsignnum() {
        //判断为不为空，手机号是不是正确的。。以后再考虑
        //需求是当手机号码没有填满的时候，那个发短信的按钮是按不出来的
        String phone = phonenum.getText().toString().trim();
        //正则的匹配
        String num="[1][234567890]\\d{9}";//注意正则表达式这里是

        //首先输入了11位就能正常显示，然后再正则()

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastCenter("号码不能为空");
            return;

        } else if (phone.matches(num)==false){
            ToastUtil.showToastCenter("输入号码不正确");
            return;

        }else {
            //首先显示验证码按钮
            HashMap<String,String> datas=new HashMap<>();
            long l = System.currentTimeMillis();
            String str = String.valueOf(l);
            datas.put("timestamp",str);
            datas.put("appv","v1");
            datas.put("mobile",phone);
            String s = EncodeBuilder.javaToJSONNewPager1(datas);
            String sign = EncodeBuilder.newString3(s);
            datas.put("sign",sign);
            httphelper.create().NewdataHomePage2(Constants.NEW_SEND_CODE, datas, new httphelper.httpcallback() {
                @Override
                public void success(String s) {

                    LogUtil.e("--发送短信--"+s);
                    NewHomeBean1 userbean = GsonUtil.parseJsonToBean(s, NewHomeBean1.class);
                    if (!userbean.code.equals("100000")){
                        ToastUtil.showToastCenter("短信验证码发送失败");
                        return;
                    }
                    if (userbean.code.equals("100000")) {
                        //设置倒计时
                        send_code.setBackgroundResource(R.drawable.shape_send_code_press);
                        send_code.setEnabled(false);
                        new Thread() {
                            @Override
                            public void run() {
                                while (time > 1) {
                                    handler.sendEmptyMessage(UN_CHECK);
                                    SystemClock.sleep(1000);
                                }
                                handler.sendEmptyMessage(CAN_CHECK);
                            }
                        }.start();

                    }

                }

                @Override
                public void fail(Exception e) {

                    LogUtil.e("--233404--"+e.toString());


                }
            });



        }
    }

    public static LoginCallBack loginCallBack;

    public static void setLoginCallBackListener(LoginCallBack loginCallBack){
        LoginFragment.loginCallBack=loginCallBack;

    }

    public static MyCallback.RefreashMessage refreashMessage;
    public static void setRefreashMessageListener(MyCallback.RefreashMessage refreashMessage){
        LoginFragment.refreashMessage=refreashMessage;
    }

    @Override
    public void onDestroy() {
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
//        if (getArguments()!=null){
//            String nfragment = getArguments().getString("nfragment");
//            //两种不同的状态，分别直接new的，以及用newstance出来的，直接new出来的直接弹栈就行，然后newstance出来的需要判断，就是下面的
//            if (!nfragment.equals("WorkFragment2")&&!user_id.matches("[0-9]+")){
//
//
//                loginCallBack.logincallback();
////                handler.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        getFragmentManager().popBackStack();
////                    }
////                },200);
//
//            }else if (!nfragment.equals("WorkFragment2")&&user_id.matches("[0-9]+")){
//                    if (refreashMessage!=null){
//                        refreashMessage.RefreashMessage();
//                    }
//            }
//
//        }
        //刷新原来的消息页面，从未登录无数据时候状态到已登录有数据时候的状态 6.25
        if (refreashMessage!=null&&user_id.matches("[0-9]+")){
            refreashMessage.RefreashMessage();
        }

        //下面这个是返回首页时候的沉浸式状态栏
        if (changeStateBarCallbcak!=null) {
            changeStateBarCallbcak.changestatebarcallbcak();
        }
        handler.sendEmptyMessage(UN_CHECK);
        handler.removeMessages(CAN_CHECK);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
