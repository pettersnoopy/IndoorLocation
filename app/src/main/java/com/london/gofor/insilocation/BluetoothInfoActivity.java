package com.london.gofor.insilocation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.Switch;
import com.london.gofor.insilocation.DeviceListAdapter;
import com.london.gofor.insilocation.iBeaconClass.iBeacon;

import java.util.List;

import fragment.BluetoothSignalFragment;

/**
 * Created by Administrator on 2015/4/29.
 */
public class BluetoothInfoActivity extends FragmentActivity {

    public static enum BlueInfoPage { main, blueinfo, moredevice }

    public static final int TAG = Log.d("tag", "stop scan");
    private Switch switchView;
    private ViewFlipper infoflipper;
    private BluetoothAdapter mBluetoothAdapter;
//    private DeviceListAdapter deviceListAdatper;
    private BluetoothSignalFragment signalFragment;
    private ButtonRectangle signalinfoButton;
    private ButtonRectangle moreBluetoothDeviceButton;
    private LinearLayout toblue;
    private Bundle toFragmentBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetoothinfo);
        toblue = (LinearLayout) findViewById(R.id.toblue);
        infoflipper = (ViewFlipper) findViewById(R.id.bluetoothflipper);
        switchView = (Switch) findViewById(R.id.switchView);
        signalinfoButton = (ButtonRectangle) findViewById(R.id.btninfo);
        moreBluetoothDeviceButton = (ButtonRectangle) findViewById(R.id.btndev);
        signalinfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(BlueInfoPage.blueinfo);
            }
        });
        moreBluetoothDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(BlueInfoPage.moredevice);
            }
        });
        toblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(BlueInfoPage.main);
            }
        });

        // 蓝牙操作
        // 判断蓝牙是否可用
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 获得蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 设备列表操作
//        deviceListAdatper = new DeviceListAdapter(this);

        switchView.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean check) {
                // open bluetooth
                if (check == true) mBluetoothAdapter.enable();
                else mBluetoothAdapter.disable();
            }
        });

        if (signalFragment == null) {
            signalFragment = new BluetoothSignalFragment();
//            signalFragment.setArguments(toFragmentBundle);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.signalframelayout, signalFragment).commit();
    }

    protected void onResume() {
        super.onResume();
//        scanLeDevice(true);
    }

    protected void onPause() {
        super.onPause();
//        scanLeDevice(false);
//        deviceListAdatper.clear();
//        Log.d("tag", "onPause");
    }

//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            Log.d("tag", "start scan");
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
//        } else {
//            Log.d("tag","stop scan");
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//        }
//        invalidateOptionsMenu();
//    }
//
//    // Device scan callBack.
//    private BluetoothAdapter.LeScanCallback mLeScanCallback =
//            new BluetoothAdapter.LeScanCallback() {
//                @Override
//                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//                    final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi, scanRecord);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d("tag", "onLeScan");
//                            deviceListAdatper.addDevice(ibeacon);
//                            deviceListAdatper.notifyDataSetChanged();
//                        }
//                    });
//                }
//            };

    public void gotoPage (BlueInfoPage page) {
        switch (page) {
            case main:
//                infoflipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
//                infoflipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                infoflipper.setDisplayedChild(page.ordinal());
                break;
            case blueinfo:
//                infoflipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
//                infoflipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                infoflipper.setDisplayedChild(page.ordinal());
                break;
            case moredevice:
                break;
        }
    }
}
