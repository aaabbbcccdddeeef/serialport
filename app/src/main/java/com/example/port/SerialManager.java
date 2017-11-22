package com.example.port;

import android.app.Activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyw on 2017-11-12.
 */

public class SerialManager {

    private List<SerialEntity> mSerialList;

    private Activity mActivity;
    private DataReceivedListener mDataReceivedListener;

    public SerialManager(Activity activity, DataReceivedListener dataReceivedListener) {
        this.mActivity = activity;
        this.mDataReceivedListener = dataReceivedListener;
        mSerialList = new ArrayList<>();
    }

    public void open(SerialEntity serialEntity) throws SecurityException, IOException, InvalidParameterException {
        SerialPort mSerialPort = new SerialPort(new File(serialEntity.getPort()), serialEntity.getBaudRate());
        OutputStream mOutputStream = mSerialPort.getOutputStream();
        InputStream mInputStream = mSerialPort.getInputStream();
        ReadThread   mReadThread = new ReadThread(serialEntity.getPort(), serialEntity.getPort(), mInputStream);
        mReadThread.start();
        SendThread   mSendThread = new SendThread(serialEntity.getPort(), mOutputStream);
        mSendThread.setSuspendFlag();
        mSendThread.start();
        serialEntity.setInputStream(mInputStream);
        serialEntity.setOutputStream(mOutputStream);
        serialEntity.setOpen(true);
        serialEntity.setReadThread(mReadThread);
        serialEntity.setSendThread(mSendThread);
        serialEntity.setSerialPort(mSerialPort);
        mSerialList.add(serialEntity);
    }


    private class ReadThread extends Thread {
        private String mPort;
        private InputStream mInputStream;

        public ReadThread(String name, String sport, InputStream inputStream) {
            super(name);
            this.mPort = sport;
            mInputStream = inputStream;
        }

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (mInputStream == null) return;
                    byte[] buffer = new byte[512];
                    int size = mInputStream.read(buffer);
                    if (size > 0) {
                        final ComBean comRecData = new ComBean(mPort, buffer, size);
                        if(mDataReceivedListener != null){
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mDataReceivedListener.onDataReceiverd(comRecData);
                                }
                            });
                        }
                    }
                    try {
                        Thread.sleep(50);//延时50ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class SendThread extends Thread {

        private OutputStream mOutputStream;

        private boolean suspendFlag = true;// 控制线程的执行
        private int iDelay = 500;

        public SendThread(String name, OutputStream outputStream) {
            super(name);
            this.mOutputStream = outputStream;
        }

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                synchronized (this) {
                    while (suspendFlag) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                byte[] _bLoopData = new byte[]{0x30};
                send(_bLoopData);
                try {
                    Thread.sleep(iDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //线程暂停
        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        //唤醒线程
        public synchronized void setResume() {
            this.suspendFlag = false;
            notify();
        }

        public void send(byte[] bOutArray) {
            try {
                mOutputStream.write(bOutArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopSend(String sPort) {
        if(mSerialList != null && mSerialList.size()> 0) {
            for (int i = 0; i < mSerialList.size(); i++) {
                SerialEntity serialEntity = mSerialList.get(i);
                SendThread sendThread = (SendThread) serialEntity.getSendThread();
                if(sendThread.getName().equals(sPort)) {

                    if (sendThread != null) {
                        sendThread.setSuspendFlag();
                    }
                }
            }
        }
    }

    public void close() {
        if(mSerialList != null && mSerialList.size() > 0){
            for(int i = 0;  i < mSerialList.size(); i++){
                SerialEntity serialEntity = mSerialList.get(i);
                serialEntity.getReadThread().interrupt();
                serialEntity.getSerialPort().close();
            }
        }
    }

    public void sendHex(SerialEntity control, String sHex) {
        if (mSerialList != null && mSerialList.size() > 0) {
            for (int i = 0; i < mSerialList.size(); i++) {
                SerialEntity serialEntity = mSerialList.get(i);
                if(serialEntity.getPort().equals(control.getPort())){

                    if(control.getBaudRate() == 57600){
                        try {
                            serialEntity.getOutputStream().write(sHex.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        byte[] bOutArray = HexUtils.HexToByteArr(sHex);
                        try {
                            serialEntity.getOutputStream().write(bOutArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }


}
