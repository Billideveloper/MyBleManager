package com.example.myble.NordicBLE.Manager2;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;


public class Connection_Manager2 extends BleProfileActivity2 {

    public static Connection_Manager2 shared = new Connection_Manager2();

    public String TAG = "Connection_Manager2";

    int[] header = {0, 0, 0, 0};

    byte[] ack = new byte[5];

    private static Timer prev_reconnection_timer = null;

    private static Timer ui_update_timer = null;


    public static Boolean isStarte = false;

//    private MainActivity mainActivity = MainActivity.shared();

    public void Connect(BluetoothDevice bluetoothDevice){
        onDeviceSelected(bluetoothDevice, bluetoothDevice.getName());
    }

    public void Disconnect(String address){
        onDeviceDeselected(address);
    }


    public void SendData(byte[] ack , String deviceaddress,String pktno){
        sendData(ack, deviceaddress,pktno);
    }


    @Override
    protected LoggableBleManager2 initializeManager() {
        final UARTManager2 manager = UARTManager2.getInstance(this);
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

    }


//    private void broadcastUpdate(BluetoothDevice device , byte[] data){
//
//        PDR_Data pdr_data = null;
//
//        Common.RIGHTDEVAdd = device.getAddress();
//
//        byte[] buffer = data;
//
//
//        if (buffer == null) {
//           return;
//        }
//
//
//        if(buffer.length == 0){
//            // send last ack
//            Log.d(TAG, "SendData: Sending reset last ack ");
//            SendData(ack, device.getAddress(),null);
//            return;
//        }
//
//        /////////////// PARSING OTHER ACTIONS ----------------------------------------------------------------------
//
//        if(buffer[0] == (byte) 0xA0 && buffer[1] == (byte) 0x06){
//            mainActivity.bReceivedAck1 = true;
//            mainActivity.onRcvStartStopEvent(new StartStopAck(true, device.getAddress()));
//            Log.i(TAG,"["+device.getAddress()+"] Right-Foot: Start Ack "+ Utilities.convertByteArrToStr(buffer, true));
//            isStarte = false;
//            return;
//        }else if (buffer[0] == (byte) 0xA0 && buffer[1] == (byte) 0x22){
//            Common.Dev2IsStopped = true;
//            mainActivity.bReceivedAck1 = false;
//            if(Common.IsTrannsferCompleted){
//                // if transfer is completed and then received the stop for right dev then update the UI
//                if(Common.IsManallystopped == false){
//                    Common.IsAutoStopped = true;
//                    // if it is not manually disconnected then on receiving stop from right device it will assume stop and will stop from both device and will update UI as Stopped
//                    Common.Dev1IsStopped = true;
//                    mainActivity.bReceivedAck = false;
//                    if(Common.LEFTDEVAdd != null){
//                        mainActivity.onRcvStartStopEvent(new StartStopAck(false, Common.LEFTDEVAdd));
//                    }
//                }
//            }
//            mainActivity.onRcvStartStopEvent(new StartStopAck(false, device.getAddress()));
//            Log.i(TAG,"["+device.getAddress()+"] Right-Foot: Stop Ack "+Utilities.convertByteArrToStr(buffer, true));
//            mainActivity.packetcounter = 0;
//            return;
//        } else if(buffer[0] == (byte) 0x25){
//            // Received total pkt info
//            // 25 p1 p2 cs1 cs2
//            int b1 = buffer[1] & 0xFF;
//            int b2 = buffer[2] & 0xFF;
//            int package_number = b1 * 256 + b2;
//            //set as a last pkt
//            Common.Dev2lastPkt = package_number;
//
//            Log.d(TAG, "SendData: Sending reset got pkt num 0x25  RIght");
//
//
//            Log.e(TAG, "broadcastUpdate: Last pkt count is " +package_number );
//        }
//        else if (buffer.length == 4 && buffer[0] == (byte)0xA0 && buffer[3] == (byte)0xF0) {
//            mainActivity.bReceivedAck1 = true;
//            mainActivity.onRcvStartStopEvent(new StartStopAck(true, device.getAddress()));
//            Log.i(TAG,"["+device.getAddress()+"] Right-Foot: Start Ack "+Utilities.convertByteArrToStr(buffer, true));
//            return;
//        }else if (buffer.length == 4 && buffer[0] == (byte)0xA0 && buffer[3] == (byte)0xC2){
//            mainActivity.bReceivedAck1 = false;
//            mainActivity.onRcvStartStopEvent(new StartStopAck(false, device.getAddress()));
//            Log.i(TAG,"["+device.getAddress()+"] Right-Foot: Stop Ack "+Utilities.convertByteArrToStr(buffer, true));
//            mainActivity.packetcounter = 0;
//            return;
//        }else if (buffer.length == 4 && buffer[0] == (byte)0xA0 && buffer[1] == (byte)0x51){//0xA0 0x51 0x00  0xF1
//            mainActivity.rcvd_expiry_ack(device.getAddress());
//            Log.i(TAG,"["+device.getAddress()+"] Right-Foot: Expiry Ack "+Utilities.convertByteArrToStr(buffer, true));
//            return;
//        }else if (buffer[0] == (byte)0xEE){
//            // too long disconnected show alert to Notify user
//            Utilities.showDisconnectionAlert(mainActivity);
//        }else if (buffer[0] == (byte)0xEA){
//            // Stop Activity
//            Common.IsTrannsferCompleted = true;
//            SendData(MainActivity.shared().sys, device.getAddress(),null);
//            // Data is transfered completely - update UI
//            mainActivity.FAB.setBackgroundColor(mainActivity.fab_color);
//            mainActivity.FAB.setImageResource(R.drawable.ic_play);
//            Common.SetTransferCompleted(); // so user can start new session
//            Utilities.showDataTarnsferCompleteAlert(mainActivity);
//        }
//
//
//        /////////////////// Parsing pkt -----------------------------------------------------------------------------------------------------
//
//        // if received bit starts with 0xAA then send received pkt ack
//
//        if( buffer[0] == (byte)0xAA){
//
//            UI_update();
//
//            int i = 0;
//            int j;
//
//            for (j = 0; j < 3; j++) {
//                header[j] = buffer[i++] & 0xFF;
//            }
//
//            Common.isSingle = false;
//
//            Common.IsDataComming = true;
//
//            int package_number_1 = header[1];
//            int package_number_2 = header[2];
//
//            // mainActivity.FAB.setImageResource(R.drawable.ic_stop2) ;
//
//            int package_number = package_number_1 * 256 + package_number_2;
//
//            //WritetoLogFile(""+package_number);
//
//            Log.e(TAG, "broadcastUpdate: PktTimeR Right Received pkt no is + " + package_number );
//
//
//            // Check last pkt received or not
//
//            byte[] reset = new byte[3];
//            reset[0] = 0x33;
//            reset[1] = 0x00;
//            reset[2] = 0x33;
//
//
//            // CASE1 :: - when stop ack not received
//            if(Common.isWaitingForStop){
//                if(Common.leftStopWait == 0){
////                    Common.leftStopWait = 25;
//                    if(Common.leftStopped == true && Common.leftDisconnected == true){
//                        Log.d(TAG, "SendData: Sending reset CASE 1 Did not got left stop Ack ");
//                        // Dismiss stop view -
//                        mainActivity.bReceivedAck = false;
//                        Common.Dev1IsStopped = true;
//                        mainActivity.onRcvStartStopEvent(new StartStopAck(false, Common.LEFTDEVAdd));
//                    }
//                }else{
//                    Common.leftStopWait--;
//                }
//            }
//
//            // CASE2 :: - when left last pkt not received from left (left last pkt >= Right last pkt)
//
//            if(Common.IsStopped == true){
//                if(Common.Dev2lastPkt != null && Common.Dev1lastPkt == null ){
//                    if(Common.ackWait == 0){
//                        Common.ackWait = 5;
//                        Log.d(TAG, "SendData: Sending r CASE 2 Did not got left pkt " + device.getAddress());
//                        Common.Dev1lastPkt = Common.Dev2lastPkt;
//                    }else{
//                        Common.ackWait--;
//                    }
//                }
//            }
//
//
//            // CASE3 :: - when left last pkt not received and Received pkt is near to Right Last pkt
//
//            if(Common.IsStopped == true){
//                if(Common.Dev1lastPkt != null && Common.Dev2lastPkt != null){
//
//                    // if it's Getting pkt no repeatedly 10 times then Reset the Device and update Session Completed status
//
//                        if(Common.Dev2lastPkt - 1 == package_number || Common.Dev2lastPkt - 2 == package_number
//                                || Common.Dev2lastPkt - 3 == package_number || Common.Dev2lastPkt - 4 == package_number
//                                || Common.Dev2lastPkt - 5 == package_number  ){
//
//                            // Now Limit loops and update status
//                            if(Common.pktloopCount == 0){
//
//                                Common.pktloopCount = 25;
//
//                                // Reset Device ::
//
//                                Log.d(TAG, "SendData: Sending reset for CASE 3" + device.getAddress());
//
//                                Connection_Manager.shared.SendData(ack, Common.LEFTDEVAdd);
//
//                                Log.d(TAG, "SendData: Sending reset for CASE 3 " + Common.LEFTDEVAdd);
//
//                                sendData(reset, device.getAddress(), null);
//
//
//                                // Reset App ::
//
//                                mainActivity.UpdateUI(false);
//
//                                Common.isComplete = true;
//
//                                Common.isRunning = false;
//
//                                Common.IsDataComming = false;
//
//                            }else{
//                                Common.pktloopCount--;
//                            }
//                        }else{
//                            if(Common.lastPktno != null){
//
//                                if(Common.lastPktno == Common.Dev2lastPkt || Common.lastPktno == Common.Dev2lastPkt-1){
//
//                                    // Reset
//
//                                    Common.Dev2TransferCompleted = true;
//
//                                    Common.isRunning = false;
//
//                                    Connection_Manager.shared.SendData(reset, Common.LEFTDEVAdd);
//
//                                    Log.d(TAG, "SendData: Sending reset for S1 " + Common.LEFTDEVAdd);
//
//                                    sendData(reset, device.getAddress(), null);
//
//                                    mainActivity.UpdateUI(false);
//
//                                    Common.isComplete = true;
//
//                                    Common.IsDataComming = false;
//
//                                    return;
//                                }else if(Common.lastPktno == package_number){
//                                    if(Common.longCounts == 0){
//                                        Common.longCounts = 10;
//                                        Toast.makeText(MainActivity.shared(),"Session Disturbed Exit And Restart Device And App", Toast.LENGTH_LONG).show();
//                                    }else{
//                                        Common.longCounts--;
//                                    }
//                                }else{
//                                    Common.longCounts = 60;
//                                    Common.lastPktno = package_number;
//                                }
//                            }
//
//                        }
//                }
//            }
//
//
//            Common.isRunning = true;
//
//            // CASE4 :: - when both Received last pkt info
//
//            if(Common.Dev2lastPkt != null && Common.Dev1lastPkt != null){
//
//                Log.e(TAG, "process_pdr_data: PPP got last pkt num CASE 4");
//
//                if(Common.IsStopped == true && Common.isComplete == false){
//
//                    Log.e(TAG, "process_pdr_data: is stopped" );
//
//                    if(package_number == Common.getlastPkt()){
//
//                        Common.Dev2TransferCompleted = true;
//
//                        ack = createAck(ack, package_number_1, package_number_2);
//
//                        //S1
//
//                        Common.isRunning = false;
//
//                        sendData(ack, device.getAddress(), String.valueOf(package_number));
//
//                        Common.wait(100);
//
//                        Log.d(TAG, "SendData: Sending reset for S1" + device.getAddress());
//
//                        Connection_Manager.shared.SendData(reset, Common.LEFTDEVAdd);
//
//                        Log.d(TAG, "SendData: Sending reset for S1 " + Common.LEFTDEVAdd);
//
//                        sendData(reset, device.getAddress(), null);
//
//                        mainActivity.UpdateUI(false);
////
////                        mainActivity.FAB.setBackgroundColor(mainActivity.fab_color);
////
////                        mainActivity.FAB.setImageResource(R.drawable.ic_play);
//
//                        Log.e(TAG, "broadcastUpdate: UI Got all packets " +package_number );
//
//                        pdr_data = new PDR_Data(device.getAddress(),"PDR_1", data);
//
//                        if(pdr_data != null){
//                            EventBus.getDefault().post(pdr_data);
//                        }
//                        Common.isComplete = true;
//                        Common.IsDataComming = false;
//                        return;
//                    }
//                    if(package_number < Common.getlastPkt()){
//                        //S2
//                        Log.e(TAG, "process_pdr_data: Transfer not complete" );
//                        mainActivity.FAB.setBackgroundColor(Color.DKGRAY);
//
//                        Log.d(TAG, "SendData: Sending r remianing pkt ack");
//
//                        mainActivity.UpdateUI(true);
//
//                        Common.Dev2TransferCompleted = false;
//                        Common.isRunning = true;
//                        Common.isComplete = false;
//                        Log.e(TAG, "broadcastUpdate: UI not got all packets " +package_number );
//                    }else{
//                        //S3
//                        Log.e(TAG, "process_pdr_data: Transfer Complete" );
//                        Common.Dev2TransferCompleted = true;
//                        Common.IsDataComming = false;
//                        Common.isComplete = true;
//                        Common.isRunning = false;
//                        sendData(reset, device.getAddress(), null);
//                        Log.d(TAG, "SendData: Sending reset for S3" + device.getAddress());
//                        Connection_Manager.shared.SendData(reset, Common.LEFTDEVAdd);
//                        Log.d(TAG, "SendData: Sending reset for S3" + Common.LEFTDEVAdd);
//
//                        mainActivity.UpdateUI(false);
////                        mainActivity.FAB.setBackgroundColor(mainActivity.fab_color);
////                        mainActivity.FAB.setImageResource(R.drawable.ic_play) ;
//                        Log.e(TAG, "broadcastUpdate: UI Got all packets completed " +package_number );
//
//                        if(Common.isTransferCompleted() == true){
//                            mainActivity.FAB.setBackgroundColor(mainActivity.fab_color);
//                            mainActivity.FAB.setImageResource(R.drawable.ic_play) ;
//                            return;
//                        }
//                    }
//                }
//            }
//
//            Common.IsDataComming = false;
//
//            ack = createAck(ack, package_number_1, package_number_2);
//
////            mainActivity.UpdateUI(true);
//
//            SendData(ack, device.getAddress(),""+package_number);
//
//            if(Common.IsStopped == false){
//
//                mainActivity.UpdateRunningSessonUI();
//                //mainActivity.FAB.setImageResource(R.drawable.ic_stop2) ;
//            }
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
//                            SendData(ack, device.getAddress(), null);
//
//                        }
//
//
//                        data = split_pkts[j];
//
//                        pdr_data = new PDR_Data(device.getAddress(),"PDR_1", data);
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
//        ////////////////// posting Data ----------------------------------------------------------------
//
//        pdr_data = new PDR_Data(device.getAddress(),"PDR_1", data);
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
        Log.d(TAG, "SendData: Sending reset ACk " + ack[0] +","+ ack[1] +","+ ack[2] +","+ ack[3] +","+ ack[4] +",");
        return ack;
    }

    //////////////////////////////// Reconnector ---------------------------------------------------------------------------------------------


    public void Connect_prev_device(BluetoothDevice device){

        prev_reconectionDevice2 = device;

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
            if(prev_reconectionDevice2 == null ){
                if(prev_reconnection_timer != null) prev_reconnection_timer.cancel();
                return;
            }
            // if there is reconnection req then connect device
            if(prev_reconectionDevice2 != null){
                Connect(prev_reconectionDevice2);
            }
        }
    }


//    public void UI_update(){
//        // if there is all_ready timer running then cancel and restart it
//        if(ui_update_timer != null){
//            ui_update_timer.cancel();
//            ui_update_timer = null;
//        }
//        ui_update_timer = new Timer();
//        ui_update_timer.schedule(new UI_Updater(), 5000, 5000);
//    }
//
//    class UI_Updater extends TimerTask {
//        @Override
//        public void run() {
//
//            if(Common.isRunning == false){
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainActivity.UpdateUI(false);
//                    }
//                }).start();
//            }
//
//            // CASE :: 1 - somehow isRunning not updated and data is not coming so update manually
//            if(Common.isRunning == true && Common.IsDataComming == false && Common.IsStopped == true){
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainActivity.UpdateUI(false);
//                    }
//                }).start();
//            }
//        }
//    }



//    public void WritetoLogFile(String  loginfo){
//        SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
//        Date now = new Date();
//        DecimalFormat df3 = new DecimalFormat("0.##");
//        DecimalFormat df5 = new DecimalFormat("0.####");
//        try {
//            File root = new File(Environment.getExternalStorageDirectory() + File.separator + MainActivity.parentDir, MainActivity.logDir);
//            if (!root.exists()) {
//                root.mkdirs();
//            }
//            File gpxfile = new File(root, "Time"+".txt");
//            FileWriter writer = new FileWriter(gpxfile, true);
//            String curnt_time = formatter.format(now);
//            writer.append(" Time " + curnt_time + " pkt no "+  loginfo +"\n");
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
