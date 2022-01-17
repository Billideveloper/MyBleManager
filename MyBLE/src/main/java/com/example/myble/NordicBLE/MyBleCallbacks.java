package com.example.myble.NordicBLE;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

public interface MyBleCallbacks {

    void onScanResults(@NonNull final BluetoothDevice device);

    void onDeviceConnected(@NonNull final BluetoothDevice device);

    void onDeviceDisConnected(@NonNull final BluetoothDevice device);

    void onSessionStarted(@NonNull final Boolean isStarted);

    void onSessionStopped(@NonNull final Boolean isStarted);

    void onSessionDataReceived(@NonNull final BluetoothDevice device, byte[] data);



}
