package com.tomorrowhi.thdemo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseFragment;
import com.tomorrowhi.thdemo.bean.EventBusTestCommonEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhaotaotao on 09/01/2017.
 * EventBusTest
 */
public class EventBusFragment extends BaseFragment {

    @BindView(R.id.event_bus_receive_data)
    TextView eventBusReceiveData;

    public static EventBusFragment newInstance() {
        return new EventBusFragment();
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        myApplication.getEventBus().register(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_event_bus_test;
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 10, sticky = false)
    public void onMessageEventPostThread(EventBusTestCommonEvent messageEvent) {
        if (messageEvent != null) {
            LogUtils.d("eventBus receiver POSTING" + messageEvent.toString());
        }
        LogUtils.d("eventBus receiver POSTING" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 11, sticky = false)
    public void onMessageEventMainThread(EventBusTestCommonEvent messageEvent) {
        if (messageEvent != null) {
            eventBusReceiveData.setText(messageEvent.getMsg());
        }
        LogUtils.d("eventBus receiver MAIN" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 12, sticky = false)
    public void onMessageEventBackgroundThread(EventBusTestCommonEvent messageEvent) {
        if (messageEvent != null) {
            LogUtils.d("eventBus receiver BACKGROUND" + messageEvent.toString());
        }
        LogUtils.d("eventBus receiver BACKGROUND" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC, priority = 13, sticky = false)
    public void onMessageEventAsync(EventBusTestCommonEvent messageEvent) {
        if (messageEvent != null) {
            LogUtils.d("eventBus receiver ASYNC" + messageEvent.toString());
        }
        LogUtils.d("eventBus receiver ASYNC" + Thread.currentThread().getName());
    }


}
