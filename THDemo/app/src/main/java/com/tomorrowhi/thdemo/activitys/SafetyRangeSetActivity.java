package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.utils.LogUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.util.locationUtiils.LocationUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by zhaotaotao on 2016/11/21.
 * 设置安全区
 */
public class SafetyRangeSetActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnMapClickListener {

    @BindView(R.id.title_return_iv)
    ImageButton returnIv;
    @BindView(R.id.safe_set_family)
    ImageView safeSetFamily;
    @BindView(R.id.safe_set_location_des)
    ImageView safeSetLocationDes;
    @BindView(R.id.safe_set_range_name)
    EditText safeSetRangeName;
    @BindView(R.id.safe_set_range_des)
    TextView safeSetRangeDes;
    @BindView(R.id.safe_range_radius_bt)
    Button safeRangeRadiusBt;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.save_safe_range)
    Button saveSafeRange;


    private OptionsPickerView<Integer> pvOptions;
    private ArrayList<Integer> range = new ArrayList<>();
    int[] radius = {200, 300, 400, 500, 600, 700, 800, 900, 1000, 1500, 2000};
    private int index = 3, defaultRadius = radius[index];
    private AMap aMap;
    private UiSettings mUiSettings;
    //地图绘点Marker
    private MarkerOptions watchMarkerOption;
    private LatLng watchLatlng;
    private GeocodeSearch geocoderSearch;
    private String addressName;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_safe_set;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    private void initMap(Bundle savedInstanceState) {
        //绑定mapView的生命周期
        mapView.onCreate(savedInstanceState);
        //设置默认的Latlng
        watchLatlng = new LatLng(22.571097, 113.861000);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        //aMap.set;
        mUiSettings = aMap.getUiSettings();
        //隐藏地图上的 + - 图标
        mUiSettings.setZoomControlsEnabled(true);
        //初始化手表坐标位置marker
        watchMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.img_current_location));
        //移动中心点
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(watchLatlng, LocationUtil.MAP_LEVEL), 500, new AMap.CancelableCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }
        });
        geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(this);
        createCircle(watchLatlng, defaultRadius);
    }


    @Override
    protected void initEvent() {
        aMap.setOnMapClickListener(this);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                defaultRadius = range.get(options1);
                safeRangeRadiusBt.setText(getString(R.string.safe_set_radius_bt, defaultRadius));
                createCircle(watchLatlng, defaultRadius);
            }
        });
    }

    @Override
    protected void initData() {
        for (int r : radius) {
            range.add(r);
        }
        pvOptions = new OptionsPickerView<>(this);
        pvOptions.setTitle(getString(R.string.safe_set_zone_range_desc));
        pvOptions.setPicker(range);
        pvOptions.setLabels(getString(R.string.safe_set_meter));
        pvOptions.setCyclic(false);
        pvOptions.setSelectOptions(index);
        safeRangeRadiusBt.setText(getString(R.string.safe_set_radius_bt, range.get(index)));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initMap(savedInstanceState);
    }

    /**
     * 在地图上添加marker
     */
    private void addWatchMarkersToMap() {
        watchMarkerOption.position(this.watchLatlng).draggable(false);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        assert aMap != null;
        aMap.addMarker(watchMarkerOption);
    }

    /**
     * 创建圆
     *
     * @param latLng 地理坐标
     * @param radius 半径
     */
    public void createCircle(LatLng latLng, int radius) {
        aMap.clear();
        addWatchMarkersToMap();
        getAddress(LocationUtil.convertToLatLonPoint(latLng));
        // 添加圆
        CircleOptions ooCircle = new CircleOptions().center(latLng)
                .strokeColor(ContextCompat.getColor(mContext, R.color.blue_7))
                .strokeWidth(1).fillColor(ContextCompat.getColor(mContext, R.color.blue_8))
                .zIndex(LocationUtil.MAP_LEVEL).radius(radius);
        aMap.addCircle(ooCircle);
    }

    @OnClick({R.id.save_safe_range, R.id.title_return_iv, R.id.safe_range_radius_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.safe_range_radius_bt:
                if (pvOptions.isShowing()) {
                    pvOptions.dismiss();
                } else {
                    pvOptions.show();
                }
                break;
            case R.id.save_safe_range:
                //保存安全区

                break;
        }
    }


    /**
     * 获取指定坐标的逆地理位置编码
     *
     * @param latLonPoint 坐标
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        //设置坐标、逆地理编码的精确度、坐标系格式
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 10, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress();
                if (result.getRegeocodeQuery().getPoint().getLatitude() == watchLatlng.latitude
                        && result.getRegeocodeQuery().getPoint().getLongitude() == watchLatlng.longitude) {
                    safeSetRangeDes.setText(addressName);
                }
                LogUtils.d("轨迹界面，单条逆地理编码：" + addressName);
            } else {
                LogUtils.d("轨迹界面，单条获取逆地理编码异常");
            }
        } else {
            LogUtils.d("轨迹界面，单条获取逆地理编码异常，异常码：" + rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        watchLatlng = latLng;
        addWatchMarkersToMap();
        createCircle(watchLatlng, defaultRadius);
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
