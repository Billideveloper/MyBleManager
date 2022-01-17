package com.example.myble.NordicBLE;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.example.myble.NordicBLE.Manager1.Connection_Manager;
import com.example.myble.NordicBLE.Manager2.Connection_Manager2;
import com.example.myble.NordicBLE.ScanManager.Ble_Scanner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyManager {

    private static final String TAG = "MyManager";

    public static MyBleCallbacks MyBleCallbacks_ = null;

    public static MyManager shared = new MyManager();

    private static final String send = "0x50 0x00 0x00 0x50";

    private static final String sys_off = "0x22 0x00 0x22";

    public static List<DeviceBean> mDevices = FileCache.mDevices;

    public static byte[] sys = Common.convertingTobyteArray(sys_off);

    public static void  Startscan(){
        Ble_Scanner.shared.StartScan();
    }

    public static void  Stoptscan(){
        Ble_Scanner.shared.StopScan();
    }

    public static void Connect(BluetoothDevice device){

        if(Common.no_ofselectedDevice <= 0){
            Common.no_ofselectedDevice = 0;
        }

        if(FileCache.mDevices != null){
            Common.no_ofselectedDevice = FileCache.mDevices.size();
        }

        DeviceDetails deviceDetails;

        if(Common.no_ofselectedDevice == 0){

            if(Common.DEVICE_MAPS1.get(device.getAddress()) != null){
                deviceDetails = Common.DEVICE_MAPS1.get(device.getAddress());
            }else{
                deviceDetails = new DeviceDetails();
            }
            deviceDetails.index = Common.no_ofselectedDevice;

            Common.DEVICE_MAPS1.put(device.getAddress(), deviceDetails);

            Connection_Manager.shared.Connect(device);

        }else if(Common.no_ofselectedDevice == 1){

            DeviceDetails deviceDetails1;

            if(Common.DEVICE_MAPS2.get(device.getAddress()) != null){
                deviceDetails = Common.DEVICE_MAPS2.get(device.getAddress());
            }else{
                deviceDetails = new DeviceDetails();
            }
            deviceDetails.index = Common.no_ofselectedDevice;

            Common.DEVICE_MAPS2.put(device.getAddress(), deviceDetails);

            Connection_Manager2.shared.Connect(device);

        }

        Common.no_ofselectedDevice++;


    }

    public static void Disconnect(String foundaddress){

        DeviceDetails deviceDetails;

        if(Common.DEVICE_MAPS1.get(foundaddress) != null){
            deviceDetails = Common.DEVICE_MAPS1.get(foundaddress);
            deviceDetails.isManuallDisconnected = true;
            Common.DEVICE_MAPS1.put(foundaddress,deviceDetails);
            if(deviceDetails.index == 0){
                Connection_Manager.shared.Disconnect(foundaddress);

                if(FileCache.mDevices != null){
                    if(FileCache.mDevices.size() == 2){
                        Connection_Manager2.shared.Disconnect(FileCache.mDevices.get(1).getAddress());
                    }
                }

            }else if(deviceDetails.index == 1){
                Connection_Manager2.shared.Disconnect(foundaddress);
            }

            Common.no_ofselectedDevice--;

        }else if(Common.DEVICE_MAPS2.get(foundaddress) != null){
            deviceDetails = Common.DEVICE_MAPS2.get(foundaddress);
            deviceDetails.isManuallDisconnected = true;
            Common.DEVICE_MAPS2.put(foundaddress,deviceDetails);

            if(deviceDetails.index == 0){
                Connection_Manager.shared.Disconnect(foundaddress);
            }else if(deviceDetails.index == 1){
                Connection_Manager2.shared.Disconnect(foundaddress);
            }

            Common.no_ofselectedDevice--;

        }
    }

    public static void StartSession(){

        Common.IsManallystopped = false;
        Common.IsAutoStopped = false;
        Common.IsTrannsferCompleted = false;
        Common.isComplete = false;
        Common.isRestarted = false;
        Common.isRunning = false;
        Common.lastPktno = null;
        Common.Dev2lastPkt = null;
        Common.Dev1lastPkt = null;
        Common.lastStoredPktno = -1;

        Common.bFirstTime = true;
        Common.bFirstTime1 = true;

        byte[] convertedBytes = Common.convertingTobyteArray(send);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        Date now = new Date();
        //mBluetoothLeService.connect(mDeviceAddress);
//
//        GraphFragment.series.setTitle(mDevices.get(0).getTitle());
//
//        GraphFragment.logFileName = "LOG_FILE_" + formatter.format(now) +"_"+ mDevices.get(0).getTitle().trim()+"_LEFT.txt";//like 2016_01_12_1.txt
//        // for (int i = 0; i < mDevices.size(); i++) {

//      BluetoothLeService.writeCharacteristicNoresponse(mDevices.get(0).getAddress(), mReadCharacteristic, convertedBytes);
        if(mDevices.size() == 1){
            if(Common.DEVICE_MAPS1.get(mDevices.get(0).getAddress()) != null){
                if(Common.DEVICE_MAPS1.get(mDevices.get(0).getAddress()).isConnected == true){
                    Common.ISSingle = true;
                    Log.e(TAG, " Start send_start_cmd: SINGLE From M1" );
                    Connection_Manager.shared.SendData(Common.SingleStartCmd(),mDevices.get(0).getAddress());
                }
            }else if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null){
                if(Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true){
                    Common.ISSingle = true;
                    Log.e(TAG, " Start send_start_cmd: SINGLE From M2" );
                    Connection_Manager2.shared.SendData(Common.SingleStartCmd(),mDevices.get(0).getAddress(), null);
                }
            }
        } else if (mDevices.size() > 1){

            if(Common.DEVICE_MAPS1.get(mDevices.get(0).getAddress()) != null) {
                if (Common.DEVICE_MAPS1.get(mDevices.get(0).getAddress()).isConnected == true) {
                    if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()) != null) {
                        if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()).isConnected == true) {
                            Common.ISSingle = false;
                            Log.e(TAG, " Start send_start_cmd: DOUBLE From M1 & M2");
//                            GraphFragment.series1.setTitle(mDevices.get(1).getTitle());
//                            GraphFragment.logFileName1 = "LOG_FILE_" + formatter.format(now) +"_"+ mDevices.get(1).getTitle().trim()+"_RIGHT.txt";
//                            GraphFragment.logFileName2 = "LOG_FILE_" + formatter.format(now) +"_final.txt";
                            Connection_Manager.shared.SendData(Common.SingleLeftStartCmd(), mDevices.get(0).getAddress());
                            Connection_Manager2.shared.SendData(Common.SingleRightStartCmd(), mDevices.get(1).getAddress(), null);
                        } else {
                            Common.ISSingle = true;
                            Log.e(TAG, " Start send_start_cmd: SINGLE From M1");
                            Connection_Manager.shared.SendData(Common.SingleStartCmd(), mDevices.get(0).getAddress());
                        }
                    } else {
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M1");
                        Connection_Manager.shared.SendData(Common.SingleStartCmd(), mDevices.get(0).getAddress());
                    }
                }else if(Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null){

                    if(Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true){
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M2" );
                        Connection_Manager2.shared.SendData(Common.SingleStartCmd(),mDevices.get(0).getAddress(), null);
                    }

                }else  if(Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()) != null){

                    if(Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()).isConnected == true){
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M2" );
                        Connection_Manager2.shared.SendData(Common.SingleStartCmd(),mDevices.get(1).getAddress(), null);
                    }
                }
            }else if(Common.DEVICE_MAPS1.get(mDevices.get(1).getAddress()) != null) {
                if(Common.DEVICE_MAPS1.get(mDevices.get(1).getAddress()).isConnected == true){
                    if(Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null){
                        if(Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true){
                            Common.ISSingle = false;
                            Log.e(TAG, " Start send_start_cmd: DOUBLE From M1 & M2" );
//                            GraphFragment.series1.setTitle(mDevices.get(1).getTitle());
//                            GraphFragment.logFileName1 = "LOG_FILE_" + formatter.format(now) +"_"+ mDevices.get(1).getTitle().trim()+"_RIGHT.txt";
//                            GraphFragment.logFileName2 = "LOG_FILE_" + formatter.format(now) +"_final.txt";
                            Connection_Manager.shared.SendData(Common.SingleLeftStartCmd(),mDevices.get(1).getAddress());
                            Connection_Manager2.shared.SendData(Common.SingleRightStartCmd(),mDevices.get(0).getAddress(), null);
                        }else{
                            Common.ISSingle = true;
                            Log.e(TAG, " Start send_start_cmd: SINGLE From M1" );
                            Connection_Manager.shared.SendData(Common.SingleStartCmd(),mDevices.get(0).getAddress());
                        }
                    }else{
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M1" );
                        Connection_Manager.shared.SendData(Common.SingleStartCmd(),mDevices.get(1).getAddress());
                    }
                }
            } else if(Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null){

                if(Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true){
                    Common.ISSingle = true;
                    Log.e(TAG, " Start send_start_cmd: SINGLE From M2" );
                    Connection_Manager2.shared.SendData(Common.SingleStartCmd(),mDevices.get(0).getAddress(), null);
                }

            }else  if(Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()) != null){

                if(Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()).isConnected == true){
                    Common.ISSingle = true;
                    Log.e(TAG, " Start send_start_cmd: SINGLE From M2" );
                    Connection_Manager2.shared.SendData(Common.SingleStartCmd(),mDevices.get(1).getAddress(), null);
                }
            }
        }

//        GraphFragment.flag_save = 1;
//
//        GraphFragment.logFileName_map = "latlng_" + formatter.format(now) + ".txt";//like 2016_01_12.txt

    }

    public static void StopSession(){

        Common.IsStopped = true;
        Common.IsManallystopped = true;
        Common.isWaitingForStop = true;

        Common.leftStopped = true;

        Common.leftDisconnected = false;


        if(mDevices.size() == 1){
            if(Common.DEVICE_MAPS1.get(mDevices.get(0).getAddress()) != null){
                if(Common.DEVICE_MAPS1.get(mDevices.get(0).getAddress()).isConnected == true){
                    Common.ISSingle = true;
                    Log.e(TAG, " Stop send_start_cmd: SINGLE From M1" );
                    Connection_Manager.shared.SendData(sys,mDevices.get(0).getAddress());
                }
            }else if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null){
                if(Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true){
                    Common.ISSingle = true;
                    Log.e(TAG, " Stop send_start_cmd: SINGLE From M2" );
                    Connection_Manager2.shared.SendData(sys,mDevices.get(0).getAddress(), null);
                }
            }
        } else if (mDevices.size() > 1) {

            if (Common.DEVICE_MAPS1.get(mDevices.get(0).getAddress()) != null) {
                if (Common.DEVICE_MAPS1.get(mDevices.get(0).getAddress()).isConnected == true) {
                    if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()) != null) {
                        if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()).isConnected == true) {
                            Common.ISSingle = false;
                            Log.e(TAG, " Stop send_start_cmd: DOUBLE From M1 & M2");
                            Log.e(TAG, " Stop send_start_cmd: DOUBLE From M1 "+ mDevices.get(0).getAddress()+ "M2 " + mDevices.get(1).getAddress());
                            Connection_Manager.shared.SendData(sys, mDevices.get(0).getAddress());
                            Common.wait(100);
                            Connection_Manager2.shared.SendData(sys, mDevices.get(1).getAddress(), null);
                        } else {
                            Common.ISSingle = true;
                            Log.e(TAG, " Stop send_start_cmd: SINGLE From M1");
                            Connection_Manager.shared.SendData(sys, mDevices.get(0).getAddress());
                        }
                    } else {
                        Common.ISSingle = true;
                        Log.e(TAG, " Stop send_start_cmd: SINGLE From M1");
                        Connection_Manager.shared.SendData(sys, mDevices.get(0).getAddress());
                    }
                }else if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null) {

                    if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true) {
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M2");
                        Connection_Manager2.shared.SendData(sys, mDevices.get(0).getAddress(), null);
                    }

                } else if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()) != null) {

                    if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()).isConnected == true) {
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M2");
                        Connection_Manager2.shared.SendData(sys, mDevices.get(1).getAddress(), null);
                    }
                }
            } else if (Common.DEVICE_MAPS1.get(mDevices.get(1).getAddress()) != null) {
                if (Common.DEVICE_MAPS1.get(mDevices.get(1).getAddress()).isConnected == true) {
                    if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null) {
                        if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true) {
                            Common.ISSingle = false;
                            Log.e(TAG, " Stop send_start_cmd: DOUBLE From M1 "+ mDevices.get(1).getAddress()+ "M2 " + mDevices.get(0).getAddress());
                            Connection_Manager.shared.SendData(sys, mDevices.get(1).getAddress());
                            Common.wait(100);
                            Connection_Manager2.shared.SendData(sys, mDevices.get(0).getAddress(), null);
                        } else {
                            Common.ISSingle = true;
                            Log.e(TAG, " Stop send_start_cmd: SINGLE From M1");
                            Connection_Manager.shared.SendData(sys, mDevices.get(0).getAddress());
                        }
                    } else {
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M1");
                        Connection_Manager.shared.SendData(sys, mDevices.get(1).getAddress());
                    }
                }else if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null) {

                    if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true) {
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M2");
                        Connection_Manager2.shared.SendData(sys, mDevices.get(0).getAddress(), null);
                    }

                } else if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()) != null) {

                    if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()).isConnected == true) {
                        Common.ISSingle = true;
                        Log.e(TAG, " Start send_start_cmd: SINGLE From M2");
                        Connection_Manager2.shared.SendData(sys, mDevices.get(1).getAddress(), null);
                    }
                }
            } else if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()) != null) {

                if (Common.DEVICE_MAPS2.get(mDevices.get(0).getAddress()).isConnected == true) {
                    Common.ISSingle = true;
                    Log.e(TAG, " Start send_start_cmd: SINGLE From M2");
                    Connection_Manager2.shared.SendData(sys, mDevices.get(0).getAddress(), null);
                }

            } else if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()) != null) {

                if (Common.DEVICE_MAPS2.get(mDevices.get(1).getAddress()).isConnected == true) {
                    Common.ISSingle = true;
                    Log.e(TAG, " Start send_start_cmd: SINGLE From M2");
                    Connection_Manager2.shared.SendData(sys, mDevices.get(1).getAddress(), null);
                }
            }

        }

    }

    public void setCallbacks(MyBleCallbacks myBleCallbacks){
        MyBleCallbacks_ = myBleCallbacks;
    }

}
