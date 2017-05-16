package com.tomorrowhi.thdemo.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.tomorrowhi.thdemo.service.IMyServiceAidlInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/5/10.
 */

public class RemoteServiceActivity extends BaseActivity {

    @BindView(R.id.title_return_iv)
    ImageButton mTitleReturnIv;
    @BindView(R.id.read_data)
    Button mReadData;
    @BindView(R.id.service_log)
    TextView mServiceLog;
    @BindView(R.id.stop_service)
    Button mStopService;

    //定义接口变量
    private IMyServiceAidlInterface mIMyServiceAidlInterface;
    private ServiceConnection mConnection;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_remote_server;
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
//        myApplication.getEventBus().register(this);
        bindRemoteService();
    }

    private void bindRemoteService() {
        Intent intent = new Intent();
        intent.setClassName(this, MyConstants.REMOTE_SERVICE_FLAG);

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //从连接中获取stub对象
                mIMyServiceAidlInterface = IMyServiceAidlInterface.Stub.asInterface(service);
                //调用Remote Service提供的方法
                try {
                    LogUtils.d("远程服务器获取到的消息：" + mIMyServiceAidlInterface.getMessage());
                    mIMyServiceAidlInterface.startThread();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                onViewClicked(mStopService);
                mIMyServiceAidlInterface = null;
            }
        };
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @OnClick({R.id.title_return_iv, R.id.read_data, R.id.stop_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.read_data:
                if (mIMyServiceAidlInterface != null) {
                    try {
                        mServiceLog.setText(String.valueOf(mIMyServiceAidlInterface.getData()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.stop_service:
                if (mIMyServiceAidlInterface != null) {
                    try {
                        mIMyServiceAidlInterface.endThread();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 22, sticky = false)
//    public void getUpdataData(NotificationServiceData result) {
//        if (result != null) {
//
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onViewClicked(mStopService);
        //解绑
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
