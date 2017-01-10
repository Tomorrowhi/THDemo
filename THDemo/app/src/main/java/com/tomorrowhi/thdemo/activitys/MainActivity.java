package com.tomorrowhi.thdemo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.util.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.event_bus_test_bt)
    Button eventBusTestBt;
    @BindView(R.id.a_map_test_bt)
    Button picassoTestBt;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
    }

    @Override
    protected void initEvent() {
        DialogUtil.progressDialog(mContext, "测试", true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.event_bus_test_bt, R.id.a_map_test_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.event_bus_test_bt:
                startActivity(new Intent(this, EventBusTestActivity.class));
                break;
            case R.id.a_map_test_bt:
                //高德地图
                startActivity(new Intent(this, AMapFunctionActivity.class));
                break;
        }
    }
}
