package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;
import android.widget.TextView;

import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by zhaotaotao on 17/01/2017.
 */

public class SingletonTestActivity extends BaseActivity {

    @BindView(R.id.hungry_loading_singleton)
    TextView hungryLoadingSingleton;
    @BindView(R.id.lazy_loading_singleton)
    TextView lazyLoadingSingleton;
    @BindView(R.id.enum_loading_singleton)
    TextView enumLoadingSingleton;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_singleton_test;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
//        SingletonHungryDemo.getINSTANCE();
        hungryLoadingSingleton.setText("public class SingletonHungryDemo {\n" +
                "\n" +
                "    private static final SingletonHungryDemo INSTANCE = new SingletonHungryDemo();\n" +
                "\n" +
                "    //私有化构造函数\n" +
                "\n" +
                "    private SingletonHungryDemo() {\n" +
                "    }\n" +
                "\n" +
                "    public static SingletonHungryDemo getINSTANCE() {\n" +
                "        return INSTANCE;\n" +
                "    }\n" +
                "}\n");
//        SingletonLazyDemo.getInstance();
        lazyLoadingSingleton.setText("public class SingletonLazyDemo {\n" +
                "    //声明为volatile, 利用其禁止指令重排序优化。保证写操作都先行发生于后面（时间上的顺序）对这个变量的读操作\n" +
                "    private volatile static SingletonLazyDemo INSTANCE;\n" +
                "\n" +
                "    private SingletonLazyDemo() {\n" +
                "    }\n" +
                "\n" +
                "    public static SingletonLazyDemo getInstance() {\n" +
                "        if (INSTANCE == null) {\n" +
                "            //两次判断INSTANCE 是为了避免多线程时操纵上的异常。synchronized保证线程安全\n" +
                "            synchronized (SingletonLazyDemo.class) {\n" +
                "                if (INSTANCE == null) {\n" +
                "                    INSTANCE = new SingletonLazyDemo();\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return INSTANCE;\n" +
                "    }\n" +
                "}");
//        SingletonEnumDemo instance = SingletonEnumDemo.INSTANCE;
        enumLoadingSingleton.setText("public enum SingletonEnumDemo {\n" +
                "    INSTANCE;\n" +
                "}");

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

}
