package com.example.myble.NordicBLE.ScanManager;


import androidx.annotation.NonNull;

import com.example.myble.NordicBLE.MyManager;
import com.example.myble.NordicBLE.Utilities;

import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

public class Ble_Scanner {


    private static final String TAG = "Ble_Scanner";


    public static Ble_Scanner shared = new Ble_Scanner();

    BluetoothLeScannerCompat bluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner();

    // USER ACCESSABLE METHODS FOR START AND STOP SCAN

    public void StartScan(){
        bluetoothLeScannerCompat.stopScan(scanCallback);
        bluetoothLeScannerCompat.startScan(scanCallback);
    }

    public void StopScan(){
        bluetoothLeScannerCompat.stopScan(scanCallback);
    }

    /** CALLBACKS FOR SCAN
     * scan results will be provided here with device id , name and wil be posted from here to @DeviceScanActivity
     * and UI will be update with the help of
     * EventBus */

    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, @NonNull ScanResult result) {
            super.onScanResult(callbackType, result);
            // CHECKING IT IS PREDEFINED OR NOT

            MyManager.MyBleCallbacks_.onScanResults(result.getDevice());

            if(Utilities.PREDEFINED_MAC_IDS.contains(result.getDevice().getAddress())){
                // FOUND DEFINED DEVICE_ID NOW POST RESULTS FOR UI UPDATE

//                EventBus.getDefault().post(new ScanResults(result));

               // Log.d(TAG, "onScanResult: the scann results is + " + result.getDevice().getAddress());
            }
        }

        @Override
        public void onBatchScanResults(@NonNull List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {

            super.onScanFailed(errorCode);
        }
    };

}
