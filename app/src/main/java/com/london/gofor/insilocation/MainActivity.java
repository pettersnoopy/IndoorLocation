package com.london.gofor.insilocation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTabs();
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
