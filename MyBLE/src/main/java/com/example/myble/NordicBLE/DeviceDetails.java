package com.example.myble.NordicBLE;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;


/*
   DeviceDetails which contains device's details, bluetooth's properties and method for enable notification
 */

public class DeviceDetails {
    private static final String TAG = DeviceDetails.class.getSimpleName();
    /** @connectedDevice this bluetooth peripheral is the peripheral provided by the BLE library weliem Blessed-Android */
    public BluetoothDevice device;
    public Integer manager = null;
    public boolean isManuallDisconnected = false;
    public boolean isConnected = false;
    public Integer index = null;
    public boolean isOTAUpdateAvailable = false;
    public String LastUpdatedVersionName = null;
    public static boolean isNotify = false;
    public static BluetoothGattCharacteristic rxCharacteristic = null;
    public static BluetoothGattCharacteristic txCharacteristic = null;
    public static BluetoothGattCharacteristic mNotifyCharacteristic = null;
    public static BluetoothGattCharacteristic mWriteCharacteristic = null;
    public static BluetoothGattCharacteristic mReadBatteryCharacteristic = null ;

    public DeviceDetails() {

    }
}
