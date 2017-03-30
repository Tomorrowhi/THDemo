package com.tomorrowhi.thdemo.activitys;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.EventBusTestCommonEvent;
import com.tomorrowhi.thdemo.bean.EventBusTestStickEvent;
import com.tomorrowhi.thdemo.fragments.EventBusFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 09/01/2017.
 * eventbus测试
 */
public class EventBusTestActivity extends BaseActivity {

    @BindView(R.id.event_bus_fragment_place)
    RelativeLayout eventBusFragmentPlace;
    @BindView(R.id.event_bus_send_event)
    Button eventBusSendEvent;
    @BindView(R.id.event_bus_send_stick_event)
    Button eventBusSendStickEvent;
    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;
    @BindView(R.id.event_bus_send_event_thread)
    Button eventBusSendEventThread;
    @BindView(R.id.event_bus_send_stick_event_thread)
    Button eventBusSendStickEventThread;

    private EventBusFragment eventBusFragment;
    private FragmentManager fragmentManager;
    private Fragment mCurrentFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_event_bus_test;
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
    protected void initView() {
        fragmentManager = getFragmentManager();
        eventBusFragment = EventBusFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.event_bus_fragment_place, eventBusFragment).commit();
        this.mCurrentFragment = eventBusFragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.event_bus_send_stick_event_thread, R.id.event_bus_send_event_thread,
            R.id.title_return_iv, R.id.event_bus_send_event, R.id.event_bus_send_stick_event})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.event_bus_send_event:
                myApplication.getEventBus().post(new EventBusTestCommonEvent("由Activity发送过来的消息"));
                LogUtils.d("发送了EventBus消息");
                break;
            case R.id.event_bus_send_event_thread:
                new Thread(new MyThread()).start();
                LogUtils.d("在子线程中发送了EventBus消息");
                break;
            case R.id.event_bus_send_stick_event:
                myApplication.getEventBus().postSticky(new EventBusTestStickEvent("由Activity发送过来的黏性消息"));
                LogUtils.d("发送了EventBus的黏性消息");
                startActivity(new Intent(this, ReceiverEventBusStickActivity.class));
                break;
            case R.id.event_bus_send_stick_event_thread:
                new Thread(new MyThreadStick()).start();
                startActivity(new Intent(this, ReceiverEventBusStickActivity.class));
                LogUtils.d("在子线程中发送了EventBus的黏性消息");
                break;
            case R.id.title_return_iv:
                this.finish();
                break;
        }
    }


    class MyThread implements Runnable {
        @Override
        public void run() {
            myApplication.getEventBus().post(new EventBusTestCommonEvent("由Activity在子线程中发送过来的消息"));
        }
    }

    class MyThreadStick implements Runnable {
        @Override
        public void run() {
            myApplication.getEventBus().postSticky(new EventBusTestStickEvent("由Activity在子线程中发送过来的黏性消息"));
        }
    }
}
