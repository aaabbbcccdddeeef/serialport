package com.example.port.serial.listener;

public interface OnSerialPortDataListener {

    /**
     * 数据接收
     *
     * @param bytes 接收到的数据
     */
    void onDataReceived(int baudRate, byte[] bytes);

    /**
     * 数据发送
     *
     * @param bytes 发送的数据
     */
    void onDataSent(int baudRate, byte[] bytes);
}
