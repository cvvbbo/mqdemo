package com.emiaoqian.app.mq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by xiong on 2018/1/24.
 */

public class Mygridview extends GridView {
    public Mygridview(Context context) {
        super(context);
    }

    public Mygridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Mygridview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
