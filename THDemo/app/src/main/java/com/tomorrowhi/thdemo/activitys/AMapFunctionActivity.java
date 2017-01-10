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
 * Created by zhaotaotao on 10/01/2017.
 * 高德地图功能举例
 */
public class AMapFunctionActivity extends BaseActivity {

    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;
    @BindView(R.id.amap_location_and_navigation)
    Button amapLocationAndNavigation;
    @BindView(R.id.amap_locus_history)
    Button amapLocusHistory;
    @BindView(R.id.amap_geography)
    Button amapGeography;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_amap_function;
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
        ButterKnife.bind(this);
    }


    @OnClick({R.id.title_return_iv, R.id.amap_location_and_navigation, R.id.amap_locus_history, R.id.amap_geography})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.amap_location_and_navigation:
                startActivity(new Intent(this, AmapMapActivity.class));
                break;
            case R.id.amap_locus_history:
                startActivity(new Intent(this, AMapLocusActivity.class));
                break;
            case R.id.amap_geography:
                startActivity(new Intent(this, SafetyRangeSetActivity.class));
                break;
        }
    }
}
