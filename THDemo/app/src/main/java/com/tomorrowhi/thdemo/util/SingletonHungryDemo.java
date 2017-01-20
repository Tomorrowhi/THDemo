package com.tomorrowhi.thdemo.util;

/**
 * Created by zhaotaotao on 17/01/2017.
 * <p>
 * 单例模式 饿汉式加载
 */

public class SingletonHungryDemo {

    private static final SingletonHungryDemo INSTANCE = new SingletonHungryDemo();

    //私有化构造函数
    private SingletonHungryDemo() {
    }

    public static SingletonHungryDemo getINSTANCE() {
        return INSTANCE;
    }
}
