package com.example.port.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by android on 2018/1/24.
 */

public class BaseEvent<T> {

    protected T t;                      // 实体类

    protected boolean ok = false;

    public BaseEvent() {
        this.ok = false;
    }

    public BaseEvent(@Nullable T t) {
        this.ok = null != t;
        this.t = t;
    }

    public BaseEvent setEvent(@Nullable T t) {
        this.ok = null != t;
        this.t = t;
        return this;
    }

    public boolean isOk() {
        return ok;
    }

    public T getBean() {
        return t;
    }

}
