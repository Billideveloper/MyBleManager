package com.example.myble.NordicBLE.Manager2;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import no.nordicsemi.android.ble.BleManagerCallbacks;

public interface UARTManagerCallbacks2 extends BleManagerCallbacks {

    void onDataReceived(@NonNull final BluetoothDevice device, final byte[] data);

    void onDataSent(@NonNull final BluetoothDevice device, final byte[] data);
}
