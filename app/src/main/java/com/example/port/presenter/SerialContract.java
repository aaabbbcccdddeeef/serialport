package com.example.port.presenter;

import com.example.port.bean.SerialBean;
import com.example.port.base.BaseContract;

/**
 * Created by android on 2018/1/24.
 */

public interface SerialContract {

    interface View extends BaseContract.BaseView {

        void stopAll();

    }
    interface Presenter extends BaseContract.BasePresenter<View> {

        void receiveMotion(int type, String motion);

        void onDataReceiverd(SerialBean serialBean);
    }

}
