package com.example.port;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lyw on 2017-11-12.
 */

public class SerialEntity {

    private String sPort;
    private int iBaudRate;

    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean isOpen;

    private Thread readThread;
    private Thread sendThread;

    private SerialPort serialPort;

    public String getPort() {
        return sPort;
    }

    public void setPort(String sPort) {
        this.sPort = sPort;
    }

    public int getBaudRate() {
        return iBaudRate;
    }

    public void setBaudRate(int iBaudRate) {
        this.iBaudRate = iBaudRate;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Thread getReadThread() {
        return readThread;
    }

    public void setReadThread(Thread readThread) {
        this.readThread = readThread;
    }

    public Thread getSendThread() {
        return sendThread;
    }

    public void setSendThread(Thread sendThread) {
        this.sendThread = sendThread;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }
}
