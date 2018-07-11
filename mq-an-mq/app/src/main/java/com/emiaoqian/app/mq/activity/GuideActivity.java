package com.emiaoqian.app.mq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiong on 2017/9/15.
 */

public class GuideActivity extends AppCompatActivity {
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.llpoint)
    LinearLayout llpoint;
    @BindView(R.id.bt)
    Button bt;
    private ArrayList<ImageView> images;
    private int[] drawables;
    private int prepoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view);
        ButterKnife.bind(this);
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        drawables = new int[]{R.drawable.guide_1, R.drawable.guide_2};
        images = new ArrayList<>();
        for (int i = 0; i < drawables.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(drawables[i]);
            images.add(imageView);
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.point_select);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.x8), getResources().getDimensionPixelSize(R.dimen.x8));
            if (i == 0) {
                point.setEnabled(false);
            } else {
                point.setEnabled(true);
                layoutParams.leftMargin =getResources().getDimensionPixelSize(R.dimen.x12);
            }
            point.setLayoutParams(layoutParams);
            llpoint.addView(point);
        }

        vp.setAdapter(new Myadapter());
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                llpoint.getChildAt(prepoint).setEnabled(true);
                llpoint.getChildAt(position).setEnabled(false);
                prepoint = position;

                //这里也很神奇啊，如果布局控件不设置为gone也是会出错的！！
                //而且不能只写一个判断，两个判断都要写
                if (position == (drawables.length - 1)) {
                    bt.setVisibility(View.VISIBLE);
                } else {
                    bt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //这里还要加个判断，第一次进入才需要添加这个指导界面，之后都不需要添加引导界面！

        //现在先不讲究这么复杂的逻辑，用户不登录还能让进系统，现在是强制用户登录，要不然就退出！！

        //引导界面肯定是第一次使用的，所以强制登录。
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入正式的主界面
                Intent intent=new Intent(GuideActivity.this,Mainactivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    class Myadapter extends PagerAdapter {

        @Override
        public int getCount() {
            return drawables.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = images.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}

