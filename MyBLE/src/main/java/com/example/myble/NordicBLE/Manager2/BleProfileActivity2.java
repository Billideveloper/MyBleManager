package com.example.myble.NordicBLE.Manager2;


import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myble.NordicBLE.Common;
import com.example.myble.NordicBLE.DeviceBean;
import com.example.myble.NordicBLE.DeviceDetails;
import com.example.myble.NordicBLE.FileCache;
import com.example.myble.NordicBLE.Manager1.Connection_Manager;
import com.example.myble.NordicBLE.MyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import no.nordicsemi.android.ble.BleManagerCallbacks;
import no.nordicsemi.android.log.ILogSession;

public abstract class BleProfileActivity2 extends AppCompatActivity implements BleManagerCallbacks, UARTManagerCallbacks2 {

    private static final String TAG = "BaseProfileActivity2";

    private LoggableBleManager2<? extends BleManagerCallbacks> bleManager;

    private BluetoothDevice reconectionDevice = null;

    public BluetoothDevice prev_reconectionDevice2 = null;

    private static Timer reconnection_timer = null;

    private FileCache mCache = FileCache.newInstance();

    private List<DeviceBean> mDevices;

    private ILogSession logSession;

    private Boolean isMangerinitilized = false;

    private int titleId = 0;

    protected abstract LoggableBleManager2<? extends BleManagerCallbacks> initializeManager();

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
        bleManager.setGattCallbacks(BleProfileActivity2.this);
        bleManager.connect(device)
                .useAutoConnect(false)
                .retry(3, 100)
                .enqueue();
    }


    // For Disconnection
    public void onDeviceDeselected(@NonNull final String device){
        bleManager = initializeManager();
        bleManager.setGattCallbacks(BleProfileActivity2.this);
        if(bleManager.getBluetoothDevice() == null)return;
        if(bleManager.getBluetoothDevice().getAddress().equalsIgnoreCase(device)){
            bleManager.disconnect().enqueue();
        }
    }

    public void sendData(byte[] bytes, String address, String pktNo){
        Log.e(TAG, "broadcastUpdate: PktTimeR Right sent pkt no is + " + pktNo );
//        DeviceDetails deviceDetails = Common.DEVICE_MAPS2.get(address);
//        UARTManager2.getInstance(MainActivity.shared()).send(bytes, deviceDetails.rxCharacteristic);
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

        Common.DEVICE_MAPS2 = new HashMap<>(1);

        reconectionDevice = null;

        prev_reconectionDevice2 = null;

        // Start CTS Server
       // BluetoothLeGattServer.getInstance(MainActivity.shared().getApplicationContext()).start();

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
//
//
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

       // MainActivity.AddCoonected(device.getAddress(), device.getName());

        DeviceDetails deviceDetails;

        if(Common.DEVICE_MAPS2.get(device.getAddress()) != null){
            deviceDetails = Common.DEVICE_MAPS2.get(device.getAddress());
        }else{
            deviceDetails = new DeviceDetails();
        }
        deviceDetails.device = device;
        deviceDetails.index = 1;
        deviceDetails.isConnected = true;
        deviceDetails.isManuallDisconnected = false;

        if(mDevices != null){
            if(mDevices.size() != 0){
                if(mDevices.get(1) != null){
                    mDevices.get(1).setConnected(true);
                }
            }
        }

        Common.DEVICE_MAPS2.put(device.getAddress(), deviceDetails);
        //EventBus.getDefault().post(new StatusMessage(device.getAddress(), STATE_CONNECTED));
    }

    @Override
    public void onDeviceDisconnecting(@NonNull BluetoothDevice device) {
        Log.d(TAG, "onDeviceDisconnecting: ");
    }

    @Override
    public void onDeviceDisconnected(@NonNull BluetoothDevice device) {

        Log.d(TAG, "onDeviceDisconnected: " + device.getAddress());

//        bleManager = null;

        MyManager.MyBleCallbacks_.onDeviceConnected(device);


//        ///////////////////////////////////////////////////////////////////////////////////////////// Removing Bond
//        Method method = null;
//        try {
//            method = bleManager.getBluetoothDevice().getClass().getMethod("removeBond", (Class[]) null);
//            boolean result = (boolean) method.invoke(device, (Object[]) null);
//            if (result) {
//                Log.i(TAG, "Successfully removed bond");
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        DeviceDetails deviceDetails = Common.DEVICE_MAPS2.get(device.getAddress().toString());
        if(deviceDetails.isManuallDisconnected == true){
            // if it is manually disconnected then no need to reconnect just update the device status
            deviceDetails.isConnected = false;
            deviceDetails.device = null;
            Common.DEVICE_MAPS2.clear();
        }else {
            // if it is not manually diconnected then reconnect it automatically
            deviceDetails.isConnected = false;
            checkConnectionHandler(device);
        }

        if(mDevices != null){
            if(mDevices.size() != 0 && mDevices.size() > 1){
                if(mDevices.get(1) != null){
                    mDevices.get(1).setConnected(false);
                }
            }
        }


        Common.DEVICE_MAPS2.put(device.getAddress().toString(), deviceDetails);
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


    public void CancelReconnectio(){
        if(reconnection_timer != null){
            reconnection_timer.cancel();
            reconnection_timer = null;
        }
    }


    public void StartReconnection(){
        checkConnectionHandler(reconectionDevice);
    }


    public void Disconnect(){
        onDeviceDeselected(bleManager.getBluetoothDevice().getAddress());
    }

    private void checkConnectionHandler(BluetoothDevice device){
//        if(reconectionReq[0] == ""){
//            reconectionReq[0] = Address;
//        }else{
//            reconectionReq[1] = Address;
//        }
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

            // if there is reconnection req then connect device
                if(reconectionDevice != null){
                    bleManager = initializeManager();
                    bleManager.setGattCallbacks(BleProfileActivity2.this);
                    if(FileCache.mDevices.size() == 2){
                        onDeviceSelected(reconectionDevice, reconectionDevice.getName());
                    }else if(FileCache.mDevices.size() == 1){
                        Connection_Manager.shared.Connect(reconectionDevice);
                    }
                }
        }
    }






}
