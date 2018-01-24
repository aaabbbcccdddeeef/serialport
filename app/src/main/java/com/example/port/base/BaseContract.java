package com.example.port.base;

/**
 * Created by android on 2018/1/24.
 */

public interface BaseContract {

    interface BasePresenter<T extends BaseView> {

        void attachView(T view);

        void detachView();
    }

    interface BaseView {

    }
}
