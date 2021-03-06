package com.example.myble.NordicBLE.Manager1;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myble.NordicBLE.Common;
import com.example.myble.NordicBLE.DeviceDetails;

import java.util.UUID;

import no.nordicsemi.android.log.LogContract;

public class UARTManager extends LoggableBleManager<UARTManagerCallbacks> {

    /** Nordic UART Service UUID */
    private final static UUID UART_SERVICE_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    /** RX characteristic UUID */
    private final static UUID UART_RX_CHARACTERISTIC_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    /** TX characteristic UUID */
    private final static UUID UART_TX_CHARACTERISTIC_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");

    private BluetoothGattCharacteristic rxCharacteristic, txCharacteristic;
    /**
     * A flag indicating whether Long Write can be used. It's set to false if the UART RX
     * characteristic has only PROPERTY_WRITE_NO_RESPONSE property and no PROPERTY_WRITE.
     * If you set it to false here, it will never use Long Write.
     *
     * TODO change this flag if you don't want to use Long Write even with Write Request.
     */
    private boolean useLongWrite = true;

    UARTManager(final Context context) {
        super(context);
    }

    private static UARTManager managerInstance = null;

    public static synchronized UARTManager getInstance(final Context context) {
        if (managerInstance == null) {
            managerInstance = new UARTManager(context);
        }
        return managerInstance;
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return new UARTManagerGattCallback();
    }

    /**
     * BluetoothGatt callbacks for connection/disconnection, service discovery,
     * receiving indication, etc.
     */
    private class UARTManagerGattCallback extends BleManagerGattCallback {

        @Override
        protected void initialize() {
            setNotificationCallback(txCharacteristic)
                    .with((device, data) -> {
                        final String text = data.getStringValue(0);
                        log(LogContract.Log.Level.APPLICATION, "\"" + text + "\" received");
                        mCallbacks.onDataReceived(device, data.getValue());
                    });
            requestMtu(260).enqueue();
            enableNotifications(txCharacteristic).enqueue();
        }

        @Override
        public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(UART_SERVICE_UUID);

//            service.addService(TimeProfile.createTimeService());

            Common.wait(100);
//            BluetoothLeGattServer.getInstance(MainActivity.shared().getApplicationContext()).start();
            Common.wait(100);
            Log.e("TAG", "broadcastUpdate: Time added 1" );

            if (service != null) {
                rxCharacteristic = service.getCharacteristic(UART_RX_CHARACTERISTIC_UUID);
                txCharacteristic = service.getCharacteristic(UART_TX_CHARACTERISTIC_UUID);
            }

            boolean writeRequest = false;
            boolean writeCommand = false;
            if (rxCharacteristic != null) {

                DeviceDetails deviceDetails;
                if(Common.DEVICE_MAPS.get(gatt.getDevice().getAddress()) != null){
                    deviceDetails = Common.DEVICE_MAPS.get(gatt.getDevice().getAddress());
                }else{
                    deviceDetails = new DeviceDetails();
                }
                deviceDetails.rxCharacteristic = rxCharacteristic;
                deviceDetails.txCharacteristic = txCharacteristic;

                Common.DEVICE_MAPS.put(gatt.getDevice().getAddress(), deviceDetails);

                final int rxProperties = rxCharacteristic.getProperties();
                writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
                writeCommand = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0;

                // Set the WRITE REQUEST type when the characteristic supports it.
                // This will allow to send long write (also if the characteristic support it).
                // In case there is no WRITE REQUEST property, this manager will divide texts
                // longer then MTU-3 bytes into up to MTU-3 bytes chunks.
                if (writeRequest)
                    rxCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                else
                    useLongWrite = false;
            }

            return rxCharacteristic != null && txCharacteristic != null && (writeRequest || writeCommand);
        }

        @Override
        protected void onDeviceDisconnected() {
            //rxCharacteristic = null;
            //txCharacteristic = null;
            useLongWrite = true;
        }


        @Override
        protected void onServicesInvalidated() {

        }
    }

    // This has been moved to the service in BleManager v2.0.
	/*@Override
	protected boolean shouldAutoConnect() {
		// We want the connection to be kept
		return true;
	}*/

    /**
     * Sends the given text to RX characteristic.
//     * @param text the text to be sent
     */
//    public void send(final String text) {
//        // Are we connected?
//        if (rxCharacteristic == null)
//            return;
//
//        if (!TextUtils.isEmpty(text)) {
//            final WriteRequest request = writeCharacteristic(rxCharacteristic, text.getBytes())
//                    .with((device, data) -> log(LogContract.Log.Level.APPLICATION,
//                            "\"" + data.getStringValue(0) + "\" sent"));
//            if (!useLongWrite) {
//                // This will automatically split the long data into MTU-3-byte long packets.
//                request.split();
//            }
//            request.enqueue();
//        }
//    }



    public void send(final byte[] bytes, BluetoothGattCharacteristic RxCharacteristic) {

//        writeCharacteristic(rxCharacteristic, bytes)
//                .done(device ->
//                        log(Log.INFO, "Done"))
//                .fail((device, status) ->
//                        log(Log.WARN, "Failed " + status))
//                .enqueue();
        writeCharacteristic(rxCharacteristic,bytes,BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE).done(device ->
                Log.e("Connection_Manager", "broadcastUpdate: PktTime Ack sent Sucessfully" )
        ).fail((device, status) ->
                Log.d("UARTmanager", "send: UART Failedto send")
        ).enqueue();

//            final WriteRequest request = writeCharacteristic(rxCharacteristic, bytes)
//                    .with((device, data) -> log(LogContract.Log.Level.APPLICATION,
//                            "\"" + data.getStringValue(0) + "\" sent"));
////            if (!useLongWrite) {
////                // This will automatically split the long data into MTU-3-byte long packets.
////                request.split();
////            }
//            request.enqueue();
    }
}

