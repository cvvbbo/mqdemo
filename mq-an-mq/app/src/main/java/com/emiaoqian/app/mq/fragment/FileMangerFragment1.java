package com.emiaoqian.app.mq.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emiaoqian.app.mq.R;
import com.emiaoqian.app.mq.adapter.RootListViewAdapter;
import com.emiaoqian.app.mq.adapter.SubListViewAdapter;
import com.emiaoqian.app.mq.utils.ScreenUtils;

/**
 * Created by xiong on 2018/1/18.
 *
 * 原生的文件管理以后用
 *
 */

public class FileMangerFragment1 extends BaseFragment implements View.OnClickListener{

    private TextView file_type;

    private PopupWindow mPopupWindow;

    /**
     * 二级菜单的根目录
     */
    private ListView rootListView;

    /**
     * 根目录的节点
     */
    //private String[] fileType = new String[] { "附近", "排序", "筛选" };




    /**
     * 子目录节点
     */
//    private String[][] sub_items = new String[][] {
//            new String[] { "海淀区", "西城区", "石景山区", "东城区", "朝阳区" },
//            new String[] { "离我最近", "人气最高", "评价最好", "人均最低", "人均最高" },
//            new String[] { "全部类型", "优惠券商户", "闪惠商户", "预约服务", "上门服务" } };





    private ListView subListView;

    /**
     * 弹出的popupWindow布局
     */
    private RelativeLayout popupLayout;

    /**
     * 子目录的布局
     */
    private FrameLayout subLayout;

    /**
     * 根目录被选中的节点
     */
    private int selectedPosition;


    private int selectedChild;

    Handler handler=new Handler();
    private String[] fileType;
    private String[] sendFile;
    private String[] receiverFile;
    private String[] receiverManger;
    private String[][] sub_items;

    @Override
    public int getlayout() {
        return R.layout.file_manger_view;
    }

    @Override
    public void initialize() {
        file_type = (TextView) view.findViewById(R.id.file_type);
        file_type.setOnClickListener(this);
        //从资源文件中获取数据
        fileType = getResources().getStringArray(R.array.file_type);
        sendFile = getResources().getStringArray(R.array.send_file);
        receiverFile = getResources().getStringArray(R.array.receiver_file);
        receiverManger = getResources().getStringArray(R.array.receiver_manger);

        sub_items = new String[][]{sendFile,receiverFile,receiverManger};

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.file_type:
                showPopBtn(ScreenUtils.getScreenWidth(getActivity()),
                        ScreenUtils.getScreenHeight(getActivity()));
                break;
        }

    }

    private void showPopBtn(int screenWidth, int screenHeight) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        //这里是初始化弹窗的位置
        popupLayout = (RelativeLayout) inflater.inflate(
                R.layout.popupwindow_layout, null, false);
        //这个是一级菜单
        rootListView = (ListView) popupLayout.findViewById(R.id.root_listview);



        /**************二级目录的数据适配器****************/
        //一级目录的数据适配器 1.18
        final RootListViewAdapter adapter = new RootListViewAdapter(
                getActivity());
        //第一步就是设置数据 这个在一级目录的数据适配器里面就能发现为什么要第一步设置数据适配器 1.18
        adapter.setItems(fileType);
        rootListView.setAdapter(adapter);

        /**
         * 子popupWindow 这个就是传说中的二级菜单，然后它的设计是在外面套上了一层framelayout 1.18
         */
        subLayout = (FrameLayout) popupLayout.findViewById(R.id.sub_popupwindow);

        /**
         * 初始化subListview ，二级目录的listview 1.18
         */
        subListView = (ListView) popupLayout.findViewById(R.id.sub_listview);

        /**
         * 弹出popupwindow时，二级菜单默认隐藏，当点击某项时，二级菜单再弹出，设置为gone最好，不然的话，点击左边并不能把弹窗给隐藏起来 1.18
         *
         * 但是设置为gone之后，一级菜单的popupwindows会变宽 1.18
         */
        subLayout.setVisibility(View.GONE);

        //8
        final int stata=subLayout.getVisibility();

        Log.e("--state--",stata+"");

        //这个点击pop自身消失，因为pop开始是隐藏了一半的，然后下面只设置了点击其他位置让pop消失，点击隐藏的部分并不会消失 1.18
        popupLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (mPopupWindow!=null&&stata==8&&mPopupWindow.isShowing()){

                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });



        /**
         * 初始化mPopupWindow
         */

        //这里添加的是整个popupwindows
        mPopupWindow = new PopupWindow(popupLayout, screenWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        /**
         * 有了mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
         * 这句可以使点击popupwindow以外的区域时popupwindow自动消失 但这句必须放在showAsDropDown之前
         */

        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        /**
         * popupwindow的位置，第一个参数表示位于哪个控件之下 第二个参数表示向左右方向的偏移量，正数表示向左偏移，负数表示向右偏移
         * 第三个参数表示向上下方向的偏移量，正数表示向下偏移，负数表示向上偏移
         *
         */
        mPopupWindow.showAsDropDown(file_type, -5, 25);// 在控件下方显示popwindow

        mPopupWindow.update();

        rootListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub

                        //这几个位置要特殊处理！！ 1.18
                        if (position==0||position==4||position==5){
                            mPopupWindow.dismiss();
                            return;
                        }

                        /**
                         * 选中root某项时改变该ListView item的背景色
                         */
                        adapter.setSelectedPosition(position);
                        adapter.notifyDataSetInvalidated(); //原来的是用的是这个的，但是什么原理未知
                        //adapter.notifyDataSetChanged();

                        //选中某个条目 1.18 （这里的不用减去1，因为是已经）
                        selectedPosition = position;

                        //二级目录的适配器(因为这里数据第一个是不算的，所以position的位置要减一，)
                        final SubListViewAdapter subAdapter = new SubListViewAdapter(
                                //二级目录的数据源
                                getActivity(), sub_items, position-1);
                        subListView.setAdapter(subAdapter);

                        /**
                         * 选中某个根节点时，使显示相应的子目录可见
                         */


                        subLayout.setVisibility(View.VISIBLE);
                        subListView
                                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> parent, View view,
                                            int position, long id) {
                                        // TODO Auto-generated method stub
                                        // popupLayout.setVisibility(View.GONE); 原来有这一句 1.18
                                        selectedChild=position;

                                        subAdapter.setSelectedPosition(position);
                                        subAdapter.notifyDataSetChanged();

                                        //点击之后让popupwindow消失
                                        //延迟显示为了点击能看到二级菜单被点击的效果 1.18
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPopupWindow.dismiss();

                                            }
                                        },300);

                                        //mPopupWindow.dismiss();


                                        //注意下面的条目值要记得减去-
                                        Toast.makeText(
                                                getActivity(),
                                                sub_items[selectedPosition-1][position],
                                                Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                });
    }
}
