package com.london.gofor.insilocation;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.london.gofor.insilocation.BluetoothInfoActivity;
import com.london.gofor.insilocation.MessageActivity;
import com.london.gofor.insilocation.R;
import com.london.gofor.insilocation.ShowInMapActivity;
import com.london.gofor.insilocation.UserInfoActivity;


public class MainActivity extends TabActivity {

    private final String TAG = "MainTab";
    private Dialog sureQuite;

    private TabHost tabhost;
    private RadioGroup main_radiogroup;
    private RadioButton tab_icon_weixin, tab_icon_address, tab_icon_friend,tab_icon_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取屏幕
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();

        //获取按钮
        main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);

        tab_icon_weixin = (RadioButton) findViewById(R.id.tab_icon_weixin);
        tab_icon_address = (RadioButton) findViewById(R.id.tab_icon_address);
        tab_icon_friend = (RadioButton) findViewById(R.id.tab_icon_friend);
        tab_icon_setting = (RadioButton) findViewById(R.id.tab_icon_setting);

        //往TabWidget添加Tab
        tabhost = getTabHost();
        tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, BluetoothInfoActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this, ShowInMapActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this, MessageActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag4").setIndicator("3").setContent(new Intent(this, UserInfoActivity.class)));

        //设置监听事件
        checkListener checkradio = new checkListener();
        main_radiogroup.setOnCheckedChangeListener(checkradio);
//        setTabs();
    }



    public class checkListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            //setCurrentTab 通过标签索引设置当前显示的内容
            //setCurrentTabByTag 通过标签名设置当前显示的内容
            switch(checkedId){
                case R.id.tab_icon_weixin:
                    tabhost.setCurrentTab(0);
                    //或
                    //tabhost.setCurrentTabByTag("tag1");
                    break;
                case R.id.tab_icon_address:
                    tabhost.setCurrentTab(1);
                    break;
                case R.id.tab_icon_friend:
                    tabhost.setCurrentTab(2);
                    break;
                case R.id.tab_icon_setting:
                    tabhost.setCurrentTab(3);
                    break;
            }


        }
    }

    private void setTabs() {
        addTab("info", R.drawable.info, BluetoothInfoActivity.class);
        addTab("Loc", R.drawable.position, ShowInMapActivity.class);
        addTab("Mes", R.drawable.message, MessageActivity.class);
        addTab("I", R.drawable.user, UserInfoActivity.class);
    }

    private void addTab(String labelId, int drawableId, Class<?> c) {
        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            onBackPressed();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (sureQuite == null) {
            sureQuite = new AlertDialog.Builder(this).setMessage(R.string.notify_exit_application).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    new InsideLocationApplication().destroyAll();
                    sureQuite.dismiss();
                }
            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).show();
        } else {
            sureQuite.show();
        }
    }


}
