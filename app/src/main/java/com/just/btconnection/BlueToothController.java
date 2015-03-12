package com.just.btconnection;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Switch;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Zhenyu on 2015/3/10.
 */
public class BlueToothController {
    private BluetoothAdapter bluetoothAdapter;
    private boolean connected;
    private BluetoothDevice bluetoothDevice;
    private static BluetoothSocket bluetoothSocket;
    private float x, y, z;
    private Handler msgHandler;
    private BTReadThread btReadThread;


    public BlueToothController(BluetoothAdapter Adapter) {
        bluetoothAdapter = Adapter;
        connected = false;
    }

    public void blueToothConnect(String macAddr) {
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddr);
        bluetoothAdapter.cancelDiscovery();
        connectDevice();
        btReadThread = new BTReadThread();
        btReadThread.start();
        Looper lp = Looper.myLooper();
        msgHandler = new MsgHandler(lp);

    }

    protected void connectDevice() {
        try {
            int connectState = bluetoothDevice.getBondState();
            switch (connectState) {
                // 未配对
                case BluetoothDevice.BOND_NONE:
                    // 配对
                    try {
                        Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                        createBondMethod.invoke(bluetoothDevice);
                        //System.out.println(bluetoothDevice.getBondState());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                // 已配对
                case BluetoothDevice.BOND_BONDED:
                    try {
                        // 连接
                        UUID dvcUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                        bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(dvcUUID);
                        bluetoothSocket.connect();
                        System.out.println(bluetoothSocket.isConnected());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }

    public void sendData(int data) {
        try {
            bluetoothSocket.getOutputStream().write(data);
        } catch (IOException e) {
        }

    }

    public void receiveData() {
        try {
            byte[] tmp = new byte[1024];
            int len = bluetoothSocket.getInputStream().read(tmp);
            if (len > 0) {
                byte[] tmp2 = tmp;
                String str = new String(tmp2);
                int[] index = new int[5];
                int num = 0;
                int length = str.length();
                for (int i = 0; i < length; i++) {
                    char chartmp = str.charAt(i);
                    if (chartmp == ' ' || chartmp == '\n' || chartmp == '\r') {
                        index[num] = i;
                        num++;
                        if (num > 3) break;
                    }
                }
                x = Float.parseFloat(str.substring(index[0], index[1]));
                y = Float.parseFloat(str.substring(index[1], index[2]));
                z = Float.parseFloat(str.substring(index[2], index[3]));
                System.out.println("" + x + " " + y + " " + z + "");
                //draw();
                Message msg = Message.obtain();
                msg.what = 0;
                msgHandler.sendMessage(msg);

            }
        } catch (IOException e) {
        }
    }

    private void draw() {
        MainActivity.mTime += 1;
        float mData = getData('x');
        float mData2 = getData('y');
        float mData3 = getData('z');
        System.out.println(mData + "......................................" + MainActivity.mTime);
        CubicLineChart.addEntry(MainActivity.mChart, MainActivity.mTime, mData, "x-data", Color.rgb(0, 0, 255), ColorTemplate.getHoloBlue());
        CubicLineChart.addEntry(MainActivity.mChart2, MainActivity.mTime, mData2, "y-data", Color.rgb(255, 0, 0), ColorTemplate.getHoloBlue());
        CubicLineChart.addEntry(MainActivity.mChart3, MainActivity.mTime, mData3, "z-data", Color.rgb(0, 255, 0), ColorTemplate.getHoloBlue());
    }

    public float getData(char type) {
        switch (type) {
            case 'x':
                return x;
            case 'y':
                return y;
            case 'z':
                return z;
            default:
                return -1f;
        }
    }

    public boolean isConnected() {
        return bluetoothSocket.isConnected();
    }

    public void socketClose() {
        try {
            if (bluetoothSocket.isConnected())
                bluetoothSocket.close();
        } catch (Exception e) {

        }

    }

    class BTReadThread extends Thread {

        public void run() {
            while (isConnected()) {
                receiveData();
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }


    }

    class MsgHandler extends Handler {
        public MsgHandler(Looper lp){
            super(lp);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
                    draw();
                    break;
                case 1 :
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
