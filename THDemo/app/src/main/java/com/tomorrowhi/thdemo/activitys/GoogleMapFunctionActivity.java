package com.tomorrowhi.thdemo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.LocusPointBean;
import com.tomorrowhi.thdemo.common.MyConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/3/25.
 * googleMap功能测试
 */
public class GoogleMapFunctionActivity extends BaseActivity {

    @BindView(R.id.title_return_iv)
    ImageButton mTitleReturnIv;
    @BindView(R.id.china_point_data)
    Button mChinaPointData;
    @BindView(R.id.china_out_data)
    Button mChinaOutData;
    @BindView(R.id.china_border_data)
    Button mChinaBorderData;

    private ArrayList<LocusPointBean> chinaPoint = new ArrayList<>();
    private ArrayList<LocusPointBean> chinaOutPoint = new ArrayList<>();
    private ArrayList<LocusPointBean> chinaBorderPoint = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_google_function;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        setPointData();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    private void setPointData() {
        //国外
        chinaOutPoint.add(new LocusPointBean(13.692893, 100.538788, false));
        chinaOutPoint.add(new LocusPointBean(13.693461, 100.540514, false));
        chinaOutPoint.add(new LocusPointBean(13.694631, 100.543064, true));
        chinaOutPoint.add(new LocusPointBean(13.695168, 100.544748, false));
        chinaOutPoint.add(new LocusPointBean(13.692676, 100.547647, false));
        chinaOutPoint.add(new LocusPointBean(13.696009, 100.546608, true));
        chinaOutPoint.add(new LocusPointBean(13.700332, 100.546065, false));
        chinaOutPoint.add(new LocusPointBean(13.701332, 100.546065, false));
        chinaOutPoint.add(new LocusPointBean(13.701332, 100.546065, false));
        chinaOutPoint.add(new LocusPointBean(13.701332, 100.546065, false));
        //中国
        chinaPoint.add(new LocusPointBean(22.565775, 113.868695, false));
        chinaPoint.add(new LocusPointBean(22.566703, 113.869076, false));
        chinaPoint.add(new LocusPointBean(22.568107, 113.870047, true));
        chinaPoint.add(new LocusPointBean(22.568829, 113.870514, false));
        chinaPoint.add(new LocusPointBean(22.56936, 113.870225, false));
        chinaPoint.add(new LocusPointBean(22.569847, 113.869535, true));
        chinaPoint.add(new LocusPointBean(22.570538, 113.868739, false));
        chinaPoint.add(new LocusPointBean(22.571099, 113.86801, true));
        chinaPoint.add(new LocusPointBean(22.573148, 113.866793, false));
        chinaPoint.add(new LocusPointBean(22.574148, 113.866793, false));
        chinaPoint.add(new LocusPointBean(22.575148, 113.866793, false));
        chinaPoint.add(new LocusPointBean(22.576148, 113.866793, false));
        //中国边界
        chinaBorderPoint.add(new LocusPointBean(22.694338, 106.868702, false));
        chinaBorderPoint.add(new LocusPointBean(22.670798, 106.834681, false));
        chinaBorderPoint.add(new LocusPointBean(22.618225, 106.8095, false));
        chinaBorderPoint.add(new LocusPointBean(22.603809, 106.762122, true));
        chinaBorderPoint.add(new LocusPointBean(22.62588, 106.698983, false));
        chinaBorderPoint.add(new LocusPointBean(22.646304, 106.614021, false));
        chinaBorderPoint.add(new LocusPointBean(22.632575, 106.527364, false));
        chinaBorderPoint.add(new LocusPointBean(22.633575, 106.527364, false));
        chinaBorderPoint.add(new LocusPointBean(22.63475, 106.527364, false));
        chinaBorderPoint.add(new LocusPointBean(22.635575, 106.527364, false));
        chinaBorderPoint.add(new LocusPointBean(22.636575, 106.527364, false));
    }


    @OnClick({R.id.title_return_iv, R.id.china_point_data, R.id.china_out_data, R.id.china_border_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.china_point_data:
                startLocusActivity(chinaPoint);
                break;
            case R.id.china_out_data:
                startLocusActivity(chinaOutPoint);
                break;
            case R.id.china_border_data:
                startLocusActivity(chinaBorderPoint);
                break;
        }
    }

    private void startLocusActivity(ArrayList<LocusPointBean> arrayList) {
        Intent intent = new Intent(this, GoogleMapTestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MyConstants.POINT_LIST, arrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
