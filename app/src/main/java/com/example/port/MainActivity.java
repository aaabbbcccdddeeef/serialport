package com.example.port;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fanfan.hotel.serial.SerialPort;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    private SerialPresenter mSerialPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSerialPresenter = new SerialPresenter(this);
        try {
            SerialPort serialPort = new SerialPort(null, -1);
            serialPort.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
