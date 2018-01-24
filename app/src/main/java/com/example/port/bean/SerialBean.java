package com.example.port.bean;

/**
 * Created by android on 2018/1/24.
 */

public class SerialBean {

    private int baudRate;
    private String motion;

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    @Override
    public String toString() {
        return "SerialBean{" +
                "baudRate=" + baudRate +
                ", motion='" + motion + '\'' +
                '}';
    }


}
