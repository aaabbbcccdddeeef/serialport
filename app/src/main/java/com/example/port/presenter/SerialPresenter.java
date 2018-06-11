//package com.example.port.presenter;
//
//import android.util.Log;
//
//import com.example.port.SerialService;
//import com.example.port.bean.SerialBean;
//import com.example.port.event.ActivityToServiceEvent;
//
//import org.greenrobot.eventbus.EventBus;
//
//import javax.inject.Inject;
//
//import dagger.Module;
//
///**
// * Created by android on 2018/1/24.
// */
//
//public class SerialPresenter {
//
//    private SerialContract.View mView;
//
//    @Inject
//    SerialPresenter(SerialContract.View view) {
//        mView = view;
//    }
//
//
//    public void receiveMotion(int type, String motion) {
//        Log.d("SerialPresenter", "send " + motion);
//        SerialBean serialBean = new SerialBean();
//        serialBean.setBaudRate(type);
//        serialBean.setMotion(motion);
//        ActivityToServiceEvent serialEvent = new ActivityToServiceEvent();
//        serialEvent.setEvent(serialBean);
//        EventBus.getDefault().post(serialEvent);
//    }
//
//
//    public void onDataReceiverd(SerialBean serialBean) {
//        int iBaudRate = serialBean.getBaudRate();
//        String motion = serialBean.getMotion();
//        if (iBaudRate == SerialService.DEV_BAUDRATE) {
//            //第一个串口
//        } else if (iBaudRate == SerialService.VOICE_BAUDRATE) {
//            //我的第二个是麦克风阵列
//            if (motion.toString().contains("WAKE UP!")) {
//
//                mView.stopAll();
//                if (motion.toString().contains("##### IFLYTEK")) {
//
//                    String str = motion.toString().substring(motion.toString().indexOf("angle:") + 6, motion.toString().indexOf("##### IFLYTEK"));
//                    int angle = Integer.parseInt(str.trim());
//                    Log.d("SerialPresenter", "解析到应该旋转的角度 : " + angle);
//
//                    receiveMotion(SerialService.DEV_BAUDRATE, "你的指令");
//                }
//            }
//
//        } else if (iBaudRate == SerialService.CRUISE_BAUDRATE) {
//            //第三个串口
//        }
//    }
//}
