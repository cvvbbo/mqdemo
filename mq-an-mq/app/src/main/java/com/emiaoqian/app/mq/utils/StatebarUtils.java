//package com.emiaoqian.app.mq.utils;
//
//import android.app.Activity;
//import android.graphics.Color;
//import android.os.Build;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewCompat;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.emiaoqian.app.mq.R;
//import com.emiaoqian.app.mq.activity.Mainactivity;
//import com.emiaoqian.app.mq.fragment.BaseFragment;
//
///**
// * Created by xiong on 2018/6/5.
// */
//
//public class StatebarUtils {
//    /****
//     *
//     * 这里的沉浸式状态栏有两种，一种是直接把布局顶上去的，另一种是白底的沉浸式状态栏。
//     *
//     *
//     */
//
//   // public Activity mactivity;
//
//    public Fragment fragment;
//
//    public StatebarUtils(Fragment fragment){
//        this.fragment=fragment;
//
//    }
//
//
//    public  void setshowstatebar(boolean hidden) {
//        String simpleName = fragment.getClass().getSimpleName();
//        //LogUtil.e(simpleName);
//        //((Fragment)fragment.getClass()).isAdded()
//
//        if (Build.VERSION.SDK_INT>=21) {
//            if (!hidden) {
//                fullScreen(fragment.getActivity());
//            }
//            else {
//                if (simpleName.equals("WorkFragment2")||simpleName.equals("MeFragment")){
//                    return;
//                }
//                Window window = fragment.getActivity().getWindow();
//                //取消状态栏透明
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                //添加Flag把状态栏设为可绘制模式
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                //设置状态栏颜色
//                window.setStatusBarColor(fragment.getActivity().getResources().getColor(R.color.white));
//                //设置系统状态栏处于可见状态
//                //把状态栏的字体改为黑色 https://blog.csdn.net/zhangyiminsunshine/article/details/68064926
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                //让view不根据系统窗口来调整自己的布局(精髓！隐藏了状态栏之后布局会自动调整)
//                ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
//                View mChildView = mContentView.getChildAt(0);
//                if (mChildView != null) {
//                    ViewCompat.setFitsSystemWindows(mChildView, false);
//                    ViewCompat.requestApplyInsets(mChildView);
//                }
//            }
//        }
//    }
//
//    //沉浸式状态栏，(把顶部的ui上移到状态栏的位置)
//    public  void fullScreen(Activity activity) {
//        //这个是大于4.x
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //这个大于5.0
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
//                Window window = activity.getWindow();
//                View decorView = window.getDecorView();
//                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
//                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//                decorView.setSystemUiVisibility(option);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.TRANSPARENT);
//                /**
//                 * 这个后来整合的，因为设置沉浸式标题栏之后，android的布局会自动调整，这样设置之后就不会让布局自动调整了。5.9
//                 *
//                 */
//                //导航栏颜色也可以正常设置
//                //    window.setNavigationBarColor(Color.TRANSPARENT);
//                ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
//                View mChildView = mContentView.getChildAt(0);
//                if (mChildView != null) {
//                    ViewCompat.setFitsSystemWindows(mChildView, false);
//                    ViewCompat.requestApplyInsets(mChildView);
//                }
//            }
//
//            /****5.0以下手机不做处理，没有沉浸式状态栏5.11****/
////            else {
////                Window window = activity.getWindow();
////                WindowManager.LayoutParams attributes = window.getAttributes();
////                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
////                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
////                attributes.flags |= flagTranslucentStatus;
////            //    attributes.flags |= flagTranslucentNavigation;
////                window.setAttributes(attributes);
////
////                ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
////                View mChildView = mContentView.getChildAt(0);
////                if (mChildView != null) {
////                    ViewCompat.setFitsSystemWindows(mChildView, false);
////                    ViewCompat.requestApplyInsets(mChildView);
////                }
////
////            }
//
//
//        }
//    }
//
//    //普通的白色状态栏
//    public void whitestatus(Fragment fragment){
//        if (Build.VERSION.SDK_INT>=21){
//            Window window = fragment.getActivity().getWindow();
//            //取消状态栏透明
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //添加Flag把状态栏设为可绘制模式
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            //设置状态栏颜色
//            window.setStatusBarColor(fragment.getActivity().getResources().getColor(R.color.white));
//            //设置系统状态栏处于可见状态
//            //把状态栏的字体改为黑色 https://blog.csdn.net/zhangyiminsunshine/article/details/68064926
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            //让view不根据系统窗口来调整自己的布局
//            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
//            View mChildView = mContentView.getChildAt(0);
//            if (mChildView != null) {
//                ViewCompat.setFitsSystemWindows(mChildView, false);
//                ViewCompat.requestApplyInsets(mChildView);
//            }
//        }
//    }
//
//}
