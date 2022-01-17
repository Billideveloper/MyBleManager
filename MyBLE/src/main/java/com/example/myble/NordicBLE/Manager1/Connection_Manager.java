package com.example.myble.NordicBLE.Manager1;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;


public class Connection_Manager extends BleProfileActivity {

    public String TAG = "Connection_Manager";

    int[] header = {0, 0, 0, 0};

    byte[] ack = new byte[5];

    private static Timer prev_reconnection_timer = null;

    public static Connection_Manager shared = new Connection_Manager();

    public static Boolean isStarte = false;

//    private MainActivity mainActivity = MainActivity.shared();

    public void Connect(BluetoothDevice bluetoothDevice){
        onDeviceSelected(bluetoothDevice, bluetoothDevice.getName());
    }

    public void Disconnect(String address){
        onDeviceDeselected(address);
    }



    public void SendData(byte[] ack , String deviceaddress){
        sendData(ack, deviceaddress);
     }


    @Override
    protected LoggableBleManager initializeManager() {
        final UARTManager manager = UARTManager.getInstance(this);
//        manager.setGattCallbacks(this);
        return manager;
    }


    @Override
    public void onDataReceived(@NonNull BluetoothDevice device, byte[] data) {
        Log.d(TAG, "onDataReceived: is " + data );
        if(data != null){
           // broadcastUpdate(device, data);
        }
    }

    @Override
    public void onDataSent(@NonNull BluetoothDevice device, byte[] data) {
        Log.d(TAG, "onDataSent: ");
    }

//    public void showAlert(){
//        Utilities.showDataTarnsferCompleteAlert(mainActivity);
//    }


//    private void broadcastUpdate(BluetoothDevice device , byte[] data){
//
//        PDR_Data pdr_data = null;
//
//        byte[] buffer = data;
//
//        Common.LEFTDEVAdd = device.getAddress();
//
//        if(buffer == null) return;
//
//        if(buffer.length == 0){
//            return;
//        }
//
//
//        /////////////// PARSING OTHER ACTIONS ------------------------------------------------------------------------------------------
//
//        if(buffer[0] == (byte) 0xA0 && buffer[1] == (byte) 0x06){
//            mainActivity.bReceivedAck = true;
//            mainActivity.onRcvStartStopEvent(new StartStopAck(true, device.getAddress()));
//
//            isStarte = false;
//            Log.i(TAG,"["+device.getAddress()+"] Left-Foot: Start Ack "+Utilities.convertByteArrToStr(buffer, true));
//            return;
//        }else if (buffer[0] == (byte) 0xA0 && buffer[1] == (byte) 0x22){
//            mainActivity.bReceivedAck = false;
//            Common.Dev1IsStopped = true;
//            mainActivity.onRcvStartStopEvent(new StartStopAck(false, device.getAddress()));
//            Log.i(TAG,"["+device.getAddress()+"] Left-Foot: Stop Ack "+Utilities.convertByteArrToStr(buffer, true));
//            return;
//        } else if(buffer[0] == (byte) 0x25){
//            // Received total pkt info
//            // 25 p1 p2 cs1 cs2
//
//            Log.d(TAG, "SendData: Sending reset got pkt num 0x25 Left");
//
//            int b1 = buffer[1] & 0xFF;
//            int b2 = buffer[2] & 0xFF;
//            int package_number = b1 * 256 + b2;
//            //set as a last pkt
//            Common.Dev1lastPkt = package_number;
//            Log.e(TAG, "broadcastUpdate: Last pkt count is " +package_number );
//        }
//        else if (buffer.length == 4 && buffer[0] == (byte)0xA0 && buffer[3] == (byte)0xF0) {
//            mainActivity.bReceivedAck = true;
//            mainActivity.onRcvStartStopEvent(new StartStopAck(true, device.getAddress()));
//            Log.i(TAG,"["+device.getAddress()+"] Left-Foot: Start Ack "+Utilities.convertByteArrToStr(buffer, true));
//            return;
//        }else if (buffer.length == 4 && buffer[0] == (byte)0xA0 && buffer[3] == (byte)0xC2){
//            mainActivity.bReceivedAck = false;
//            mainActivity.onRcvStartStopEvent(new StartStopAck(false, device.getAddress()));
//            Log.i(TAG,"["+device.getAddress()+"] Left-Foot: Stop Ack "+Utilities.convertByteArrToStr(buffer, true));
//            return;
//        }else if (buffer.length == 4 && buffer[0] == (byte)0xA0 && buffer[1] == (byte)0x51){//0xA0 0x51 0x00  0xF1
//            mainActivity.rcvd_expiry_ack(device.getAddress());
//            Log.i(TAG,"["+device.getAddress()+"] Left-Foot: Expiry Ack "+Utilities.convertByteArrToStr(buffer, true));
//            return;
//        }else if (buffer[0] == (byte)0xEE){
//            // if too long disconnected show alert to Notify user
//            Utilities.showDisconnectionAlert(mainActivity);
//
//        } else if (buffer[0] == (byte)0xEA){ // changed by ravi
//            // Stop Activity
//            if(Common.RIGHTDEVAdd != null){
//                // Sending stop to Right device
//                Connection_Manager2.shared.SendData(mainActivity.sys,Common.RIGHTDEVAdd,null);
//            }
//            mainActivity.FAB.setBackgroundColor(mainActivity.fab_color);
//            mainActivity.FAB.setImageResource(R.drawable.ic_play);
//            Common.SetTransferCompleted();
//            Utilities.showDataTarnsferCompleteAlert(mainActivity);
//        }
//
//
//
//        /////////////////// Parsing pkt -----------------------------------------------------------------------------------------------------
//
//        if( buffer[0] == (byte)0xAA){
//
//            int i = 0;
//            int j;
//
//            for (j = 0; j < 3; j++) {
//                header[j] = buffer[i++] & 0xFF;
//            }
//
//            int package_number_1 = header[1];
//            int package_number_2 = header[2];
//
//            int package_number = package_number_1 * 256 + package_number_2;
//
//            Common.isSingle = true;
//
//
//            byte[] reset = new byte[3];
//            reset[0] = 0x33;
//            reset[1] = 0x00;
//            reset[2] = 0x33;
//
//            Log.e(TAG, "broadcastUpdate: PktTime Left Received pkt no is + " + package_number );
//
//
//            if(Common.isSingle != null){
//
//                if(Common.Dev1lastPkt != null){
//                    Log.e(TAG, "process_pdr_data: PPP got last pkt num ");
//                    if(Common.IsStopped == true){
//                        Log.e(TAG, "process_pdr_data: is stopped" );
//
//                        if(package_number == Common.Dev1lastPkt){
//
//                            // S1
//                            Common.Dev1TransferCompleted = true;
//                            ack = createAck(ack, package_number_1, package_number_2);
//
//                            if(Common.isSingle == true){
//                                SendData(ack, device.getAddress());
//                                Common.wait(100);
//                                SendData(reset, device.getAddress());
//                                Log.d(TAG, "SendData: Sending reset for S1" + device.getAddress());
//
//                            }else if(Common.isSingle == false){
//                                Connection_Manager2.shared.SendData(ack, Common.RIGHTDEVAdd, null);
//                                Common.wait(100);
//                                SendData(reset, device.getAddress());
//                                Log.d(TAG, "SendData: Sending reset for S1" + device.getAddress());
//                                Connection_Manager2.shared.SendData(reset, Common.RIGHTDEVAdd, null);
//                                Log.d(TAG, "SendData: Sending reset for S1" + Common.RIGHTDEVAdd);
//
//                            }
//                            Common.IsDataComming = false;
//
//                            pdr_data = new PDR_Data(device.getAddress(),"PDR", data);
//
//                            if(pdr_data != null){
//                                EventBus.getDefault().post(pdr_data);
//                            }
//
//                            mainActivity.UpdateUI(false);
////                                mainActivity.FAB.setBackgroundColor(mainActivity.fab_color);
////                                mainActivity.FAB.setImageResource(R.drawable.ic_play);
//                            return;
//                        }
//
//                        if(package_number < Common.Dev1lastPkt){
//                            //S2
//
//                            Log.d(TAG, "SendData: Sending r stop for S2" + device.getAddress());
//
//                            Log.e(TAG, "process_pdr_data: Transfer not complete" );
//                            mainActivity.FAB.setBackgroundColor(Color.DKGRAY);
//                            mainActivity.UpdateUI(true);
//                            Common.Dev1TransferCompleted = false;
//                        }else{
//                            //S3
//                            Log.e(TAG, "process_pdr_data: Transfer Complete" );
//                            Common.Dev1TransferCompleted = true;
//                            Common.IsDataComming = false;
//                            SendData(reset, device.getAddress());
//                            Log.d(TAG, "SendData: Sending reset for S3" + device.getAddress());
//                            Connection_Manager2.shared.SendData(reset, Common.RIGHTDEVAdd, null);
//                            Log.d(TAG, "SendData: Sending reset for S3" + Common.RIGHTDEVAdd);
//
//                            mainActivity.UpdateUI(false);
////
////                                mainActivity.FAB.setBackgroundColor(mainActivity.fab_color);
////                                mainActivity.FAB.setImageResource(R.drawable.ic_play);
//
//                            if(Common.Dev1TransferCompleted == true){
//                                mainActivity.FAB.setBackgroundColor(mainActivity.fab_color);
//                                mainActivity.FAB.setImageResource(R.drawable.ic_play);
//                                return;
//                            }
//                        }
//                    }
//                }
//
//                if(Common.isSingle == true){
//                    Common.IsDataComming = true;
//                    //mainActivity.UpdateUI(true);
//                    ack = createAck(ack, package_number_1,package_number_2);
//                    SendData(ack, device.getAddress());
//                }
//                //Common.IsDataComming = true;
//
//                Common.isRunning = true;
//
//                if(Common.IsStopped == false){
//                    mainActivity.FAB.setImageResource(R.drawable.ic_stop2) ;
//                }
//            }
//
////            ack = createAck(ack, package_number_1, package_number_2);
////
////            SendData(ack,device.getAddress());
//        }
//
//
//        if( buffer[0] == (byte)0xBB){
//
//
//            Log.d(TAG, "SendData: Sending reset got 0xBB");
//
//            int pktchunk;
//
//            pktchunk = buffer[1] & 0xFF;
//
//            int i = 2;
//
//            byte[] de = buffer;
//
//            for (int j = 2; j < buffer.length; j++) {
//                de[j-2] = buffer[j];
//            }
//
//            if(pktchunk != 0){
//
//                byte[][] split_pkts = Common.splitArray(de, Common.PKT_SIZE);
//
//                for (int j = 0; j < split_pkts.length; j++) {
//
//                    if(split_pkts[j].length == Common.PKT_SIZE){
//
//                        if(pktchunk - 1 == j){
//
//                            Log.d(TAG, "broadcastUpdate: pkt ack got last pkt");
//
//                            buffer = split_pkts[j];
//
//                            for (int k = 0; k < 3; k++) {
//                                header[k] = buffer[k] & 0xFF;
//                            }
//
//                            int package_number_1 = header[1];
//                            int package_number_2 = header[2];
//
//                            ack = createAck(ack, package_number_1,package_number_2);
//
//                            SendData(ack, device.getAddress());
//
//                        }
//
//
//                        data = split_pkts[j];
//
//                        pdr_data = new PDR_Data(device.getAddress(),"PDR", data);
//
//                        if(pdr_data != null){
//                            EventBus.getDefault().post(pdr_data);
//                        }
//
//
//
//
//                    }
//
////                    if(j == pktchunk - 1){
////
////                        i = 0;
////
////                        buffer = split_pkts[j];
////
////                        for (j = 0; j < 3; j++) {
////                            header[j] = buffer[j] & 0xFF;
////                        }
////
////                        int package_number_1 = header[1];
////                        int package_number_2 = header[2];
////
////                        ack = createAck(ack, package_number_1,package_number_2);
////
////                        SendData(ack, device.getAddress());
////                    }
//
//                    // post Data
//
////                    data = split_pkts[j];
////
////                    pdr_data = new PDR_Data(device.getAddress(),"PDR", data);
////
////                    if(pdr_data != null){
////                        EventBus.getDefault().post(pdr_data);
////                    }
////
////                    return;
//
//                }
//
//            }
//        }
//
//        ////////////////// posting Data ----------------------------------------------------------------------------------------------------
//
//
//        pdr_data = new PDR_Data(device.getAddress(),"PDR", data);
//
//        if(pdr_data != null){
//            EventBus.getDefault().post(pdr_data);
//        }
//
//    }


    ////////////////////////////// Ack Generator ---------------------------------------------------------------------------------------------

    public byte[] createAck(byte[] ack, int package_number_1, int package_number_2) {
        ack[0] = 0x01;
        ack[1] = (byte) package_number_1;
        ack[2] = (byte) package_number_2;
        ack[3] = (byte) ((1 + package_number_1 + package_number_2 - (1 + package_number_1 + package_number_2) % 256) / 256);
        ack[4] = (byte) ((1 + package_number_1 + package_number_2) % 256);
        return ack;
    }


    //////////////////////////////// Reconnector ---------------------------------------------------------------------------------------------


    public void Connect_prev_device(BluetoothDevice device){

        prev_reconectionDevice = device;

        // if there is all_ready timer running then cancel and restart it
        if(prev_reconnection_timer != null){
            prev_reconnection_timer.cancel();
            prev_reconnection_timer = null;
        }
        prev_reconnection_timer = new Timer();
        prev_reconnection_timer.schedule(new Prev_connectionUpdater(), 0, 2000);
    }

    class Prev_connectionUpdater extends TimerTask {
        @Override
        public void run() {
            // if there is no reconnection req then cancel loop
            if(prev_reconectionDevice == null ){
                if(prev_reconnection_timer != null) prev_reconnection_timer.cancel();
                return;
            }
            // if there is reconnection req then connect device
                if(prev_reconectionDevice != null){
                    Connect(prev_reconectionDevice);
                }
        }
    }


}
