package com.just.btconnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements OnChartValueSelectedListener{

    private ToggleButton blueToothControlButton;
    private ToggleButton blueToothSearchingButton;
    private BluetoothAdapter bluetoothAdapter;
    private ListView deviceNameListView;
    private ArrayList<String> deviceNameList;
    private BlueToothReceiver blueToothReceiver;
    private BlueToothController blueToothController;
//    private Button sendDataButton;

    public static int mTime = 0;
    public static LineChart mChart, mChart2, mChart3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        checkBlueToothStatus();
        checkBlueToothSearchStatus();
        setListener();
//        System.out.println("================================");
//        GetDataThread getDataThread = new GetDataThread();
//        getDataThread.start();
//        System.out.println("/////////////////////////////");

    }



    @Override
    protected void onDestroy() {
        unregisterReceiver(blueToothReceiver);
        blueToothController.socketClose();
        super.onDestroy();
    }

    /**
     * 控件初始化
     */
    void init() {


        mChart = (LineChart) findViewById(R.id.chart1);
        mChart2 = (LineChart) findViewById(R.id.chart11);
        mChart3 = (LineChart) findViewById(R.id.chart111);


        CubicLineChart.setChart(mChart, "description of the data of x", 1300f, 1600f);
        CubicLineChart.setChart(mChart2, "description of the data of y", 1100f, 1400f);
        CubicLineChart.setChart(mChart3, "description of the data of z", 1500f, 1800f);

        blueToothControlButton = (ToggleButton) findViewById(R.id.openBT);
        blueToothSearchingButton = (ToggleButton) findViewById(R.id.searchDevice);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //sendDataButton = (Button)findViewById(R.id.senddata);

        deviceNameListView = (ListView) findViewById(R.id.deviceName);
        deviceNameList = new ArrayList();

        blueToothController = new BlueToothController(bluetoothAdapter);

        blueToothReceiver = new BlueToothReceiver();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);     //过滤器
        registerReceiver(blueToothReceiver, intentFilter);                              //注册接收器
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
     * 检测搜索状态，并且显示在ToggleButton中
     */
    void checkBlueToothSearchStatus() {
        if (bluetoothAdapter.isDiscovering())
            blueToothSearchingButton.setChecked(true);
        else
            blueToothSearchingButton.setChecked(false);
    }

    /**
     * 设置监听器
     */
    void setListener() {
        mChart.setOnChartValueSelectedListener(this);
        mChart2.setOnChartValueSelectedListener(this);
        mChart3.setOnChartValueSelectedListener(this);

        blueToothControlButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    bluetoothAdapter.enable();
                else
                    bluetoothAdapter.disable();
            }
        });

        blueToothSearchingButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    bluetoothAdapter.startDiscovery();
                else
                    bluetoothAdapter.cancelDiscovery();
            }
        });

//        sendDataButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (blueToothController.isConnected())
//                {
//                    Toast.makeText(getApplicationContext(),"连接成功", Toast.LENGTH_SHORT).show();
//                    blueToothController.sendData(48);
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"连接未成功", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        deviceNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), deviceNameList.get(position), Toast.LENGTH_SHORT).show();
                String deviceInfo = deviceNameList.get(position);
                String connectMac = "";
                for (int i = 0; i < deviceInfo.length(); i++) {
                    if (deviceInfo.charAt(i) == '|') {
                        connectMac = deviceInfo.substring(i + 1);
                        break;
                    }

                }
                blueToothController.blueToothConnect(connectMac);

            }
        });
    }

    /**
     * 显示周围设备在LIstView中
     */
    void showDeviceList() {
        deviceNameListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNameList));
    }

    /**
     * Called when a value has been selected inside the chart.
     *
     * @param e            The selected Entry.
     * @param dataSetIndex The index in the datasets array of the data object
     *                     the Entrys DataSet is in.
     * @param h            the corresponding highlight object that contains information
     */
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    @Override
    public void onNothingSelected() {

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
                if (!deviceNameList.contains(bluetoothDevice.getName() + '|' + bluetoothDevice.getAddress()))
                    deviceNameList.add(bluetoothDevice.getName() + '|' + bluetoothDevice.getAddress());

                showDeviceList();

            }

        }
    }

    class GetDataThread extends Thread {

        public void run() {
            try {
                while (true) {
                    mTime+=1;
                    float mData = blueToothController.getData('x');
                    float mData2 = blueToothController.getData('y');
                    float mData3 = blueToothController.getData('z');
                    System.out.println(mData+"......................................"+MainActivity.mTime);
                    CubicLineChart.addEntry(mChart,mTime, mData, "x-data", Color.rgb(0, 0, 255), ColorTemplate.getHoloBlue());
                    CubicLineChart.addEntry(mChart2,mTime,mData2,"y-data",Color.rgb(255,0,0),ColorTemplate.getHoloBlue());
                    CubicLineChart.addEntry(mChart3,mTime,mData3,"z-data",Color.rgb(0, 255, 0),ColorTemplate.getHoloBlue());
                    Thread.sleep(50);
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }


    }

//    private class MyClickListener implements OnClickListener {
//        /**
//         * Called when a view has been clicked.
//         *
//         * @param v The view that was clicked.
//         */
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//            case R.id.openBT:
//                bluetoothAdapter.enable();
//                break;
//            default:
//                break;
//            }
//        }
//    }


}
