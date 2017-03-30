package com.tomorrowhi.thdemo.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.f2prateek.rx.preferences2.Preference;
import com.tbruyelle.rxpermissions2.Permission;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.common.MyConstants;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @BindView(R.id.event_bus_test_bt)
    Button eventBusTestBt;
    @BindView(R.id.a_map_test_bt)
    Button picassoTestBt;
    @BindView(R.id.green_dao_test_bt)
    Button greenDaoTestBt;
    @BindView(R.id.retrofit_test_bt)
    Button retrofitTestBt;
    @BindView(R.id.rx_Java_test_bt)
    Button rxJavaTestBt;
    @BindView(R.id.rx_preference_test_bt)
    Button rxPreferenceTestBt;
    @BindView(R.id.rx_bus_test_bt)
    Button rxBusTestBt;
    @BindView(R.id.singleton_test_bt)
    Button singletonTestBt;
    @BindView(R.id.google_map_test_bt)
    Button mGoogleMapTestBt;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        //初始化权限
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(@NonNull Boolean aBoolean) throws Exception {
                                   if (aBoolean) {
                                       //已经获取权限
                                   } else {
                                       //未获取权限
                                   }
                               }
                           }
                );
        rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
                        if (permission.granted) {
                            LogUtils.d("获取的权限名：" + permission.name);
                        } else {
                            LogUtils.d("拒绝的权限名：" + permission.name);
                        }
                    }
                });
        Preference<Long> appId = defaultRxPreferences.getLong(MyConstants.APP_ID);
        LogUtils.d("main Activity appId:" + appId.get());
        LogUtils.d("getPhoneStr:" + PhoneUtils.getPhoneStatus());
    }

    @Override
    protected void initView() {

//        DialogUtil.progressDialog(mContext, "测试", true);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }


    @OnClick({R.id.event_bus_test_bt, R.id.a_map_test_bt, R.id.rx_Java_test_bt,
            R.id.rx_preference_test_bt, R.id.singleton_test_bt, R.id.retrofit_test_bt,
            R.id.google_map_test_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.event_bus_test_bt:
                //event bus
                startActivity(new Intent(this, EventBusTestActivity.class));
                break;
            case R.id.a_map_test_bt:
                //高德地图
                startActivity(new Intent(this, AMapFunctionActivity.class));
                break;
            case R.id.retrofit_test_bt:
                //retrofit 2
                startActivity(new Intent(this, RetrofitActivity.class));
                break;
            case R.id.green_dao_test_bt:
                //green dao
                break;
            case R.id.rx_Java_test_bt:
                //RxJava 使用Demo
                startActivity(new Intent(this, RxJavaUseActivity.class));
                break;
            case R.id.rx_preference_test_bt:
                //RxPreferences
                startActivity(new Intent(this, RxPreferenceActivity.class));
                break;
            case R.id.rx_bus_test_bt:
                //RxBus
                ToastUtils.showShortToast("RxBus 需要适配RxJava 2.0");
//                startActivity(new Intent(this, RxBusTestActivity.class));
                break;
            case R.id.singleton_test_bt:
                ToastUtils.showShortToast("单例模式的应用，请看代码");
                startActivity(new Intent(this, SingletonTestActivity.class));
                break;
            case R.id.google_map_test_bt:
                //Google地图
                startActivity(new Intent(this, GoogleMapFunctionActivity.class));
                break;
        }
    }

}
