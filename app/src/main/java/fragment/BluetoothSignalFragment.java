package fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.london.gofor.insilocation.DeviceListAdapter;
import com.london.gofor.insilocation.MessageActivity;
import com.london.gofor.insilocation.R;
import com.london.gofor.insilocation.iBeaconClass;

import java.util.ArrayList;


/**
 * Created by mac on 15/5/11.
 */
public class BluetoothSignalFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    private DeviceListAdapter deviceListAdatper;
    private ListView lv;
    private NotificationManager notificationManager;
    private Notification notification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signalinfo, null);
//        Bundle bundle = this.getArguments();
        lv = (ListView) view.findViewById(R.id.lv);
//        DeviceListAdapter adapter = (DeviceListAdapter) bundle.getSerializable("devicelist");
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        deviceListAdatper = new DeviceListAdapter(getActivity());
        lv.setAdapter(deviceListAdatper);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        scanLeDevice(true);
    }

    @Override
    public void onPause() {
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
        getActivity().invalidateOptionsMenu();
    }

    // Device scan callBack.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    final iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi, scanRecord);

                    int r = ibeacon.rssi;
                    if (ibeacon.proximityUuid.equals("e2c56db5-dffb-48d2-b060-d0f5a71096e0")) {
                        if (r > -75) {
                            String tmp = ibeacon.proximityUuid;
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
                            if (notificationManager == null)
                                notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            if (notification == null)
                                notification = new Notification(R.drawable.icon, "Entered region: " + ibeacon.name + " " + uuid, System.currentTimeMillis());
                            notification.flags = Notification.FLAG_AUTO_CANCEL;
                            Intent intent = new Intent(getActivity(), getActivity().getClass());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent contentIntent = PendingIntent.getActivity(
                                    getActivity(),
                                    R.string.app_name,
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            notification.setLatestEventInfo(
                                    getActivity(),
                                    "Entered region: " + uuid,
                                    "Welcome to nmlab laboratory!",
                                    contentIntent);
                            notificationManager.notify(R.string.app_name, notification);
                        } else {
                            // leave bluetooth device
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
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
