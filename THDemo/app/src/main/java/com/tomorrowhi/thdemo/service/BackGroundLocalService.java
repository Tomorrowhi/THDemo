package com.tomorrowhi.thdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.tomorrowhi.thdemo.MyApplication;
import com.tomorrowhi.thdemo.bean.event.LocalServiceData;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhaotaotao on 2017/5/10.
 * 后台本地服务
 */
public class BackGroundLocalService extends Service {

    private Disposable subscribe;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("Local Service onStartCommand");
        //bindService模式下，不会运行此处代码
        return super.onStartCommand(intent, flags, startId);
    }

    public void testThread(long defaultLong) {
        LogUtils.d("Local Service testThread："+defaultLong);
        subscribe = Flowable.intervalRange(defaultLong, 100, 1, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                    }
                }).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        MyApplication.getInstance().getEventBus().post(new LocalServiceData(aLong));
                    }
                });

    }

    public void endThread() {
        if (subscribe != null) {
            subscribe.dispose();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        endThread();
        super.onDestroy();
    }

    /**
     * IBinder是远程对象的基本接口，是为高性能而设计的轻量级远程调用机制的核心部分。但它不仅用于远程
     * 调用，也用于进程内调用。这个接口定义了与远程对象交互的协议。
     * 不要直接实现这个接口，而应该从Binder派生。
     */
    public class MyBinder extends Binder {

        public BackGroundLocalService getService() {
            return BackGroundLocalService.this;
        }

    }
}
