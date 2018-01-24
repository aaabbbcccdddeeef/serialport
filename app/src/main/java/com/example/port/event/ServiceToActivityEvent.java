package com.example.port.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.port.base.BaseEvent;
import com.example.port.bean.SerialBean;

/**
 * Created by android on 2018/1/24.
 */

public class ServiceToActivityEvent extends BaseEvent<SerialBean> {

    public ServiceToActivityEvent() {
        super();
    }

    public ServiceToActivityEvent(@Nullable SerialBean serialBean) {
        super(serialBean);
    }
}
