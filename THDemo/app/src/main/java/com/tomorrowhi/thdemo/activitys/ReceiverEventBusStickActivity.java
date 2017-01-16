package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.EventBusTestStickEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 09/01/2017.
 * event bus 黏性事件
 */
public class ReceiverEventBusStickActivity extends BaseActivity {

    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;
    @BindView(R.id.receiver_event_bus_stick_data)
    TextView receiverEventBusStickData;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_receiver_event_bus_stick;
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
        myApplication.getEventBus().register(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick(R.id.title_return_iv)
    public void onClick() {
        this.finish();
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 10, sticky = true)
    public void onMessageEventPostThread(EventBusTestStickEvent messageEvent) {
        if (messageEvent != null) {
            LogUtils.d("eventBus receiver stick POSTING" + messageEvent.toString());
        }
        LogUtils.d("eventBus receiver stick POSTING" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 11, sticky = true)
    public void onMessageEventMainThread(EventBusTestStickEvent messageEvent) {
        if (messageEvent != null) {
            receiverEventBusStickData.setText(messageEvent.getMsg());
        }
        LogUtils.d("eventBus receiver stick MAIN" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 12, sticky = true)
    public void onMessageEventBackgroundThread(EventBusTestStickEvent messageEvent) {
        if (messageEvent != null) {
            LogUtils.d("eventBus receiver stick BACKGROUND" + messageEvent.toString());
        }
        LogUtils.d("eventBus receiver stick BACKGROUND" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC, priority = 13, sticky = true)
    public void onMessageEventAsync(EventBusTestStickEvent messageEvent) {
        if (messageEvent != null) {
            LogUtils.d("eventBus receiver stick ASYNC" + messageEvent.toString());
        }
        LogUtils.d("eventBus receiver stick ASYNC" + Thread.currentThread().getName());
    }
}
