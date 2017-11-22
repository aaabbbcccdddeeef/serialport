package com.example.port;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidParameterException;

public class SerialPresenter implements DataReceivedListener {

    private SerialEntity comA;
    private SerialEntity comB;
    private SerialEntity comC;

    private String devName = "ttyUSB0";
    private String voiceName = "ttyUSB1";
    private String cruiseName = "ttyUSB2";

    private SerialManager mSerialManager;

    public SerialPresenter(Activity activity) {

        mSerialManager = new SerialManager(activity, this);

        comA = new SerialEntity();
        comA.setPort("/dev/" + devName);
        comA.setBaudRate(9600);
        openComPort(comA);

        comB = new SerialEntity();
        comB.setPort("/dev/" + voiceName);
        comB.setBaudRate(115200);
        openComPort(comB);

        comC = new SerialEntity();
        comC.setPort("/dev/" + cruiseName);
        comC.setBaudRate(57600);
        openComPort(comC);
    }

    public boolean isHasDevices(SerialEntity comPort) {
        SerialPortFinder serialPortFinder = new SerialPortFinder();
        String[] devices = serialPortFinder.getAllDevices();
         if (devices != null && devices.length > 0) {
            for (int i = 0; i < devices.length; i++) {
                if (comPort.getPort().equals("/dev/" + devices[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public void openComPort(SerialEntity comPort) {
        if (!isHasDevices(comPort)) {
            return;
        }
        try {
//            comPort.open();
            mSerialManager.open(comPort);
        } catch (SecurityException e) {
            Log.e("openComPort", "打开串口失败:没有串口读/写权限!");
        } catch (IOException e) {
            Log.e("openComPort", "打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            Log.e("openComPort", "打开串口失败:参数错误!");
        }
    }

    public void closeComPort() {

        if (comA != null) {
            if (!isHasDevices(comA)) {
                return;
            }
            mSerialManager.stopSend(comA.getPort());
        }

        if (comB != null) {
            if (!isHasDevices(comB)) {
                return;
            }
            mSerialManager.stopSend(comB.getPort());
        }

        if (comC != null) {
            if (!isHasDevices(comC)) {
                return;
            }
            mSerialManager.stopSend(comC.getPort());
        }

        mSerialManager.close();
    }


    public void sendPortData(SerialEntity control, String sOut) {

        if (!isHasDevices(control)) {
            return;
        }
        if (control != null) {
            Log.e("sendPortData", "串口指令 ： " + control.getPort() + " , "  + control.getBaudRate() + " , " + sOut);
            mSerialManager.sendHex(control, sOut);

        }
    }

    public void receiveMotion(ComType comType, String motion) {
        if (comType == ComType.A) {
            sendPortData(comA, motion);
        } else if (comType == ComType.B) {
            sendPortData(comB, motion);
        } else if (comType == ComType.C) {
            sendPortData(comC, motion);
        }
    }

    @Override
    public void onDataReceiverd(ComBean comRecData) {
        if(comRecData.sComPort.equals(comB.getPort())) {
            StringBuilder sMsg = new StringBuilder();
            //在十六进制转换为字符串后的得到的是Unicode编码,此时再将Unicode编码解码即可获取原始字符串
            sMsg.append(HexUtils.hexStringToString(HexUtils.byte2HexStr(comRecData.bRec)));
//        sMsg.append(HexUtils.unicode2String(strStr)); [B@ea3c18a  [B@754bbdb
            if (sMsg.toString().contains("WAKE UP!")) {

                if(sMsg.toString().contains("##### IFLYTEK")){


                String str = sMsg.toString().substring(sMsg.toString().indexOf("angle:") + 6, sMsg.toString().indexOf("##### IFLYTEK"));
                int angle = Integer.parseInt(str.trim());
                if (0 <= angle && angle < 30) {
                    receiveMotion(ComType.A, "A521821EAA");
                } else if (30 <= angle && angle <= 60) {
                    receiveMotion(ComType.A, "A521823CAA");
                } else if (120 <= angle && angle <= 150) {
                    receiveMotion(ComType.A, "A5218278AA");
                } else if (150 < angle && angle <= 180) {
                    receiveMotion(ComType.A, "A5218296AA");
                }
                }
            }else if(comRecData.sComPort.equals(comC.getPort())){

                if (sMsg.toString().trim().equals("first")||sMsg.toString().trim().equals("second")||sMsg.toString().trim().equals("third")
                        ||sMsg.toString().trim().equals("fifth")||sMsg.toString().trim().equals("sixth")) {

                    receiveMotion(ComType.A, "A50C8001AA");
                }

            }
        }
    }
}
