package com.tomorrowhi.thdemo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/5/10.
 * Service demo
 */
public class ServiceActivity extends BaseActivity {

    @BindView(R.id.title_return_iv)
    ImageButton mTitleReturnIv;
    @BindView(R.id.local_service)
    Button mLocalService;
    @BindView(R.id.remote_service)
    Button mRemoteService;
    @BindView(R.id.notification_service)
    Button mNotificationService;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_service;
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

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_return_iv, R.id.local_service, R.id.remote_service, R.id.notification_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.local_service:
                startActivity(new Intent(this,LocalServiceActivity.class));
                break;
            case R.id.remote_service:
                startActivity(new Intent(this,RemoteServiceActivity.class));
                break;
            case R.id.notification_service:
                startActivity(new Intent(this,NotificationServiceActivity.class));
                break;
        }
    }
}
