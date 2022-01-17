package com.example.myble.NordicBLE.Manager1;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import no.nordicsemi.android.ble.BleManagerCallbacks;

public interface UARTManagerCallbacks extends BleManagerCallbacks {

    void onDataReceived(@NonNull final BluetoothDevice device, final byte[] data);

    void onDataSent(@NonNull final BluetoothDevice device, final byte[] data);
}