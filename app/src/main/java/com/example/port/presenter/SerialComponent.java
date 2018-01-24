package com.example.port.presenter;

import com.example.port.MainActivity;

import dagger.Component;

/**
 * Created by android on 2018/1/24.
 */

@Component(modules = SerialModule.class)
public interface SerialComponent {

    void inject(MainActivity activity);
}
