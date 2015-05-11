package com.london.gofor.insilocation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.Switch;
import com.london.gofor.insilocation.DeviceListAdapter;
import com.london.gofor.insilocation.iBeaconClass.iBeacon;

import java.util.List;

/**
 * Created by Administrator on 2015/4/29.
 */
public class BluetoothInfoActivity extends Activity {

    public static final int TAG = Log.d("tag", "stop scan");
    private Switch switchView;
    private BluetoothAdapter mBluetoothAdapter;
    private DeviceListAdapter deviceListAdatper;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetoothinfo);
        switchView = (Switch) findViewById(R.id.switchView);


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

        // open bluetooth
        mBluetoothAdapter.enable();

        // 设备列表操作
        deviceListAdatper = new DeviceListAdapter(this);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(deviceListAdatper);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // do something
            }
        });

        switchView.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean check) {
                // open bluetooth
                if (check == true) mBluetoothAdapter.enable();
                else mBluetoothAdapter.disable();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        scanLeDevice(true);
    }

    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        deviceListAdatper.clear();
        Log.d("tag", "onPause");
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            Log.d("tag", "start scan");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.d("tag","stop scan");
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Device scan callBack.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi, scanRecord);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("tag", "onLeScan");
                            deviceListAdatper.addDevice(ibeacon);
                            deviceListAdatper.notifyDataSetChanged();
                        }
                    });
                }
            };
}
