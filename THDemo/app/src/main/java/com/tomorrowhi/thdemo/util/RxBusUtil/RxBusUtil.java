package com.tomorrowhi.thdemo.util.RxBusUtil;

import android.os.Message;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

/**
 * Created by zhaotaotao on 17/01/2017.
 * RxBus
 */

public class RxBusUtil {

    private static volatile RxBusUtil mDefaultInstance;

    private Map<Class, List<Subscription>> subscriptionByEventType = new HashMap<>();

    private Map<Object, List<Class>> eventTypesBySubscriber = new HashMap<>();

    private Map<Class, List<SubscriberMethod>> subscriberMethodByEventType = new HashMap<>();

    private final Subject bus;  //主题

    private RxBusUtil() {
        //PublishSubject 只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
//        bus = new SerializedSubject<>(PublishSubject.create());
        bus = null;
    }

    public static RxBusUtil getDefault() {
        RxBusUtil rxBus = mDefaultInstance;
        if (RxBusUtil.mDefaultInstance == null) {
            synchronized (RxBusUtil.class) {
                rxBus = mDefaultInstance;
                if (mDefaultInstance == null) {
                    rxBus = new RxBusUtil();
                    mDefaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 提供一个新的事件，单一类型
     *
     * @param o 事件数据
     */
    public void post(Object o) {
        bus.onNext(o);
    }


    /**
     * 根据传递的eventType 类型返回特定类型（eventType）的被观察者
     *
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> tObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

}
