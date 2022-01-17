package com.example.myblemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import com.example.myble.NordicBLE.MyBleCallbacks;
import com.example.myble.NordicBLE.MyManager;

public class MainActivity extends AppCompatActivity implements MyBleCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyManager.shared.setCallbacks(this);
    }

    @Override
    public void onScanResults(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onDeviceConnected(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onDeviceDisConnected(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onSessionStarted(@NonNull Boolean isStarted) {

    }

    @Override
    public void onSessionStopped(@NonNull Boolean isStarted) {

    }

    @Override
    public void onSessionDataReceived(@NonNull BluetoothDevice device, byte[] data) {

    }
}