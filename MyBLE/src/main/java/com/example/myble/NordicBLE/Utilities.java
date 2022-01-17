package com.example.myble.NordicBLE;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Utilities {

    public static final int WAITING_TIME = 10;
    public static final int START_TIMEOUT = 15000;
    public static final int NEXT_TIMEOUT = 10000;
    private static int First4Bit_VAL = 0;
    private static int Second4Bit_VAL = 0;
    public static int start_sensor_type = -1;
    public static boolean[] ShowHide = {true, true, true, true};


    public static List<String> PREDEFINED_MAC_IDS = new ArrayList<String>(){
        {
            add("00:A0:50:E4:74:CF");
            add("00:A0:50:F9:03:67");
            add("C7:61:4B:41:87:79");
            add("F2:3D:B4:DA:49:3A");
            add("FF:AD:B1:80:F8:47");
            add("EB:F5:A5:54:48:FB");
            add("E1:D6:C0:CF:1F:5A");
            add("F8:02:F9:6E:BD:3A");
            add("FF:AD:B1:80:F8:48");
            add("EB:FA:3D:96:96:91");
            add("EB:FA:3D:96:96:90");
            add("D6:B9:BD:A8:83:46");
            add("F8:98:A1:67:CF:0C");
            add("E8:82:41:5D:63:44");
            add("E4:5E:41:03:70:BF");

        }
    };





    public static void showDisconnectionAlert(final Activity activity) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Disconnection too long");
        alertDialog.setMessage("Device is disconnected for too long");

        alertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    //convert two byte to short integer
    public static short getShortValue(byte b1, byte b2){
        byte[] temp = {b1, b2};
        return ByteBuffer.wrap(temp).getShort();
    }

    //convert two byte to integer
    public static int getIntValue(byte b1, byte b2){
        int val1 = b1 & 0xFF ;
        int val2 = b2 & 0xFF ;
        return val1*256 + val2;
    }





    public static String getDeviceTime(byte b4, byte b5, byte b6){

        String s1 = String.format("%8s", Integer.toBinaryString(b4 & 0xFF)).replace(' ', '0');

        String s2 = String.format("%8s", Integer.toBinaryString(b5 & 0xFF)).replace(' ', '0');

        String s3 = String.format("%8s", Integer.toBinaryString(b6 & 0xFF)).replace(' ', '0');


        char[] first8bit1 = s1.toCharArray();
        char[] sec8bit1 = s2.toCharArray();
        char[] third8bit1 = s3.toCharArray();


        //s1 = 11111111 - first 5 bit = hours
        //s2 = 11111111 - last 3 bit of s1 and first 3 bit of s2 = min
        // remaining 5 bit of s2 and starting 1 bit of s3 = second
        //s3 = 11111111 - apart from starting single bit represents millisecond  = millisecond * 10;


        int hour1 = binaryToInteger("" +first8bit1[0] + first8bit1[1] + first8bit1[2] + first8bit1[3] + first8bit1[4]);
        //binaryToInteger("" + hour1);
        int min1 = binaryToInteger(""+first8bit1[5]+first8bit1[6]+first8bit1[7]+sec8bit1[0]+sec8bit1[1]+sec8bit1[2]);
        int sec1 = binaryToInteger(""+sec8bit1[3]+sec8bit1[4]+sec8bit1[5]+sec8bit1[6]+sec8bit1[7]+third8bit1[0]);
        int millisec1 = binaryToInteger(""+third8bit1[1]+third8bit1[2]+third8bit1[3]+third8bit1[4]+third8bit1[5]+third8bit1[6]+third8bit1[7]);
        int msec1 = millisec1*10;

        String hour = String.format("%02d", hour1);
        String sec = String.format("%02d", sec1);
        String min = String.format("%02d", min1);
        String msec = String.format("%03d", msec1);

        String time = hour+min+sec+msec;

        Log.d("TimeCTS", "TimeCTS received time is " + time);

        return time;
    }

    public static int[] getArrary(int num){
        String temp = Integer.toString(num);
        int[] newGuess = new int[temp.length()];
        for (int i = 0; i < temp.length(); i++)
        {
            newGuess[i] = temp.charAt(i) - '0';
        }
        return newGuess;
    }

    public static int binaryToInteger(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for(int i=numbers.length - 1; i>=0; i--)
            if(numbers[i]=='1')
                result += Math.pow(2, (numbers.length-i - 1));
        return result;
    }



    /**
     * Create new SWDR command which setting by user
     * @return byte array
     */
    static byte[] getNewSwdrCmd() {
        return getNewSwdrCmd(First4Bit_VAL, Second4Bit_VAL);
    }

    /**
     * Create new SWDR command which setting by user
     * @param first4Bits  first 4 bits of byte
     * @param second4Bits_VAL second 4 bits of byte
     * @return byte array
     */
    private static byte[] getNewSwdrCmd(int first4Bits, int second4Bits_VAL){
        byte[] cmd = new byte[4];
        cmd[0] = 0x50;
        cmd[1] = (byte)(first4Bits*16 + second4Bits_VAL);

        int total = (cmd[1] & 0xFF) + (cmd[0] & 0xFF);
        byte[] checksum = intToBytes(total); //Convert total to 4 byte array

        cmd[2] = checksum[1];
        cmd[3] = checksum[0];
        return cmd;
    }

    /**
     * Convert int to byte array
     * @param x int value
     * @return byte array
     */
    public static byte[] intToBytes(int x) {
        ByteBuffer buffer_int = ByteBuffer.allocate(Integer.SIZE/8).order(ByteOrder.LITTLE_ENDIAN);
        buffer_int.putInt(0, x);
        return buffer_int.array();
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

    private static int convertstringtobyte(String string) {
        return Integer.parseInt(string, 16);
    }

    public static String byte2HexStr(byte[] paramArrayOfByte, int paramInt) {
        StringBuilder localStringBuilder1 = new StringBuilder("");
        int i = 0;
        for (; ; ) {
            if (i >= paramInt) {
                String str1 = localStringBuilder1.toString().trim();
                Locale localLocale = Locale.US;
                return str1.toUpperCase(localLocale);
            }
            String str2 = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
            if (str2.length() == 1) {
                str2 = "0" + str2;
            }
            StringBuilder localStringBuilder2 = localStringBuilder1.append(str2);
            StringBuilder localStringBuilder3 = localStringBuilder1.append(" ");
            i += 1;
        }
    }

    public static String convertByteArrToStr(byte[] byteArray, boolean needSpace){
        if(byteArray == null || byteArray.length == 0) return "";

        final StringBuilder stringBuilder = new StringBuilder(byteArray.length);
        String fmt = "%02X";
        if(needSpace){
            fmt = "%02X ";
        }
        for (byte byteChar : byteArray) {
            stringBuilder.append(String.format(fmt, byteChar));
        }
        return stringBuilder.toString();
    }


    /**
     *
     * @param millisec
     */
    public static void wait(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("FILE ERROR ", "Problem creating folder");
                ret = false;
            } else {
                Log.i("FILE HANDLING", path + " Created successfully");
            }
        }
        return ret;
    }




}
