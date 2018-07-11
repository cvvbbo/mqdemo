package com.emiaoqian.app.mq.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.utils.MakephotoUtils;
import com.emiaoqian.app.mq.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Created by xiong on 2018/7/2.
 */

public class ClearWebCacheDialogFragment extends android.support.v4.app.DialogFragment{


    public static  ClearWebCacheDialogFragment newstance(String cachesize){
        ClearWebCacheDialogFragment fragment=new ClearWebCacheDialogFragment();
        Bundle bundle=new Bundle();
        bundle.putString("cachesize",cachesize);
        fragment.setArguments(bundle);
        return fragment;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.clear_cache_dialog_view,null);
        TextView tv1= (TextView) view.findViewById(R.id.tv1);
        if (getArguments()!=null){
            String cachesize = getArguments().getString("cachesize");
            String s = tv1.getText().toString();
            tv1.setText(s+cachesize+"缓存");
        }
        TextView tv2= (TextView) view.findViewById(R.id.tv2);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
        // return dialog;
    }

    @Override
    public void onResume() {
        //Log.e("66onResume","SigntvDialogFragment");
        super.onResume();
        int width = getActivity().getResources().getDimensionPixelOffset(R.dimen.x150);
        int height = getActivity().getResources().getDimensionPixelOffset(R.dimen.x90);
//        getDialog().getWindow().setLayout(width,height);
        //点击外部不可取消
        // getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        window.setLayout(width,height);
        windowParams.dimAmount = 0.5f;
        window.setAttributes(windowParams);

    }
}
