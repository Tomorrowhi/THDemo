package com.tomorrowhi.thdemo.util.RxBusUtil;

import android.annotation.TargetApi;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhaotaotao on 17/01/2017.
 */

public class SubscriberMethod {

    public Method method;
    public ThreadMode threadMode;
    public Class<?> eventType;
    public Object subscriber;
    public int code;

    public SubscriberMethod(Method method, ThreadMode threadMode, Class<?> eventType, Object subscriber, int code) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.subscriber = subscriber;
        this.code = code;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void invoke(Object o) {
        try {
            method.invoke(subscriber, o);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
