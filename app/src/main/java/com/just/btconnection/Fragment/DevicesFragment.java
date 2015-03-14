package com.just.btconnection.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ToggleButton;

import com.just.btconnection.BlueToothController;
import com.just.btconnection.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DevicesFragment extends Fragment {

    private ToggleButton blueToothControlButton;
    private Button blueToothSearchingButton;
    private BluetoothAdapter bluetoothAdapter;
    private ListView deviceNameListView;
    //private ArrayList<String> deviceNameList;
    private BlueToothReceiver blueToothReceiver;
    private BlueToothController blueToothController;

    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, Object>> deviceListItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        blueToothControlButton = (ToggleButton) getActivity().findViewById(R.id.openBT);
        blueToothSearchingButton = (Button) getActivity().findViewById(R.id.searchDevice);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //sendDataButton = (Button)findViewById(R.id.senddata);

        deviceNameListView = (ListView) getActivity().findViewById(R.id.deviceName);
        deviceListItem = new ArrayList<>();

        blueToothController = new BlueToothController(bluetoothAdapter);

        blueToothReceiver = new BlueToothReceiver();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);     //过滤器
        getActivity().registerReceiver(blueToothReceiver, intentFilter);                              //注册接收器


        checkBlueToothStatus();
        setListener();

    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(blueToothReceiver);
        blueToothController.socketClose();
        super.onDestroy();
    }

    /**
     * 检测蓝牙状态，并且显示在ToggleButton中
     */
    void checkBlueToothStatus() {
        if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            blueToothControlButton.setChecked(true);
        } else
            blueToothControlButton.setChecked(false);
    }


    /**
     * 设置监听器
     */
    void setListener() {
//        mChart.setOnChartValueSelectedListener(this);
//        mChart2.setOnChartValueSelectedListener(this);
//        mChart3.setOnChartValueSelectedListener(this);

        blueToothControlButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("open.....................");
                if (isChecked)
                    bluetoothAdapter.enable();
                else
                    bluetoothAdapter.disable();
            }
        });

        blueToothSearchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter.startDiscovery();
            }
        });
        deviceNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String connectMac = (String) (deviceListItem.get(position)).get("DeviceAddress");
                blueToothController.blueToothConnect(connectMac);

            }
        });
    }

    /**
     * 显示周围设备在LIstView中
     */
    void showDeviceList() {
        simpleAdapter = new SimpleAdapter(getActivity(), deviceListItem, R.layout.activity_list_item, new String[]{"ItemImage", "DeviceName", "DeviceAddress"},
                new int[]{R.id.ItemImage, R.id.ItemTitle, R.id.ItemText});
        deviceNameListView.setAdapter(simpleAdapter);
    }

    /**
     * 内部类，自定义广播接收类，用来接收蓝牙扫描讯息
     */
    private class BlueToothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("DeviceName", bluetoothDevice.getName());
                map.put("DeviceAddress", bluetoothDevice.getAddress());

                if(!deviceListItem.contains(map))
                    deviceListItem.add(map);

                showDeviceList();

            }

        }
    }
}
