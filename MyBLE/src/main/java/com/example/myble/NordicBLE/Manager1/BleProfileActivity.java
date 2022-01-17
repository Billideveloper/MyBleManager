package com.example.myble.NordicBLE.Manager1;


import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myble.NordicBLE.Common;
import com.example.myble.NordicBLE.DeviceBean;
import com.example.myble.NordicBLE.DeviceDetails;
import com.example.myble.NordicBLE.FileCache;
import com.example.myble.NordicBLE.MyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import no.nordicsemi.android.ble.BleManagerCallbacks;
import no.nordicsemi.android.log.ILogSession;

public abstract class BleProfileActivity extends AppCompatActivity implements BleManagerCallbacks, UARTManagerCallbacks {

    private static final String TAG = "BaseProfileActivity";

    private LoggableBleManager<? extends BleManagerCallbacks> bleManager;

    private BluetoothDevice reconectionDevice = null;

    private static Timer reconnection_timer = null;

    public BluetoothDevice prev_reconectionDevice = null;

    private FileCache mCache = FileCache.newInstance();

    private List<DeviceBean> mDevices;

    private ILogSession logSession;

    private Boolean isMangerinitilized = false;

    private int titleId = 0;

    public static Boolean isManuallyDisconnected = false;

    protected abstract LoggableBleManager<? extends BleManagerCallbacks> initializeManager();


    /**
     * Returns the title resource id that will be used to create logger session. If 0 is returned (default) logger will not be used.
     *
     * @return the title resource id
     */
    protected int getLoggerProfileTitle() {
        return 0;
    }

    /**
     * This method may return the local log content provider authority if local log sessions are supported.
     *
     * @return local log session content provider URI
     */
    protected Uri getLocalAuthorityLogger() {
        return null;
    }

    // For connection
    public void onDeviceSelected(@NonNull final BluetoothDevice device, final String name) {
        bleManager = initializeManager();
        bleManager.setGattCallbacks(BleProfileActivity.this);
        bleManager.connect(device)
                .useAutoConnect(false)
                .retry(3, 100)
                .enqueue();
    }

    // For Disconnection
    public void onDeviceDeselected(@NonNull final String device){
        bleManager = initializeManager();
        bleManager.setGattCallbacks(BleProfileActivity.this);
        if(bleManager.getBluetoothDevice() == null)return;
        if(bleManager.getBluetoothDevice().getAddress().equalsIgnoreCase(device)){
            bleManager.disconnect().enqueue();
        }
    }

    public void sendData(byte[] bytes, String address){
//        DeviceDetails deviceDetails = Common.DEVICE_MAPS1.get(address);
//        UARTManager.getInstance(MainActivity.shared()).send(bytes, deviceDetails.rxCharacteristic);
    }
    
    @Override
    public void onDeviceConnecting(@NonNull BluetoothDevice device) {
        Log.d(TAG, "onDeviceConnecting: ");
    }

    @Override
    public void onDeviceConnected(@NonNull BluetoothDevice device) {

        Log.d(TAG, "onDeviceConnected: "+ device.getAddress());

//        for(int i=0; i < reconectionReq.length;i++){
//            if(reconectionReq[i].equalsIgnoreCase(device.getAddress().toString())){
//                reconectionReq[i] = "";
//            }
//        }


       MyManager.MyBleCallbacks_.onDeviceConnected(device);


        Common.DEVICE_MAPS1 = new HashMap<>(1);

        prev_reconectionDevice = null;
        reconectionDevice = null;



        // Start CTS Server
        //BluetoothLeGattServer.getInstance(MainActivity.shared().getApplicationContext()).start();


       // Common.wait(50);

//        for(int i=0; i < previousConnectedReq.length;i++){
//            if(previousConnectedReq[i].equalsIgnoreCase(device.getAddress().toString())){
//                previousConnectedReq[i] = "";
//            }
//        }


//        if(FileCache.mDevices.size() == 0){
//            MainActivity.shared().addNewDevice(device.getName(), device.getAddress());
//            mDevices = mCache.getDevices();
//            mCache.writeToFile();
//        }


//        for(int i = 0 ; i < FileCache.mDevices.size(); i++){
//            if(FileCache.mDevices.get(i) != null){
//                if(FileCache.mDevices.get(i).getAddress().equals(device.getAddress())){
//                    // Allread in list
//                    Log.d(TAG, "onDeviceConnected: Allread stored in list");
//                }else{
//                    MainActivity.shared().addNewDevice(device.getName(), device.getAddress());
//                    mDevices = mCache.getDevices();
//                    mCache.writeToFile();
//                }
//            }
//        }
        
        //MainActivity.AddCoonected(device.getAddress(), device.getName());

        DeviceDetails deviceDetails;

        if(Common.DEVICE_MAPS1.get(device.getAddress()) != null){
            deviceDetails = Common.DEVICE_MAPS1.get(device.getAddress());
        }else{
            deviceDetails = new DeviceDetails();
        }
        deviceDetails.device = device;
        deviceDetails.index = 0;
        deviceDetails.isConnected = true;
        deviceDetails.isManuallDisconnected = false;

        if(mDevices != null){
            if(mDevices.size() != 0){
                if(mDevices.get(0) != null){
                    mDevices.get(0).setConnected(true);
                }
            }
        }


//        if(Common.isSessionStarted == false && Common.isReset == false){
//            Common.isReset = true;
//            byte[] reset = new byte[3];
//            reset[0] = 0x33;
//            reset[1] = 0x00;
//            reset[2] = 0x33;
//            sendData(reset,device.getAddress());
//        }

        Common.DEVICE_MAPS1.put(device.getAddress(), deviceDetails);

       // EventBus.getDefault().post(new StatusMessage(device.getAddress(), STATE_CONNECTED));
    }

    @Override
    public void onDeviceDisconnecting(@NonNull BluetoothDevice device) {
        Log.d(TAG, "onDeviceDisconnecting: ");
    }


    public void SendReset(byte[] ack, BluetoothDevice device){
        sendData(ack,device.getAddress());
    }

    @Override
    public void onDeviceDisconnected(@NonNull BluetoothDevice device) {

//      bleManager = null;
        Log.d(TAG, "onDeviceDisconnected: " + device.getAddress());

//        if(Common.DEVICE_MAPS1 != null){

        MyManager.MyBleCallbacks_.onDeviceDisConnected(device);


            Common.leftDisconnected = true;

            DeviceDetails deviceDetails = Common.DEVICE_MAPS1.get(device.getAddress().toString());
            if(deviceDetails.isManuallDisconnected == true){
                // if it is manually disconnected then no need to reconnect just update the device status
                deviceDetails.isConnected = false;
                deviceDetails.device = null;
                Common.DEVICE_MAPS1.clear();
            }else {
                // if it is not manually diconnected then reconnect it automatically
                deviceDetails.isConnected = false;
                //EventBus.getDefault().post(new StatusMessage(device.getAddress(), STATE_DISCONNECTED));
                checkConnectionHandler(device);
            }
            Common.DEVICE_MAPS1.put(device.getAddress().toString(), deviceDetails);



        if(mDevices != null){
            if(mDevices.size() != 0){
                if(mDevices.get(0) != null){
                    mDevices.get(0).setConnected(false);
                }
            }
        }
//        MainActivity.RemoveDiscoonected(device.getAddress(), device.getName());
//        EventBus.getDefault().post(new StatusMessage(device.getAddress(), STATE_DISCONNECTED));
    }

    @Override
    public void onLinkLossOccurred(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onServicesDiscovered(@NonNull BluetoothDevice device, boolean optionalServicesFound) {

    }

    @Override
    public void onDeviceReady(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onBondingRequired(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onBonded(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onBondingFailed(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onError(@NonNull BluetoothDevice device, @NonNull String message, int errorCode) {

    }

    @Override
    public void onDeviceNotSupported(@NonNull BluetoothDevice device) {

    }



    public void Disconnect(){
        onDeviceDeselected(bleManager.getBluetoothDevice().getAddress());
    }

    private void checkConnectionHandler(BluetoothDevice device){

        reconectionDevice = device;

        // if there is all_ready timer running then cancel and restart it
        if(reconnection_timer != null){
            reconnection_timer.cancel();
            reconnection_timer = null;
        }
        reconnection_timer = new Timer();
        reconnection_timer.schedule(new connectionUpdater(), 0, 2000);
    }


    class connectionUpdater extends TimerTask {
        @Override
        public void run() {
            // if there is no reconnection req then cancel loop
            // if there is reconnection req then connect device
                if(reconectionDevice != null){
                    bleManager = initializeManager();
                    bleManager.setGattCallbacks(BleProfileActivity.this);
                    onDeviceSelected(reconectionDevice, reconectionDevice.getName());
                }
        }
    }

}