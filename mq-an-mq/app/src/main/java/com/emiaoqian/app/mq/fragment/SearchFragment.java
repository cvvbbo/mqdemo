package com.emiaoqian.app.mq.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import com.emiaoqian.app.mq.Interface.ChangeStateBarCallbcak;
import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.activity.Mainactivity;
import com.emiaoqian.app.mq.application.MyApplication;
import com.emiaoqian.app.mq.db.RecordSQLiteOpenHelper;
import com.emiaoqian.app.mq.utils.LogUtil;
import com.emiaoqian.app.mq.view.MyListView;



/**
 * Created by xiong on 2018/1/17.
 *
 *
 * http://blog.csdn.net/leoleohan/article/details/50688283
 *
 */

public class SearchFragment extends BaseFragment {

    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(MyApplication.mcontext);
    private SQLiteDatabase db;
    private BaseAdapter adapter;

    private MyListView history;
    private EditText searchview;
    private TextView change_tv;
    private TextView tv_clear;
    private ImageView returnIm;
    private LinearLayout title_bar;

    @Override
    public int getlayout() {
        return R.layout.search_view;
    }

    @Override
    public void initialize() {
        if (Build.VERSION.SDK_INT<21){
            title_bar = (LinearLayout) view.findViewById(R.id.title_bar);
            lessApi19NeedStatarbarHeight(title_bar);
        }

        Mainactivity.getChildFragmentWebviewCallback(this);
        LogUtil.e("233---创建"+this.getClass().getSimpleName());

        history = (MyListView) view.findViewById(R.id.history_list);
        searchview = (EditText) view.findViewById(R.id.searchview);
        change_tv = (TextView) view.findViewById(R.id.change_tv);
        tv_clear = (TextView) view.findViewById(R.id.tv_clear);
        returnIm = (ImageView) view.findViewById(R.id.returnIm);
        //返回按钮的处理2.25
        returnIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //强行关闭软键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);
                getFragmentManager().popBackStack();
            }
        });

        // 清空搜索历史
        // 清空搜索历史
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先删除再查询2.5
                deleteData();
                queryData("");
            }
        });

        // 搜索框的键盘搜索键点击回调
        searchview.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    //把键盘上的回车变成搜索需要xml文件的edtext修改以及java代码一起修改
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                           getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = hasData(searchview.getText().toString().trim());
                    if (!hasData) {
                        insertData(searchview.getText().toString().trim());
                        queryData("");
                        adapter.notifyDataSetChanged();
                    }

                    if (searchview.getText().toString().trim()!=null){

                        HistoryDetailFragment newfragment3 = HistoryDetailFragment.newInstance(searchview.getText().toString().trim());

                        getFragmentManager().beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.rlroot,newfragment3)
                                .commit();
                        searchview.setText("");
                        adapter.notifyDataSetChanged();

                    }


                }
                return false;
            }
        });

        // 搜索框的文本变化实时监听
        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tempName = searchview.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据，只显示自己输入的部分2.5
                queryData(tempName);

            }
        });

        //listview的条目，除了position还能够判断的是view，（这个就是条目里面的view）2.8
        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView textView = (TextView) view.findViewById(R.id.tv);
                String name = textView.getText().toString();
                searchview.setText(name);

                //隐藏软件盘（在软件盘打开的情况下，即使fragment跳转了，软件盘还是打开着的 2.25）
                //下面这个就是强行关闭软件盘的方法
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);

                if (name!=null){
                    HistoryDetailFragment newfragment3 = HistoryDetailFragment.newInstance(name);
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.rlroot,newfragment3)
                            .commit();
                    searchview.setText("");

                }

            }
        });

        // 第一次进入查询所有的历史记录
        queryData("");

        Mainactivity.getChildFragmentWebviewCallback(this);

    }


    public static ChangeStateBarCallbcak changeStateBarCallbcak;
    public static void setChangeStateBarListener(ChangeStateBarCallbcak changeStateBarCallbcak){
        SearchFragment.changeStateBarCallbcak=changeStateBarCallbcak;

    }










    /**
     * 模糊查询数据，并改变数据适配器2.5，如果模糊查询为空就是查询所有的2.5
     */
    private void queryData(String tempName) {

        //下面这个是原版
//        Cursor cursor = helper.getReadableDatabase().rawQuery(
//                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);

        //模糊查询并只显示前10条数据 2.5 (数据库查表也是从0开始的)
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc limit 0,10 ", null);


        // 创建adapter适配器对象(这个是原来为改动的)
//        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
//                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        //第四个和第五个参数有对应关系
        //第五个参数的id是，第二个参数视图里面的id。上面的哪个原来的方法也是 2.5
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.history_list, cursor, new String[] { "name" },
                new int[] { R.id.tv}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


        // 设置适配器
        history.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 插入数据
     */
    private void insertData(String tempName) {

        db = helper.getWritableDatabase();

        String sql = "select count(*) from records";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);

        //因为这里是先获取的总数然后再添加，所以会产生多一个的表面现象2.5
        Log.e("---总条数---",count+" ");

        db.execSQL("insert into records(name) values('" + tempName + "')");

        db.close();


    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }


    @Override
    public void onDestroy() {
        changeStateBarCallbcak.changestatebarcallbcak();
        LogUtil.e("233--销毁-"+this.getClass().getSimpleName());
        Mainactivity.getChildFragmentWebviewCallback(null);
        super.onDestroy();
    }
}
