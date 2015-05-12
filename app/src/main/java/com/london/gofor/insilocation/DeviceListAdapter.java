package com.london.gofor.insilocation;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import com.london.gofor.insilocation.iBeaconClass.iBeacon;

/**
 * Created by Administrator on 2015/4/29.
 */
public class DeviceListAdapter extends BaseAdapter {

    // ble 设备队列
    private ArrayList<iBeaconClass.iBeacon> mLeDevice;
    private LayoutInflater mInflator;
    private NotificationManager notificationManager;
    private Notification notification;

    public ArrayList<iBeaconClass.iBeacon> getmLeDevice() {
        return mLeDevice;
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceUUID;
        TextView deviceMajor_Minor;
    }

    public DeviceListAdapter(Context context) {
        this.mInflator = LayoutInflater.from(context);
        mLeDevice = new ArrayList<iBeacon>();
    }

    @Override
    public int getCount() {
        return mLeDevice.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevice.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void clear() {
        mLeDevice.clear();
    }
    public void addDevice(iBeacon device) {
        if(device == null)
            return;

        for(int i=0; i<mLeDevice.size(); i++) {
            String btAddress = mLeDevice.get(i).bluetoothAddress;
            if(btAddress.equals(device.bluetoothAddress)) {
                mLeDevice.add(i+1, device);
                mLeDevice.remove(i);
                return;
            }
        }

        mLeDevice.add(device);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null) {
            view = mInflator.inflate(R.layout.item, null);  //根据布局创建视图
            viewHolder = new ViewHolder();
//            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            viewHolder.deviceUUID= (TextView)view.findViewById(R.id.device_beacon_uuid);
            viewHolder.deviceMajor_Minor=(TextView)view.findViewById(R.id.device_major_minor_rssi);
//            viewHolder.devicetxPower_RSSI=(TextView)view.findViewById(R.id.device_txPower_rssi);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        iBeaconClass.iBeacon device = mLeDevice.get(i);
        final String deviceName = device.name;
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);

//        viewHolder.deviceAddress.setText(device.bluetoothAddress);
        String tmp = device.proximityUuid;
        Log.d("uuid", tmp);
        String uuid = "";
        for (int j = 0; j < tmp.length(); j++) {
            if (tmp.charAt(j) == '-') break;
            char c = tmp.charAt(j);
            if (tmp.charAt(j) <= 'z' && tmp.charAt(j) >= 'a') {
                c = (char) (tmp.charAt(j) + 'A' - 'a');
            }
            uuid += c;
        }
//        viewHolder.deviceAddress.setText(uuid);
        viewHolder.deviceUUID.setText(uuid);
        viewHolder.deviceMajor_Minor.setText("major:"+device.major+", minor:"+device.minor + ", rssi:" + device.rssi);
//        viewHolder.devicetxPower_RSSI.setText("txPower:"+device.txPower+",rssi:"+device.rssi);
        return view;
    }
}
