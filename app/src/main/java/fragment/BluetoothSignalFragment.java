package fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.london.gofor.insilocation.DeviceListAdapter;
import com.london.gofor.insilocation.R;
import com.london.gofor.insilocation.iBeaconClass;


/**
 * Created by mac on 15/5/11.
 */
public class BluetoothSignalFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    private DeviceListAdapter deviceListAdatper;
    private ListView lv;

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
