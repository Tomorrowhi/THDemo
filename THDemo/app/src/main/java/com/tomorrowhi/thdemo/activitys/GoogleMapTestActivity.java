package com.tomorrowhi.thdemo.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.PatternItem;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.LocusPointBean;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.tomorrowhi.thdemo.util.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoogleMapTestActivity extends BaseActivity implements OnMapReadyCallback, GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.title_return_iv)
    ImageButton mTitleReturnIv;
    @BindView(R.id.map_container)
    LinearLayout mMapContainer;
    @BindView(R.id.change_map_bt)
    Button mChangeMapBt;

    //google
    private GoogleMap googlemap;
    private MapView mGoogleMapView;
    private com.google.android.gms.maps.model.BitmapDescriptor googleBitmapDescriptor, googleBitmapDescriptorStart,
            googleBitmapDescriptorEnd;
    private com.google.android.gms.maps.model.LatLngBounds latLngBounds;
    private com.google.android.gms.maps.model.PolylineOptions polylineOptions;
    private List<com.google.android.gms.maps.model.MarkerOptions> mMarkerOptionsList = new ArrayList<>();
    private List<com.google.android.gms.maps.model.LatLng> googleLatLng = new ArrayList<>();
    private static final int PATTERN_DASH_LENGTH_PX = 50;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final Dash DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_DASHED = Arrays.asList(DASH, GAP);
    private List<com.google.android.gms.maps.model.PolylineOptions> mOptionsBooleanList = new ArrayList<>();
    //高德
    private TextureMapView mAMapView;
    private AMap aMap;
    private PolylineOptions options;
    private GeocodeSearch geocoderSearch;
    private MarkerOptions watchMarkerOptionStart, watchMarkerOptionEnd, watchMarkerOptionPoint;
    private Marker startmarker, endMarker, redMarker;

    //公共

    private List<LocusPointBean> nowUsePoint = new ArrayList<>();
    private LinearLayout.LayoutParams mParams;
    private View pointView, watchMarkerViewStart, watchMarkerViewEnd;
    private float zoom = 10;
    private double latitude = 39.23242;
    private double longitude = 116.253654;
    private boolean mIsAmapDisplay = true;  //标记当前使用的是那个地图
    private boolean mIsAuto = true;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_google_map;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        nowUsePoint = getIntent().getParcelableArrayListExtra(MyConstants.POINT_LIST);
        if (nowUsePoint == null) {
            ToastUtils.showShortToast("传递的坐标点列表为null");
            return;
        } else {
            for (LocusPointBean bean : nowUsePoint) {
                googleLatLng.add(new com.google.android.gms.maps.model.LatLng(bean.getLat(), bean.getLng()));
            }
        }
        getPointDesc(nowUsePoint);
    }


    @Override
    protected void initView() {
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googlemap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (mMarkerOptionsList.size() > 0) {
            for (com.google.android.gms.maps.model.MarkerOptions markerOptions : mMarkerOptionsList) {
                googlemap.addMarker(markerOptions);
            }
        }
        if (googleLatLng.size() > 0) {
            latLngBounds = googleGetLatLngBounds(
                    googleLatLng.get(googleLatLng.size() - 1),
                    googleLatLng);
            googlemap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds(latLngBounds,
                    ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(), SizeUtils.dp2px(50)));
        }
        if (mOptionsBooleanList.size() > 0) {
            for (com.google.android.gms.maps.model.PolylineOptions polylineOptions : mOptionsBooleanList) {
                googlemap.addPolyline(polylineOptions);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAMapView != null) {
            mAMapView.onResume();
        }
        if (mGoogleMapView != null) {
            try {
                mGoogleMapView.onResume();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAMapView != null) {
            mAMapView.onPause();
        }
        if (mGoogleMapView != null) {
            try {
                mGoogleMapView.onPause();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAMapView != null) {
            mAMapView.onSaveInstanceState(outState);
        }
        if (mGoogleMapView != null) {
            try {
                mGoogleMapView.onSaveInstanceState(outState);
            } catch (Exception e) {
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAMapView != null) {
            mAMapView.onDestroy();
        }
        if (mGoogleMapView != null) {
            try {
                mGoogleMapView.onDestroy();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 使用高德地图
     *
     * @param beanList beanList
     */
    private void changeToAmapView(List<LocusPointBean> beanList) {
        //是否需要记录当前点
        boolean isRemberPonint = false;
        if (googlemap != null) {
            zoom = googlemap.getCameraPosition().zoom;
        }
        latitude = beanList.get(0).getLat();
        longitude = beanList.get(0).getLng();
        mAMapView = new TextureMapView(this, new AMapOptions()
                .camera(new CameraPosition(new LatLng(latitude, longitude), zoom, 0, 0)));
        mAMapView.onCreate(null);
        mAMapView.onResume();
        mMapContainer.addView(mAMapView, mParams);
        aMap = mAMapView.getMap();
        //初始化要绘制的元素
        setWatchMarkerOptionPointIcon();
        List<LatLng> latLngItem = new ArrayList<>();
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < beanList.size(); i++) {
            LocusPointBean bean = beanList.get(i);
            LatLng latLng = new LatLng(bean.getLat(), bean.getLng());
            latLngs.add(latLng);
            if (i == 0) {
                //第一点，创建一个options
                options = new PolylineOptions();
                options.width(10).color(ContextCompat.getColor(mContext, R.color.purple_2));
            } else if (i == (beanList.size() - 1)) {
                //最后一点
            } else {
                //中间点
                addWatchMarkersPointToMap(bean);
            }
            latLngItem.add(latLng);
            if (isRemberPonint) {
                //将当前点与上一点，画一条虚线
                options.setPoints(latLngItem);
                latLngItem = new ArrayList<>();
                latLngItem.add(latLng);
                //画虚线
                options.setDottedLine(true);
                aMap.addPolyline(options);
                options = new PolylineOptions();
                options.width(10).color(ContextCompat.getColor(mContext, R.color.purple_2));
                isRemberPonint = false;
            }
            if (bean.isOver()) {
                //true，代表结束，与下一个点之间的连线为虚线
                options.setPoints(latLngItem);
                latLngItem = new ArrayList<>();
                latLngItem.add(latLng);
                //画实线
                options.setDottedLine(false);
                aMap.addPolyline(options);
                options = new PolylineOptions();
                options.width(10).color(ContextCompat.getColor(mContext, R.color.purple_2));
                isRemberPonint = true;
            } else {
                //实线
                isRemberPonint = false;
            }

        }
        options.setPoints(latLngItem);
        aMap.addPolyline(options);
        addWatchMarkersEndToMap(beanList.get(beanList.size() - 1));
        addWatchMarkersStartToMap(beanList.get(0));
        zoomToSpanWithCenter(endMarker, latLngs.get(latLngs.size() - 1), latLngs);
        mGoogleMapView.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mGoogleMapView.setVisibility(View.GONE);
                mMapContainer.removeView(mGoogleMapView);
                if (mGoogleMapView != null) {
                    mGoogleMapView.onDestroy();
                }
            }
        });
        mIsAmapDisplay = true;
    }

    private void changeToGoogleMapView(List<LocusPointBean> beanList) {
        boolean isRemberPonint = false;
        if (mAMapView != null) {
            zoom = mAMapView.getMap().getCameraPosition().zoom;
        }
        latitude = beanList.get(0).getLat();
        longitude = beanList.get(0).getLng();
        mGoogleMapView = new MapView(this, new GoogleMapOptions()
                .camera(new com.google.android.gms.maps.model
                        .CameraPosition(new com.google.android.gms.maps.model.LatLng(latitude, longitude), zoom, 0, 0)));
        mGoogleMapView.onCreate(null);
        mGoogleMapView.onResume();
        mMapContainer.addView(mGoogleMapView, mParams);
        mGoogleMapView.getMapAsync(this);
        //开始在google地图上画线
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.app_point_layout, null);
        View inflate1 = LayoutInflater.from(mContext).inflate(R.layout.app_watch_marker_start_layout, null);
        View inflate2 = LayoutInflater.from(mContext).inflate(R.layout.app_watch_marker_end_layout, null);
        googleBitmapDescriptor = com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(Tools.convertViewToBitmap(inflate));
        googleBitmapDescriptorStart = com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(Tools.convertViewToBitmap(inflate1));
        googleBitmapDescriptorEnd = com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(Tools.convertViewToBitmap(inflate2));
        List<com.google.android.gms.maps.model.LatLng> latLngItem = new ArrayList<>();
        mMarkerOptionsList.clear();
        mOptionsBooleanList.clear();
        for (int i = 0; i < beanList.size(); i++) {
            LocusPointBean bean = beanList.get(i);
            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(bean.getLat(), bean.getLng());
            setMarkerOptions(beanList, i, latLng);
            if (i == 0) {
                polylineOptions = new com.google.android.gms.maps.model.PolylineOptions();
                polylineOptions.width(10).color(ContextCompat.getColor(mContext, R.color.purple_2));
            }
            latLngItem.add(latLng);
            if (isRemberPonint) {
                //将当前点与上一点，画一条虚线
                polylineOptions.addAll(latLngItem);
                latLngItem = new ArrayList<>();
                latLngItem.add(latLng);
                //画虚线
                polylineOptions.pattern(PATTERN_DASHED);
                mOptionsBooleanList.add(polylineOptions);
                polylineOptions = new com.google.android.gms.maps.model.PolylineOptions();
                polylineOptions.width(10).color(ContextCompat.getColor(mContext, R.color.purple_2));
                isRemberPonint = false;
            }
            if (bean.isOver()) {
                //true，代表结束，与下一个点之间的连线为虚线
                polylineOptions.addAll(latLngItem);
                latLngItem = new ArrayList<>();
                latLngItem.add(latLng);
                //画实线
                polylineOptions.pattern(null);
                mOptionsBooleanList.add(polylineOptions);
                polylineOptions = new com.google.android.gms.maps.model.PolylineOptions();
                polylineOptions.width(10).color(ContextCompat.getColor(mContext, R.color.purple_2));
                isRemberPonint = true;
            } else {
                //实线
                isRemberPonint = false;
            }
        }
        polylineOptions.addAll(latLngItem);
        mOptionsBooleanList.add(polylineOptions);
        //此处延时设置为1秒，避免切换到google地图时出现黑屏
        handler.sendEmptyMessageDelayed(0, 1000);
        mIsAmapDisplay = false;
    }

    private void setMarkerOptions(List<LocusPointBean> beanList, int i, com.google.android.gms.maps.model.LatLng latLng) {
        com.google.android.gms.maps.model.MarkerOptions markerOptions;
        if (i == 0) {
            //第一点
            markerOptions = new com.google.android.gms.maps.model.MarkerOptions()
                    .icon(googleBitmapDescriptorStart)
                    .position(latLng)
                    .draggable(false);
        } else if (i == (beanList.size() - 1)) {
            //最后一点
            markerOptions = new com.google.android.gms.maps.model.MarkerOptions()
                    .icon(googleBitmapDescriptorEnd)
                    .position(latLng)
                    .draggable(false);
        } else {
            //中间点
            markerOptions = new com.google.android.gms.maps.model.MarkerOptions()
                    .icon(googleBitmapDescriptor)
                    .position(latLng)
                    .draggable(false)
                    .anchor(0.5f, 0.5f);

        }
        mMarkerOptionsList.add(markerOptions);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (mAMapView == null) {
                return;
            }
            mAMapView.setVisibility(View.GONE);
            mMapContainer.removeView(mAMapView);
            if (mAMapView != null) {
                mAMapView.onDestroy();
            }
            if (aMap != null) {
                aMap.clear();
            }
        }
    };

    @OnClick({R.id.title_return_iv, R.id.change_map_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.change_map_bt:
                if (mIsAmapDisplay) {
                    changeToGoogleMapView(nowUsePoint);
                } else {
                    changeToAmapView(nowUsePoint);
                }
                break;
        }
    }

    private void getPointDesc(List<LocusPointBean> beanList) {
        LocusPointBean locusPointBean = beanList.get(0);
        getAddress(new LatLonPoint(locusPointBean.getLat(), locusPointBean.getLng()));
    }

    /**
     * 设置自定义点图标
     */
    private void setWatchMarkerOptionPointIcon() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.app_point_layout, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(inflate);
        watchMarkerOptionPoint = new MarkerOptions().icon(bitmapDescriptor);

        //设置起始点图标
        View inflate1 = LayoutInflater.from(mContext).inflate(R.layout.app_watch_marker_start_layout, null);
        BitmapDescriptor bitmapDescriptorStart = BitmapDescriptorFactory.fromView(inflate1);
        watchMarkerOptionStart = new MarkerOptions().icon(bitmapDescriptorStart);

        //设置终点图标
        View inflate2 = LayoutInflater.from(mContext).inflate(R.layout.app_watch_marker_end_layout, null);
        BitmapDescriptor bitmapDescriptorEnd = BitmapDescriptorFactory.fromView(inflate2);
        watchMarkerOptionEnd = new MarkerOptions().icon(bitmapDescriptorEnd);
    }

    /**
     * 起点
     *
     * @param playLocus playLocus
     */
    private void addWatchMarkersStartToMap(LocusPointBean playLocus) {
        watchMarkerOptionStart
                .position(new LatLng(playLocus.getLat(), playLocus.getLng()))
                .draggable(false);
        startmarker = aMap.addMarker(watchMarkerOptionStart);
    }

    /**
     * 终点
     *
     * @param playLocus playLocus
     */
    private void addWatchMarkersEndToMap(LocusPointBean playLocus) {
        watchMarkerOptionEnd
                .position(new LatLng(playLocus.getLat(), playLocus.getLng()))
                .draggable(false);
        endMarker = aMap.addMarker(watchMarkerOptionEnd);
    }

    /**
     * 点
     *
     * @param playLocus playLocus
     */
    private void addWatchMarkersPointToMap(LocusPointBean playLocus) {
        watchMarkerOptionPoint
                .position(new LatLng(playLocus.getLat(), playLocus.getLng()))
                .draggable(false)
                .anchor(0.5f, 0.5f);
        redMarker = aMap.addMarker(watchMarkerOptionPoint);
    }


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


    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String formatAddress = result.getRegeocodeAddress().getFormatAddress();
                if (formatAddress != null && formatAddress.length() > 0) {
                    //中国国内点，使用高德地图
                    ToastUtils.showShortToast("第一位点为国内点，建议使用高德");
                    changeToAmapView(nowUsePoint);
                } else {
                    //查询不到逆编码数据，使用google地图
                    ToastUtils.showShortToast("第一位点为国外点，建议使用Google");
                    changeToGoogleMapView(nowUsePoint);
                }
                LogUtils.d("高德地图，单条逆地理编码：" + formatAddress);
            } else {
                LogUtils.d("高德地图，单条获取逆地理编码异常");
            }
        } else {
            LogUtils.d("高德地图，单条获取逆地理编码异常，异常码：" + rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 缩放移动地图，保证所有自定义marker在可视范围中，且地图中心点不变。
     */
    public void zoomToSpanWithCenter(Marker centerMarker, LatLng centerPoint, List<LatLng> pointList) {
        if (pointList != null && pointList.size() > 0) {
            if (aMap == null)
                return;
            centerMarker.setVisible(true);
            LatLngBounds bounds = getLatLngBounds(centerPoint, pointList);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    /**
     * 根据中心点和自定义内容获取缩放bounds
     */
    private LatLngBounds getLatLngBounds(LatLng centerpoint, List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (centerpoint != null) {
            for (int i = 0; i < pointList.size(); i++) {
                LatLng p = pointList.get(i);
                LatLng p1 = new LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    /**
     * 根据中心点和自定义内容获取缩放bounds
     */
    private com.google.android.gms.maps.model.LatLngBounds googleGetLatLngBounds(com.google.android.gms.maps.model.LatLng centerpoint, List<com.google.android.gms.maps.model.LatLng> pointList) {
        com.google.android.gms.maps.model.LatLngBounds.Builder b = com.google.android.gms.maps.model.LatLngBounds.builder();
        if (centerpoint != null) {
            for (int i = 0; i < pointList.size(); i++) {
                com.google.android.gms.maps.model.LatLng p = pointList.get(i);
                com.google.android.gms.maps.model.LatLng p1 = new com.google.android.gms.maps.model.LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }
}
