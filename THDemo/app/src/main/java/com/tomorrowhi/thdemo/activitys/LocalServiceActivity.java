package com.tomorrowhi.thdemo.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.event.LocalServiceData;
import com.tomorrowhi.thdemo.service.BackGroundLocalService;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhaotaotao on 2017/5/10.
 */

public class LocalServiceActivity extends BaseActivity {

    @BindView(R.id.start_service)
    Button mStartService;
    @BindView(R.id.stop_service)
    Button mStopService;
    @BindView(R.id.service_log)
    TextView mServiceLog;
    @BindView(R.id.title_return_iv)
    ImageButton mTitleReturnIv;

    private ServiceConnection mServiceConnection;
    private BackGroundLocalService backGroundLocalService;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_local_service;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //建立连接
                //获取服务的操作对象
                BackGroundLocalService.MyBinder binder = (BackGroundLocalService.MyBinder) service;
                //获得Service
                backGroundLocalService = binder.getService();
                String logNum = mServiceLog.getText().toString();
                Long defaultNum;
                if (EmptyUtils.isEmpty(logNum)) {
                    defaultNum = 0L;
                } else {
                    defaultNum = Long.valueOf(logNum);
                }
                backGroundLocalService.testThread(defaultNum);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ToastUtils.showShort("连接断开");
            }
        };
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

    @OnClick({R.id.start_service, R.id.stop_service, R.id.title_return_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.start_service:
                if (backGroundLocalService != null) {
                    String logNum = mServiceLog.getText().toString();
                    Long defaultNum;
                    if (EmptyUtils.isEmpty(logNum)) {
                        defaultNum = 0L;
                    } else {
                        defaultNum = Long.valueOf(logNum);
                    }
                    backGroundLocalService.testThread(defaultNum);
                    ToastUtils.showShort("服务已经启动");
                    break;
                }
                Intent intent = new Intent(this, BackGroundLocalService.class);
                //绑定服务
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

                break;
            case R.id.stop_service:
                if (backGroundLocalService != null) {
                    backGroundLocalService.endThread();
                } else {
                    ToastUtils.showShort("服务未启动");
                }
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 22, sticky = false)
    public void getUpdataData(LocalServiceData result) {
        if (result != null) {
            mServiceLog.setText(String.valueOf(result.getaLong()));
        }
    }

    @Override
    protected void onDestroy() {
        if (mServiceConnection != null)
            unbindService(mServiceConnection);
        super.onDestroy();
    }
}
