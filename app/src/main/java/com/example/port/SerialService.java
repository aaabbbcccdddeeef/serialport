package com.example.port;

import android.util.Log;

import com.example.port.base.BaseService;
import com.example.port.bean.SerialBean;
import com.example.port.event.ActivityToServiceEvent;
import com.example.port.event.ServiceToActivityEvent;
import com.example.port.serial.HexUtils;
import com.example.port.serial.SerialPortManager;
import com.example.port.serial.listener.OnOpenSerialPortListener;
import com.example.port.serial.listener.OnSerialPortDataListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by android on 2018/1/24.
 */

public class SerialService extends BaseService implements OnOpenSerialPortListener, OnSerialPortDataListener {

    public static final String DEV = "/dev/";

    public static final String devName = "ttyUSB0";
    public static final String voiceName = "ttyUSB1";
    public static final String cruiseName = "ttyUSB2";

    public static final int DEV_BAUDRATE = 9600;
    public static final int VOICE_BAUDRATE = 115200;
    public static final int CRUISE_BAUDRATE = 57600;

    private SerialPortManager mManager1;
    private SerialPortManager mManager2;
    private SerialPortManager mManager3;

    @Override
    public void onCreate() {
        super.onCreate();

        mManager2 = init(mManager2, new File(DEV + voiceName), DEV_BAUDRATE);
        mManager1 = init(mManager1, new File(DEV + devName), VOICE_BAUDRATE);
        mManager3 = init(mManager3, new File(DEV + cruiseName), CRUISE_BAUDRATE);

    }

    private SerialPortManager init(SerialPortManager serialPortManager, File file, int baudRate) {
        serialPortManager = new SerialPortManager();
        serialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(this)
                .openSerialPort(file, baudRate);
        return serialPortManager;
    }

    @Override
    public void onDestroy() {
        close(mManager1);
        close(mManager2);
        close(mManager3);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultEvent(ActivityToServiceEvent event) {
        if (event.isOk()) {
            SerialBean serialBean = event.getBean();
            Log.d("SerialService", "activity发送到service中的数据 " + serialBean.toString());
            int iBaudRate = serialBean.getBaudRate();
            if (iBaudRate == DEV_BAUDRATE) {
                byte[] bOutArray = HexUtils.HexToByteArr(serialBean.getMotion());
                mManager2.sendBytes(bOutArray);
            } else if (iBaudRate == VOICE_BAUDRATE) {
//                byte[] bOutArray = HexUtils.HexToByteArr(serialBean.getMotion());
                mManager1.sendBytes(serialBean.getMotion().getBytes());
            } else if (iBaudRate == CRUISE_BAUDRATE) {
                mManager3.sendBytes(serialBean.getMotion().getBytes());
            }
        } else {
            Log.d("SerialService", "ReceiveEvent error");
        }
    }


    private void close(SerialPortManager serialPortManager) {
        if (null != serialPortManager) {
            serialPortManager.closeSerialPort();
        }
    }


    //*****************************打开
    @Override
    public void onSuccess(File device, int baudRate) {
        Log.d("SerialService", String.format("串口 [%s] 打开成功   波特率 %s", device.getPath(), baudRate));
    }

    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case NO_READ_WRITE_PERMISSION:
                Log.d("SerialService", device.getPath() + " 没有读写权限");

                break;
            case OPEN_FAIL:
            default:
                Log.d("SerialService", device.getPath() + " 串口打开失败");
                break;
        }
    }

    //****************************收发数据
    @Override
    public void onDataReceived(int baudRate, byte[] bytes) {

        StringBuilder sMsg = new StringBuilder();
        if (baudRate == VOICE_BAUDRATE) {
            //在十六进制转换为字符串后的得到的是Unicode编码,此时再将Unicode编码解码即可获取原始字符串
            sMsg.append(HexUtils.hexStringToString(HexUtils.byte2HexStr(bytes)));
        } else {
            sMsg.append(new String(bytes));
        }
        SerialBean serialBean = new SerialBean();
        serialBean.setBaudRate(baudRate);
        serialBean.setMotion(sMsg.toString());
        Log.d("SerialService", "service中接受到串口的数据" + serialBean.toString());

        ServiceToActivityEvent serviceToActivityEvent = new ServiceToActivityEvent();
        serviceToActivityEvent.setEvent(serialBean);
        EventBus.getDefault().post(serviceToActivityEvent);
    }

    @Override
    public void onDataSent(int baudRate, byte[] bytes) {
        Log.d("SerialService", "send success " + baudRate);
    }

}
