package com.tomorrowhi.thdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhaotaotao on 2017/5/10.
 * 后台远程服务
 */
public class BackGroundRemoteService extends Service {

    public BackGroundRemoteService() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // 在客户端连接服务端时，Stub通过ServiceConnection传递到客户端
        return stub;
    }

    // 实现接口中暴露给客户端的Stub--Stub继承自Binder，它实现了IBinder接口
    private IMyServiceAidlInterface.Stub stub = new IMyServiceAidlInterface.Stub() {

        private long countData;
        private Disposable subscribe;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getMessage() throws RemoteException {
            return "remote service 方法调用成功";
        }

        @Override
        public long getData() throws RemoteException {
            return countData;
        }

        @Override
        public void startThread() throws RemoteException {
            LogUtils.d("Local Service testThread：" + 0);
            subscribe = Flowable.intervalRange(0, 100, 1, 1, TimeUnit.SECONDS)
                    .doOnNext(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                        }
                    }).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                            countData = aLong;
//                            MyApplication.getInstance().getEventBus().post(new LocalServiceData(aLong));
                        }
                    });
        }

        @Override
        public void endThread() throws RemoteException {
            if (subscribe != null) {
                LogUtils.d("结束线程");
                subscribe.dispose();
            }
        }
    };


}
