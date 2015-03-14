package com.just.btconnection;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.just.btconnection.Fragment.AboutFragment;
import com.just.btconnection.Fragment.DevicesFragment;
import com.just.btconnection.Fragment.GraphFragment;

/**
 * Created by tim on 15/3/12.
 */
public class MainTabActivity extends FragmentActivity{


    private ToggleButton blueToothControlButton;
    private ToggleButton blueToothSearchingButton;
    private Button testBtn;
    //private View mView = DevicesFragment.getRootView();

    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {DevicesFragment.class,GraphFragment.class,AboutFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_devices_btn,R.drawable.tab_graph_btn,R.drawable.tab_about_btn};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"设备", "数据", "关于"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);

        initView();
    }

    /**
     * 初始化组件
     */
    private void initView(){
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }
}