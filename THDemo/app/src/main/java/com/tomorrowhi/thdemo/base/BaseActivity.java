package com.tomorrowhi.thdemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tomorrowhi.thdemo.MyApplication;

/**
 * Created by zhaotaotao on 2016/11/7.
 * Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    public MyApplication myApplication;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        myApplication = MyApplication.getInstance();
        setContentView(getLayoutRes());
        init(savedInstanceState);
        initView();
        initData();
        initEvent();
        initComplete(savedInstanceState);
        myApplication.addActivity(this);
    }

    protected abstract int getLayoutRes();

    protected abstract void initComplete(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void init(Bundle savedInstanceState);


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApplication.removeActivity(this);
        myApplication.getEventBus().unregister(this);
    }

}
