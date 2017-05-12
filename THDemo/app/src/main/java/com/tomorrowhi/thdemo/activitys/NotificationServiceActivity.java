package com.tomorrowhi.thdemo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.event.NotificationServiceData;
import com.tomorrowhi.thdemo.service.BackGroundNotificationService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/5/10.
 */

public class NotificationServiceActivity extends BaseActivity {

    @BindView(R.id.title_return_iv)
    ImageButton mTitleReturnIv;
    @BindView(R.id.start_service)
    Button mStartService;
    @BindView(R.id.stop_service)
    Button mStopService;
    @BindView(R.id.service_log)
    TextView mServiceLog;

    private Intent intent;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_notification_service;
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

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        myApplication.getEventBus().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_return_iv, R.id.start_service, R.id.stop_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.start_service:
                intent = new Intent(this, BackGroundNotificationService.class);
                startService(intent);
                break;
            case R.id.stop_service:
                if (intent != null) {
                    stopService(intent);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 22, sticky = false)
    public void getUpdataData(NotificationServiceData result) {
        if (result != null) {
            mServiceLog.setText(String.valueOf(result.getaLong()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intent != null) {
            stopService(intent);
        }
    }
}
