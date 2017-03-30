package com.tomorrowhi.thdemo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.tomorrowhi.thdemo.util.PermissionUtil;
import com.tomorrowhi.thdemo.util.locationUtiils.LocationUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 09/01/2017.
 * 高德地图
 */
public class AmapMapActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnMapClickListener, AMap.InfoWindowAdapter {

    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;
    @BindView(R.id.a_map_map_view)
    MapView aMapMapView;
    @BindView(R.id.a_map_my_location)
    ImageView aMapMyLocation;
    @BindView(R.id.a_map_assign_points_navigation)
    ImageView aMapAssignPointsNavigation;
    @BindView(R.id.map_view_location_ll)
    LinearLayout mapViewLocationLl;
    @BindView(R.id.a_map_app_location_desc)
    TextView aMapAppLocationDesc;
    @BindView(R.id.a_map_bottom_desc)
    RelativeLayout aMapBottomDesc;

    private AMap aMap;
    private UiSettings mUiSettings;
    //定位
    private AMapLocationClient locationClient = null;
    //地图绘点Marker
    private MarkerOptions appMarkerOption, navigationMarkerOption;
    private LatLng appLatlng, navigationLatLng;
    private BitmapDescriptor bitmapDescriptor, navigationBitmapDescriptor;  //定位图标
    private GeocodeSearch geocoderSearch;
    private boolean initLocation = false, isShowWatch = true, isMapClick = true;
    private String addressName, navigationDesc;
    private ExecutorService mExecutorService;
    private Marker appMarker, navigationMarker;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_amap_map;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        initLocation = true;
        initLocation();

    }

    @Override
    protected void initView() {
        PermissionUtil.locationPermission(this);
        PermissionUtil.externalStoragePermission(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        aMapMapView.onCreate(savedInstanceState);
        geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(this);
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (aMap == null) {
            aMap = aMapMapView.getMap();
        }
        aMap.setInfoWindowAdapter(this);
        //aMap.set;
        mUiSettings = aMap.getUiSettings();
        //隐藏地图上的 + - 图标
        mUiSettings.setZoomControlsEnabled(false);
        setAppLocationIcon();
        setNavigationLocationIcon();

    }

    /**
     * 设置自定义的app定位图标
     */
    private void setAppLocationIcon() {
        View fenceLocationView = LayoutInflater.from(mContext).inflate(R.layout.app_location_layout, null);
        bitmapDescriptor = BitmapDescriptorFactory.fromView(fenceLocationView);
    }

    /**
     * 设置自定义的选择点的定位图标
     */
    private void setNavigationLocationIcon() {
        View fenceLocationView = LayoutInflater.from(mContext).inflate(R.layout.app_navigation_layout, null);
        navigationBitmapDescriptor = BitmapDescriptorFactory.fromView(fenceLocationView);
    }

    @OnClick({R.id.title_return_iv, R.id.a_map_my_location, R.id.a_map_assign_points_navigation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                finish();
                break;
            case R.id.a_map_my_location:
                //位置
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(appLatlng, LocationUtil.MAP_LEVEL), 700, new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case R.id.a_map_assign_points_navigation:
                //导航
                if (navigationLatLng == null || (navigationLatLng.longitude == 0 && navigationLatLng.latitude == 0)) {
                    ToastUtils.showShortToast("请在地图上选择导航点");
                    return;
                }
                if (appLatlng == null || (appLatlng.longitude == 0 && appLatlng.latitude == 0)) {
                    ToastUtils.showShortToast("正在定位，请稍后");
                    return;
                }
                Intent intent = new Intent(mContext, AMapNavigationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(MyConstants.NAVIGATION_LATLNG, navigationLatLng);
                bundle.putParcelable(MyConstants.APP_LOCATION_LATLNG, appLatlng);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void addAppMarkersToMap() {
        appMarkerOption.position(appLatlng).draggable(false);
        if (aMap == null) {
            aMap = aMapMapView.getMap();
        }
        assert aMap != null;
        appMarker = aMap.addMarker(appMarkerOption);
    }

    private void addNavigationMarkersToMap(LatLng latLonPoint, String formatAddress) {
        navigationMarkerOption
                .position(latLonPoint)
                .snippet(formatAddress)
                .draggable(true)
                .infoWindowEnable(true);
        if (aMap == null) {
            aMap = aMapMapView.getMap();
        }
        assert aMap != null;
        navigationMarker = aMap.addMarker(navigationMarkerOption);
    }

    private void showInfoWindow() {
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (Marker marker : mapScreenMarkers) {
            marker.showInfoWindow();
        }
    }

    private void startLocation() {
        // 启动定位
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("延时后，开始进行定位");
                if (locationClient.isStarted()) {
                    locationClient.startLocation();
                }
            }
        }, 2000);
        LogUtils.d("startLocation");
    }

    private void stopLocation() {
        //停止定位
        locationClient.stopLocation();
        LogUtils.d("stopLocation");
    }

    private void destoryLocation() {
        //销毁定位
        locationClient.onDestroy();
        LogUtils.d("destoryLocation");
    }


    private void initLocation() {
        navigationMarkerOption = new MarkerOptions().icon(navigationBitmapDescriptor);
        //初始化app坐标位置marker
        appMarkerOption = new MarkerOptions().icon(bitmapDescriptor);
        //初始化client
        locationClient = new AMapLocationClient(myApplication.applicationContext());
        //设置定位参数
        locationClient.setLocationOption(LocationUtil.getDefaultOption(false));
        // 设置定位监听
        locationClient.setLocationListener(appLocationListener);
    }

    /**
     * App连续定位监听，不设置移动到中心点
     */
    AMapLocationListener appLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                LogUtils.d("连续定位code：" + loc.getErrorCode() + "纬度：" + loc.getLatitude() + "经度：" + loc.getLongitude());
                //解析定位结果
                appLatlng = new LatLng(loc.getLatitude(), loc.getLongitude());
                if (appMarker == null) {
                    addAppMarkersToMap();
                } else {
                    appMarker.setPosition(appLatlng);
                }
                if (initLocation) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(appLatlng, LocationUtil.MAP_LEVEL));
                    initLocation = false;
                }
                getAddress(LocationUtil.convertToLatLonPoint(appLatlng));
                LogUtils.d("app location:" + appLatlng.toString());

            } else {
                LogUtils.d("定位失败");
            }
        }
    };

    /**
     * 获取指定坐标的逆地理位置编码
     *
     * @param latLonPoint 坐标
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 10,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    /**
     * 清除地图上所有的marker
     */
    public void clearAllMarkersToMap() {
        aMap.clear();
    }


    /**
     * 自定义地图Marker的显示view
     *
     * @param marker marker
     * @return view
     */
    private View getInfoWindowView(Marker marker) {
        LogUtils.d("getInfoWindowView:" + marker.toString());
        View inflate = this.getLayoutInflater().inflate(R.layout.map_marker_popwindow, null);
        renderLocationWindow(marker, inflate);
        return inflate;
    }

    private void renderLocationWindow(Marker marker, View inflate) {
        TextView markerWindowLocationDes = (TextView) inflate.findViewById(R.id.marker_window_location_des);
        markerWindowLocationDes.setText(marker.getSnippet());
    }

    /**
     * 响应逆地理编码的批量请求
     */
    private void getAddresses() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        final LatLonPoint latLonPoint = LocationUtil.convertToLatLonPoint(navigationLatLng);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 10,
                            GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    RegeocodeAddress result = geocoderSearch.getFromLocation(query);// 设置同步逆地理编码请求
                    if (result != null && result.getFormatAddress() != null) {
                        navigationDesc = result.getFormatAddress();
                        navigationMarker.setSnippet(navigationDesc);
                        LogUtils.d("轨迹界面，批量获取逆地理编码：" + result.getFormatAddress() + "，坐标：" + latLonPoint.toString());
                        navigationMarker.showInfoWindow();
                    }

                } catch (AMapException e) {
                    LogUtils.d("轨迹界面，批量获取逆地理编码异常，异常码：" + e.getErrorCode());
                }
            }
        });

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress();
                if (!TextUtils.isEmpty(addressName)) {
                    aMapAppLocationDesc.setText(addressName);
                    aMapBottomDesc.setVisibility(View.VISIBLE);
                } else {
                    aMapBottomDesc.setVisibility(View.GONE);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        aMapMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        aMapMapView.onResume();
        startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        aMapMapView.onPause();
        stopLocation();
    }

    @Override
    protected void onDestroy() {
        stopLocation();
        aMapMapView.onDestroy();
        destoryLocation();
        super.onDestroy();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        navigationLatLng = latLng;
        if (navigationMarker == null) {
            addNavigationMarkersToMap(navigationLatLng, "");
        } else {
            navigationMarker.setPosition(navigationLatLng);
        }
        getAddresses();

    }


    @Override
    public View getInfoWindow(Marker marker) {
        return getInfoWindowView(marker);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
