package com.tomorrowhi.thdemo.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.f2prateek.rx.preferences2.Preference;
import com.tbruyelle.rxpermissions2.Permission;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.adapter.MainFunctionAdapter;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.MainFunctionBean;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.tomorrowhi.thdemo.interfaces.RecyclerViewClickListener;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String[] functions = {"EventBus 3.0 test", "高德地图", "Green Dao",
            "Retrofit", "RxJava", "RxPreferences", "RxBus",
            "单例模式", "google地图", "sensor", "service_test", "连接第三方平台", "录音测试", "广告测试"};
    private List<MainFunctionBean> functionBeanList = new ArrayList<>();
    private MainFunctionAdapter mainFunctionAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
    }

    @Override
    protected void initEvent() {
        mainFunctionAdapter.setItemClickListener(new RecyclerViewClickListener() {
            @Override
            public void itemClick(int position) {
                MainFunctionBean mainFunctionBean = functionBeanList.get(position);
                startFunction(mainFunctionBean);
            }

            @Override
            public void itemChildClick(int position) {

            }
        });
    }

    @Override
    protected void initData() {
        //初始化权限
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.RECORD_AUDIO)
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
                            LogUtils.d("getPhoneStr:" + PhoneUtils.getPhoneStatus());
                        } else {
                            LogUtils.d("拒绝的权限名：" + permission.name);
                        }
                    }
                });
        Preference<Long> appId = defaultRxPreferences.getLong(MyConstants.APP_ID);
        LogUtils.d("main Activity appId:" + appId.get());
    }

    @Override
    protected void initView() {

//        DialogUtil.progressDialog(mContext, "测试", true);
        LogUtils.d(TimeZone.getDefault().getRawOffset());

        for (int i = 0; i < functions.length; i++) {
            functionBeanList.add(new MainFunctionBean(functions[i], i));
        }

        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayout);
        mainFunctionAdapter = new MainFunctionAdapter(mContext);
        recyclerView.setAdapter(mainFunctionAdapter);
        mainFunctionAdapter.setList(functionBeanList);

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    private void startFunction(MainFunctionBean mainFunctionBean) {
        switch (mainFunctionBean.getId()) {
            case 0:
                //event bus
                startActivity(new Intent(this, EventBusTestActivity.class));
                break;
            case 1:
                //高德地图
                startActivity(new Intent(this, AMapFunctionActivity.class));
                break;
            case 2:
                //retrofit 2
                startActivity(new Intent(this, RetrofitActivity.class));
                break;
            case 3:
                //green dao
                ToastUtils.showShort("未完成");
                break;
            case 4:
                //RxJava 使用Demo
                startActivity(new Intent(this, RxJavaUseActivity.class));
                break;
            case 5:
                //RxPreferences
                startActivity(new Intent(this, RxPreferenceActivity.class));
                break;
            case 6:
                //RxBus
                ToastUtils.showShort("RxBus 需要适配RxJava 2.0");
                startActivity(new Intent(this, RxBusTestActivity.class));
                break;
            case 7:
                ToastUtils.showShort("单例模式的应用，请看代码");
                startActivity(new Intent(this, SingletonTestActivity.class));
                break;
            case 8:
                //Google地图
                startActivity(new Intent(this, GoogleMapFunctionActivity.class));
                break;
            case 9:
                //sensor 计步传感器
                startActivity(new Intent(this, SensorActivity.class));
                break;
            case 10:
                startActivity(new Intent(this, ServiceActivity.class));
                break;
            case 11:
                startActivity(new Intent(this, ConnectThirdPartyActivity.class));
                break;
            case 12:
                startActivity(new Intent(this, MyAudioRecordOne.class));
                break;
            case 13:
                break;
        }
    }

}
