package com.tomorrowhi.thdemo.util;

/**
 * Created by zhaotaotao on 17/01/2017.
 * 懒汉式加载
 */

public class SingletonLazyDemo {
    //声明为volatile, 利用其禁止指令重排序优化。保证写操作都先行发生于后面（时间上的顺序）对这个变量的读操作
    private volatile static SingletonLazyDemo INSTANCE;

    private SingletonLazyDemo() {
    }

    public static SingletonLazyDemo getInstance() {
        if (INSTANCE == null) {
            //两次判断INSTANCE 是为了避免多线程时操纵上的异常。synchronized保证线程安全
            synchronized (SingletonLazyDemo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SingletonLazyDemo();
                }
            }
        }
        return INSTANCE;
    }
}
