package com.example.student.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.View;

import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.HeartRateNotifyListener;
import com.zhaoxiaodan.miband.listeners.NotifyListener;
import com.zhaoxiaodan.miband.listeners.RealtimeStepsNotifyListener;
import com.zhaoxiaodan.miband.model.BatteryInfo;
import com.zhaoxiaodan.miband.model.LedColor;
import com.zhaoxiaodan.miband.model.UserInfo;
import com.zhaoxiaodan.miband.model.VibrationMode;

public class MainActivity extends AppCompatActivity {

    private MiBand miband;
    private BluetoothDevice device;
    private ScanCallback scanCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            // Write you code here if permission already given.
        }
        requestHehe();

//        final BluetoothDevice device = intent.getParcelableExtra("device");
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                device = result.getDevice();
                String name = device.getName();
                int x = 5;
                if(name!=null){
                    if(name.contains("MI1S"))
                    {
                        MiBand.stopScan(scanCallback);
                        connectToMiBand();
                    }
                }

            }
        };

    }
    void connectToMiBand() {
        miband = new MiBand(this);
        miband.connect(device, new ActionCallback() {

            @Override
            public void onSuccess(Object data) {
            MiBand.stopScan(scanCallback);
                miband.setDisconnectedListener(new NotifyListener() {
                    @Override
                    public void onNotify(byte[] data) {

                    }
                });
            }

            @Override
            public void onFail(int errorCode, String msg) {
                int bbb=15;
            }
        });

    }

    void requestHehe(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);

        }
    }

    public void onClick(View view) {
        MiBand.startScan(scanCallback);

    }

    public void onClick2(View view) {
        miband.startVibration(VibrationMode.VIBRATION_WITH_LED);
//        miband.getBatteryInfo(new ActionCallback() {
//
//            @Override
//            public void onSuccess(Object data) {
//                BatteryInfo info = (BatteryInfo) data;
//                Log.d("Siema", info.toString());
//            }
//
//            @Override
//            public void onFail(int errorCode, String msg) {
//                Log.d("Elo", "getBatteryInfo fail");
//            }
//        });
    }
}
