package com.yangpeiwen.bluetoothcar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class Controller extends AppCompatActivity {
    BluetoothSPP bt;
    TextView textRead;
    TextView textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        textRead = (TextView)findViewById(R.id.textView_rec );
        textStatus = (TextView)findViewById(R.id.textView_state);
        bt = new BluetoothSPP(this);
        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "蓝牙不可用"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }



        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                textStatus.setText("状态:未连接");
            }

            public void onDeviceConnectionFailed() {
                textStatus.setText("状态:连接失败");
            }

            public void onDeviceConnected(String name, String address) {
                textStatus.setText("状态:已连接上" + name);
            }
        });

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                textRead.setText(message);
            }
        });


        findViewById(R.id.button_w).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    send("w");
                } else if (action == MotionEvent.ACTION_UP) {
                    send("t");
                }
                return false;
            }
        });

        findViewById(R.id.button_a).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    send("a");
                }else if(action == MotionEvent.ACTION_UP){
                    send("t");
                }
                return false;
            }
        });

        findViewById(R.id.button_s).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    send("s");
                }else if(action == MotionEvent.ACTION_UP){
                    send("t");
                }
                return false;
            }
        });

        findViewById(R.id.button_d).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    send("d");
                }else if(action == MotionEvent.ACTION_UP){
                    send("t");
                }
                return false;
            }
        });

        findViewById(R.id.button_f).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    send("f");
                }else if(action == MotionEvent.ACTION_UP){
                    send("t");
                }
                return false;
            }
        });

        findViewById(R.id.button_x).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    send("x");
                }
                return false;
            }
        });

        findViewById(R.id.button_z).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    send("z");
                }
                return false;
            }
        });


    }

    public void send(String s){
        if(bt.getServiceState()== BluetoothState.STATE_CONNECTED)
        {
            bt.send(s, false);
        }else {
            xuanze();
        }
    }

    public void xuanze()
    {
        bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        intent.putExtra("bluetooth_devices", "蓝牙设备");
        intent.putExtra("no_devices_found", "没有发现设备");
        intent.putExtra("scanning", "搜索中");
        intent.putExtra("scan_for_devices", "搜索");
        intent.putExtra("select_device", "选择");
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    public void lianjie(View v){
        xuanze();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }
}
