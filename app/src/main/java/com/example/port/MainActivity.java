package com.example.port;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.port.bean.SerialBean;
import com.example.port.event.ServiceToActivityEvent;
import com.example.port.presenter.DaggerSerialComponent;
import com.example.port.presenter.SerialContract;
import com.example.port.presenter.SerialModule;
import com.example.port.presenter.SerialPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements SerialContract.View {

    @Inject
    SerialPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, SerialService.class));

        DaggerSerialComponent.builder()
                .serialModule(new SerialModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, SerialService.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultEvent(ServiceToActivityEvent event) {
        if (event.isOk()) {
            SerialBean serialBean = event.getBean();
            presenter.onDataReceiverd(serialBean);
        } else {
            Log.d("MainActivity", "ReceiveEvent error");
        }
    }

    @Override
    public void stopAll() {

    }

}
