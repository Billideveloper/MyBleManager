package com.example.myble.NordicBLE;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Common {


    public static Integer longCounts = 60;

    public static Boolean isRunning = false;

    public static Integer no_ofselectedDevice = 0;

    public static Boolean isSessionStarted = false;

    public static Boolean isReset = false;

    public static Integer lastPktno = 0;

    public static Integer lastStoredPktno = -1;

    public static Integer ackWait = 5;

    public static Integer leftStopWait = 25;

    public static Boolean leftStopped = false;

    public static Boolean leftDisconnected = false;

    public static Integer pktloopCount = 15;

    public static Boolean isWaitingForStop = false;

    public static Map<String, DeviceDetails> DEVICE_MAPS = new HashMap<>();
    public static Map<String, DeviceDetails> DEVICE_MAPS1 = new HashMap<>(1);
    public static Map<String, DeviceDetails> DEVICE_MAPS2 = new HashMap<>(1);

    public static final short PKT_SIZE = 18;

    private static Timer session_timer_1 = null;

    public static Boolean isStartedSingle = true;
    public static Integer FromDevice = 1;

    public static Integer Dev1lastPkt = null;
    public static Integer Dev2lastPkt = null;
    public static Boolean Dev1TransferCompleted = true;
    public static Boolean Dev2TransferCompleted = true;
    public static Boolean IsTrannsferCompleted = false;
    public static Boolean Dev1IsStopped = false;
    public static Boolean Dev2IsStopped = false;

    public static Boolean isComplete = false;

    public static Boolean ISSingle = true;

    public static Integer PktNum = 0;

    public static Boolean isFirst = true;
    public static int WaitLoop = 0;

    public static String TimeLeftDev = "";
    public static String TimeRightDev = "";
    public static String StepCountLeft = "";
    public static String StepCountRight = "";

    public static Boolean isRestarted = false;

    public static Boolean IsStopped = false;

    public static Boolean IsDataComming = false;

    public static Boolean IsManallystopped = false;

    public static String RIGHTDEVAdd = null;

    public static String LEFTDEVAdd = null;

    public static Boolean IsAutoStopped = false;

    public static Boolean isActivityStopped = true;

    public static Boolean isTransferdCompleted = false;


    public static boolean bFirstTime = true;
    public static boolean bFirstTime1 = true;


    public static Boolean isTransferCompleted(){

        // return Dev1TransferCompleted;

        if(Dev1TransferCompleted == true || Dev2TransferCompleted == true){
            return true;
        }else{
            return false;
        }

    }

    public static void SetTransferCompleted(){
        Dev1TransferCompleted = true;
        Dev2TransferCompleted = true;
    }

    public static Integer getlastPkt(){
        if(Dev1lastPkt <= Dev2lastPkt){
            return Dev1lastPkt;
        }else{
            return Dev2lastPkt;
        }
    }

    public static Boolean isStopped_(){
        if(Dev1IsStopped == true && Dev2IsStopped == true){
            return true;
        }else{
            return false;
        }

    }

    public static void wait(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Boolean isSingle;

    public static byte[] SingleStartCmd(){
        isSingle = true;
        byte[] cmd = new byte[7];
        cmd[0] = 0x06;
        cmd[1] = 0x03;
        cmd[2] = 0x00;
        cmd[3] = 0x00;
        cmd[4] = 0x00;
        int total = 0;
        for(int i = 0 ; i < (cmd.length - 2) ; i++ ){
            int val = cmd[i] & 0xFF;
            total += val;
        }
        byte[] checksum = ByteUtils.intToBytes(total); //Convert total to 4 byte array

        cmd[5] = checksum[1];
        cmd[6] = checksum[0];
        return  cmd;
    }

    public static byte[] SingleLeftStartCmd(){
        isSingle = false;
        byte[] cmd = new byte[7];
        cmd[0] = 0x06;
        cmd[1] = 0x01;
        cmd[2] = 0x00;
        cmd[3] = 0x00;
        cmd[4] = 0x00;
        int total = 0;
        for(int i = 0 ; i < (cmd.length - 2) ; i++ ){
            int val = cmd[i] & 0xFF;
            total += val;
        }
        byte[] checksum = ByteUtils.intToBytes(total); //Convert total to 4 byte array

        cmd[5] = checksum[1];
        cmd[6] = checksum[0];
        return  cmd;
    }

    public static byte[] SingleRightStartCmd(){
        isSingle = false;
        byte[] cmd = new byte[7];
        cmd[0] = 0x06;
        cmd[1] = 0x02;
        cmd[2] = 0x00;
        cmd[3] = 0x00;
        cmd[4] = 0x00;
        int total = 0;
        for(int i = 0 ; i < (cmd.length - 2) ; i++ ){
            int val = cmd[i] & 0xFF;
            total += val;
        }
        byte[] checksum = ByteUtils.intToBytes(total); //Convert total to 4 byte array

        cmd[5] = checksum[1];
        cmd[6] = checksum[0];
        return  cmd;
    }

    public static byte[][] splitArray(final byte[] byteArr, final int split_len){
        int size = (int)Math.ceil((float)byteArr.length/(float)split_len);
        int length = byteArr.length;

        byte[][] Array_of_arrays = new byte[size][split_len];
        int counter1 = 0;
        for (int i = 0; i < length - split_len + 1; i += split_len)
            Array_of_arrays[counter1++] = Arrays.copyOfRange(byteArr, i, i + split_len);

        if (length % split_len != 0)
            Array_of_arrays[counter1] = Arrays.copyOfRange(byteArr, length - length % split_len, length);

        return Array_of_arrays;
    }








    /**
     * Method to convert hex to byteArray
     */
    public static byte[] convertingTobyteArray(String result) {
        String[] splited = result.split("\\s+");
        byte[] valueByte = new byte[splited.length];
        for (int i = 0; i < splited.length; i++) {
            if (splited[i].length() > 2) {
                String trimmedByte = splited[i].split("x")[1];
                valueByte[i] = (byte) convertstringtobyte(trimmedByte);
            }

        }
        return valueByte;

    }

    /**
     * Convert the string to byte
     *
     * @param string
     * @return
     */

    private static int convertstringtobyte(String string) {
        return Integer.parseInt(string, 16);
    }
}
