package com.example.myble.NordicBLE;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by kukkoo on 5/3/2017.
 */

public class DeviceBean {

    private String title;
    private String address;
    private int rssi;
    private boolean isConnected = false;
    private boolean isStarted = false;
    private boolean isExpiredAck = false;
    //private boolean isStopped = true;

    private boolean isManuallDisconnnected = false;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic = null;

    public String getTitle() {
        return title;
    }

    public DeviceBean(String title, String address, int rssi) {
        this.title = title;
        this.address = address;
        this.rssi = rssi;
    }

    public DeviceBean() {}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isExpiredAck() {
        return isExpiredAck;
    }

    public void setExpiredAck(boolean expiredAck) {
        isExpiredAck = expiredAck;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristic() {
        return bluetoothGattCharacteristic;
    }

    public void setBluetoothGattCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
    }

//    public boolean isStopped() {
//        return isStopped;
//    }
//
//    public void setStopped(boolean stopped) {
//        isStopped = stopped;
//    }

    public boolean isManuallDisconnnected() {
        return isManuallDisconnnected;
    }

    public void setManuallDisconnnected(boolean manuallDisconnnected) {
        isManuallDisconnnected = manuallDisconnnected;
    }
}
