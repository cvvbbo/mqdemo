package com.emiaoqian.app.mq.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.adapter.AddMoreSmallAdapter;
import com.emiaoqian.app.mq.adapter.AddMorebigAdapter;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.bean.NewHomeBean1;
import com.emiaoqian.app.mq.bean.Newsbean;
import com.emiaoqian.app.mq.fragment.LoginFragment;
import com.emiaoqian.app.mq.fragment.ToWebviewFragment;
import com.emiaoqian.app.mq.fragment.WorkFragment2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.emiaoqian.app.mq.activity.Mainactivity.addbutton;
import static com.emiaoqian.app.mq.activity.Mainactivity.addbuttontitle;
import static com.emiaoqian.app.mq.activity.Mainactivity.addbuttonurl;


public class PopupMenuUtil {


    /***
     *
     *
     * 它这个动画是先超出再回弹一点回来 1.25
     *
     *
     *
     */

    private static final String TAG = "PopupMenuUtil";
    private GridView pop_grid;
    private ImageView im;
    private RecyclerView small_ry;
    private RecyclerView bg_ry;
    private AddMoreSmallAdapter smallAdapter;
    private AddMorebigAdapter bigadapter;


    public static PopupMenuUtil getInstance() {


        return MenuUtilHolder.INSTANCE;
    }

    private static class MenuUtilHolder {
        public static PopupMenuUtil INSTANCE = new PopupMenuUtil();
    }

    private View rootVew;
    private PopupWindow popupWindow;

    private RelativeLayout rlClick;
    private ImageView ivBtn;
    //private LinearLayout llTest1, llTest2, llTest3, llTest4, llTest5, llTest6, llTest7, llTest8;

    /**
     * 动画执行的 属性值数组
     */
    float animatorProperty[] = null;
    /**
     * 第一排图 距离屏幕底部的距离
     */
    int top = 0;
    /**
     * 第二排图 距离屏幕底部的距离
     */
    int bottom = 0;

    /**
     * 创建 popupWindow 内容
     *
     *
     */
    private void _createView( AppCompatActivity activity) {
        rootVew = LayoutInflater.from(activity).inflate(R.layout.popup_menu, null);
        popupWindow = new PopupWindow(rootVew,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //设置为失去焦点 方便监听返回键的监听
        popupWindow.setFocusable(false);

        // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);


        if (animatorProperty == null) {
            top = dip2px(activity, 210);
            //top=210;
            bottom = dip2px(activity, 210);
            Log.e("--距离底部的距离是--",bottom+"");
            animatorProperty = new float[]{bottom, 60, -30, -20 - 10, 0};
        }

        initLayout(activity);
    }

    /**
     * dp转化为px
     *
     * @param context  context
     * @param dipValue dp value
     * @return 转换之后的px值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 初始化 view
     */
    private void initLayout(final AppCompatActivity activity) {
       // rlClick = (RelativeLayout) rootVew.findViewById(R.id.pop_rl_click);
        ivBtn = (ImageView) rootVew.findViewById(R.id.pop_iv_img);

//        pop_grid = (GridView) rootVew.findViewById(R.id.pop_gird);
//        pop_grid.setAdapter(new PopGridview());

        bg_ry = (RecyclerView) rootVew.findViewById(R.id.bg_ry);
        //gridlayoutmanger会自己预留位置，如果设置了一行显示多少个
        //GridLayoutManager bglayoutManager=new GridLayoutManager(rootVew.getContext(),2);
        LinearLayoutManager layoutManager=new LinearLayoutManager(rootVew.getContext(),LinearLayout.HORIZONTAL,false);
        bg_ry.setLayoutManager(layoutManager);
        bigadapter = new AddMorebigAdapter(activity,WorkFragment2.bigdatas);
        bg_ry.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                DisplayMetrics metrics = new DisplayMetrics();
                Display display = activity.getWindowManager().getDefaultDisplay();
                display.getMetrics(metrics);
                    //item是第一个的时候不设置间距
                if (parent.getChildAdapterPosition(view) == 1) {
                    outRect.left =activity.getResources().getDimensionPixelOffset(R.dimen.x46);
                }

            }
        });
        bg_ry.setAdapter(bigadapter);
        bigadapter.setOnItemClickListener(new AddMorebigAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ(activity);
                }else {
                    ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(bigadapter.getItemString(position),
                            "nostatabar");
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.rlroot, toWebviewFragment)
                            .addToBackStack(null)
                            .commit();
                }

                popupWindow.dismiss();
            }
        });

        //这个是在下面的集合。（小图标）
        small_ry = (RecyclerView) rootVew.findViewById(R.id.small_ry);
        GridLayoutManager smalllayoutManager = new GridLayoutManager(rootVew.getContext(), 4);
        small_ry.setLayoutManager(smalllayoutManager);
        smallAdapter = new AddMoreSmallAdapter(activity,WorkFragment2.smalldatas);
        small_ry.setAdapter(smallAdapter);
        smallAdapter.setOnItemClickListener(new AddMoreSmallAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (!user_id.matches("[0-9]+")) {
                    lognMQ(activity);
                }else {
                    ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(smallAdapter.getItemString(position),
                            "nostatabar");
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.rlroot, toWebviewFragment)
                            .addToBackStack(null)
                            .commit();
                }

                popupWindow.dismiss();
            }
        });


        //这个是最下面的图标的点击事件，就是那个突出的按钮 1.25
       // rlClick.setOnClickListener(new MViewClick(0, activity));
        ivBtn.setOnClickListener(new MViewClick(0, activity));


//        pop_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String url = addbuttonurl.get(position);
//                ToWebviewFragment toWebviewFragment = ToWebviewFragment.newInstance(url,addbuttontitle.get(position),"nostatabar");
//                activity.getSupportFragmentManager().beginTransaction()
//                        .add(R.id.rlroot,toWebviewFragment)
//                        .addToBackStack(null)
//                        .commit();
//                popupWindow.dismiss();
//
//
//            }
//        });


    }

    private void lognMQ(AppCompatActivity activity) {
        activity.getSupportFragmentManager().
                beginTransaction().
                addToBackStack(null).
                replace(R.id.rlroot, new LoginFragment()).
                commit();
    }

    class  PopGridview extends BaseAdapter{

        @Override
        public int getCount() {
            return addbutton.size();
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
            //正常的添加布局直接null就行了，但是如果里面要二次嵌套的话，里面的第三个参数就不能为null
            //如果要为null也行，在子布局里面要设置最小的宽高
            View view = View.inflate(parent.getContext(), R.layout.grid_app_view, null);
            //第二个参数是用来复用的，这里不用，因为子元素少

            im = (ImageView) view.findViewById(R.id.iv);

            //这里使用glide会报错，因为有张图片的解码glide好像不支持，用picasso就没事2.8
            //Glide.with(MyApplication.mcontext).load(addbutton.get(position)).into(im);
            Picasso.with(MyApplication.mcontext).load(addbutton.get(position)).into(im);
            return view;
        }
    }

    /**
     * 点击事件
     */
    private class MViewClick implements View.OnClickListener {

        public int index;
        public Context context;

        public MViewClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (index == 0) {
                //加号按钮点击之后的执行
                _rlClickAction();
            } else {
                showToast(context, "index=" + index);
            }
        }
    }

    Toast toast = null;

    /**
     * 防止toast 多次被创建
     *
     * @param context context
     * @param str     str
     */
    private void showToast(Context context, String str) {
        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
        toast.show();
    }

    /**
     * 刚打开popupWindow 执行的动画
     */
    private void _openPopupWindowAction() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 0f, 135f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        //下面是里面8个元素的进场时候的动画 （这里暂时叫进场（严格说进场是5.0的进场动画））
//        _startAnimation(llTest1, 500, animatorProperty);
//        _startAnimation(llTest2, 430, animatorProperty);
//        _startAnimation(llTest3, 430, animatorProperty);
//        _startAnimation(llTest4, 500, animatorProperty);
//
//        _startAnimation(llTest5, 500, animatorProperty);
//        _startAnimation(llTest6, 430, animatorProperty);
//        _startAnimation(llTest7, 430, animatorProperty);
//        _startAnimation(llTest8, 500, animatorProperty);


        //// TODO: 2018/5/28
        // _startAnimation(pop_grid,500,animatorProperty);
        _startAnimation(bg_ry,500,animatorProperty);
        _startAnimation(small_ry,500,animatorProperty);
    }


    /**
     * 关闭 popupWindow执行的动画
     */
    public void _rlClickAction() {
        if (ivBtn != null ) {
       // if (ivBtn != null && rlClick != null) {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

//            _closeAnimation(llTest1, 300, top);
//            _closeAnimation(llTest2, 200, top);
//            _closeAnimation(llTest3, 200, top);
//            _closeAnimation(llTest4, 300, top);
//            _closeAnimation(llTest5, 300, bottom);
//            _closeAnimation(llTest6, 200, bottom);
//            _closeAnimation(llTest7, 200, bottom);
//            _closeAnimation(llTest8, 300, bottom);

            //// TODO: 2018/5/28
            //_closeAnimation(pop_grid,300,top);
            _closeAnimation(bg_ry,300,top);
            _closeAnimation(small_ry,300,top);

//            rlClick.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    _close();
//                }
//            }, 300);
            ivBtn.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _close();
                }
            }, 300);

        }
    }


    /**
     * 弹起 popupWindow
     *
     *
     * @param parent  parent
     */
    public void _show(AppCompatActivity activity, View parent) {
        _createView(activity);
        // 网络连接错误时候的，或者是获取不到数据时候显示这个2.24
//        if (popupWindow != null && !popupWindow.isShowing()&&addbutton.size()!=0) {
//            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
//            _openPopupWindowAction();
//
//        }
//        // // TODO: 2018/5/28
//        else if (addbutton.size()==0){
//            ToastUtil.showToastCenter("网络连接异常");
//        }

        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = activity.getWindow().getDecorView().getHeight();
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, winHeight-rect.bottom);
        //原版的popupwindows显示的位置 // TODO: 2018/6/28
        //popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        _openPopupWindowAction();
    }

    /**
     * 关闭popupWindow
     */

    public void _close() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * @return popupWindow 是否显示了
     */
    public boolean _isShowing() {
        if (popupWindow == null) {
            return false;
        } else {
            return popupWindow.isShowing();
        }
    }

    /**
     * 关闭 popupWindow 时的动画
     *
     * @param view     mView
     * @param duration 动画执行时长
     * @param next     平移量
     */
    private void _closeAnimation(View view, int duration, int next) {
        //这个0就是从它固定的位置，向下收起，所以就不用
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, next);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 启动动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void _startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }


}
