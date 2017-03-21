package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.blankj.utilcode.utils.LogUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.LocusPointBean;
import com.tomorrowhi.thdemo.util.locationUtiils.LocationUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 10/01/2017.
 * 高德 历史轨迹
 */
public class AMapLocusActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener, AMap.InfoWindowAdapter {

    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;
    @BindView(R.id.amap_map_view)
    MapView amapMapView;
    @BindView(R.id.a_map_app_location_desc)
    TextView aMapAppLocationDesc;
    @BindView(R.id.a_map_locus_bottom_desc)
    RelativeLayout aMapBottomDesc;

    private AMap aMap;
    private ReadThread playLocus;
    private MarkerOptions locusMarkerOption;
    private GeocodeSearch geocoderSearch;
    private ExecutorService mExecutorService;
    private PolylineOptions options;
    private long threadRunTimeIntervalLong = 1700;  //绘制线的最长间隔
    private long threadRunTimeInterval = 200;  //绘制线的间隔差值
    private long threadRunTimeIntervalShort = 1000;  //绘制线的最短时间间隔
    private List<LocusPointBean> locusPointBeanList = new ArrayList<>();
    private double addNum = 0.001;
    private double defaultLat = 22.5714712;
    private double defaultLng = 113.8619078;
    private int countMarker = 0;
    private View markerView;
    private TextView markerNumTv;
    private LatLng lastLatlng = null;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_amap_locus;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
    }

    @Override
    protected void initEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPlayLocus();
            }
        }, 2000);
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
        for (int i = 0; i < 10; i++) {
            defaultLat += addNum;
            locusPointBeanList.add(new LocusPointBean(defaultLat, defaultLng, false));
        }
        for (int i = 0; i < 10; i++) {
            defaultLng += addNum;
            locusPointBeanList.add(new LocusPointBean(defaultLat, defaultLng, false));
        }
        options = new PolylineOptions();
        //请求获得坐标所属的地理位置逆编码
        getLocationDescribe();
        getAddresses();
    }

    @Override
    protected void initView() {
        setUpMapIfNeeded();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        amapMapView.onCreate(savedInstanceState);
        myApplication.getEventBus().register(this);
    }


    @OnClick({R.id.title_return_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;

        }
    }

    private void setUpMapIfNeeded() {
        if (aMap == null) {
            aMap = amapMapView.getMap();
        }
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        //aMap.set;
        UiSettings mUiSettings = aMap.getUiSettings();
        //隐藏地图上的 + - 图标
        mUiSettings.setZoomControlsEnabled(false);
        setLocationIcon();
        //获取传递过来的基本信息
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(defaultLat, defaultLng), LocationUtil.MAP_LEVEL));
    }


    /**
     * 设置自定义的定位图标
     */
    private void setLocationIcon() {
        //初始化手表坐标位置marker
        locusMarkerOption = new MarkerOptions();
    }

    private void startPlayLocus() {
        playLocus = new ReadThread("playLocus");
        playLocus.start();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return getInfoWindowView(marker);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
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

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 通过子线程进行播放轨迹操作
     */
    public class ReadThread implements Runnable {
        Thread thread;
        private String threadName;
        private boolean suspended = false;
        private boolean end = false;

        ReadThread(String threadName) {
            this.threadName = threadName;
            LogUtils.d("Creating " + threadName);
        }

        /**
         * 执行内容
         */
        public void run() {
            for (int i = 0; i < locusPointBeanList.size(); i++) {
                try {
                    synchronized (this) {
                        while (suspended) {
                            wait();
                        }
                    }

                    if (Thread.interrupted()) {
                        LogUtils.d("Thread1" + threadName + " exiting.");
                        break;
                    }
                    if (i == (locusPointBeanList.size() - 1)) {
                        //最后一条数据，发送改变文字标识通知
                        LogUtils.d("locus play over");
                        locusPointBeanList.get(i).setOver(true);
                    }
                    myApplication.getEventBus().post(locusPointBeanList.get(i));
                    Thread.sleep(threadRunTimeIntervalLong);
                    LogUtils.d("threadRunTimeIntervalLong：" + threadRunTimeIntervalLong);
                    if (threadRunTimeIntervalLong > threadRunTimeIntervalShort) {
                        threadRunTimeIntervalLong -= threadRunTimeInterval;
                    }
                } catch (InterruptedException e) {
                    LogUtils.d("Thread " + threadName + " interrupted.");
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                if (end) {
                    break;
                }

            }
            LogUtils.d("Thread " + threadName + " exiting.");
        }

        /**
         * 开始
         */
        void start() {
            LogUtils.d("Starting " + threadName);
            if (thread == null) {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }

        /**
         * 暂停
         */
        void suspend() {
            if (thread != null) {
                suspended = true;
            }
        }

        /**
         * 继续
         */
        synchronized void resume() {
            if (thread != null && suspended) {
                suspended = false;
                notify();
            }
        }

        void endThread() {
            end = true;
            thread.interrupt();
            thread = null;
        }

    }

    /**
     * 请求获得坐标所属的地理位置逆编码
     */
    private void getLocationDescribe() {
        geocoderSearch = new GeocodeSearch(this);
    }

    /**
     * 响应逆地理编码的批量请求
     */
    private void getAddresses() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        for (int i = 0; i < locusPointBeanList.size(); i++) {
            final LocusPointBean point = locusPointBeanList.get(i);
            final LatLonPoint latLonPoint = new LatLonPoint(point.getLat(), point.getLng());
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 10,
                                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                        RegeocodeAddress result = geocoderSearch.getFromLocation(query);// 设置同步逆地理编码请求
                        if (result != null && result.getFormatAddress() != null
                                ) {
                            point.setOver(false);
                            point.setLocationDescribe(result.getFormatAddress());
//                            addWatchMarkersToMap(point, false, 0);
                            LogUtils.d("轨迹界面，批量获取逆地理编码：" + result.getFormatAddress() + "，坐标：" + point.toString());
                        }

                    } catch (AMapException e) {
                        LogUtils.d("轨迹界面，批量获取逆地理编码异常，异常码：" + e.getErrorCode());
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 20)
    public void getLocusLocation(LocusPointBean result) {
        if (result != null) {
            LogUtils.d("event事件:LocusActivity");
            countMarker++;
            LatLng latLng = new LatLng(result.getLat(), result.getLng());
            addWatchMarkersToMap(result, true, countMarker);
            //绘制线
            if (lastLatlng != null) {
                //存在上一点记录，则开始绘制
                options.add(lastLatlng);
            } else {
                //说明是第一个点，不进行绘制
            }
            options.add(latLng);
            aMap.addPolyline(options.width(10).color(ContextCompat.getColor(mContext, R.color.purple_2)));
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    latLng, LocationUtil.MAP_LEVEL));
            aMapAppLocationDesc.setText(result.getLocationDescribe());
            if (result.isOver()) {
                //如果是最后一条数据，还需要更改按钮文字
                result.setOver(false);
                //播放时间间隔设置为默认值
                threadRunTimeIntervalLong = 1700;
                lastLatlng = null;
                countMarker = 0;
                //恢复开始播放前的状态，此处屏蔽，不恢复
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        clearAllMarkersToMap();
//                        for (LocationHistoryBean bean : locationHistoryBeanList) {
//                            addWatchMarkersToMap(bean, false, 0);
//                        }
//
//                    }
//                }, threadRunTimeIntervalLong);
                options = new PolylineOptions();
            }
        }
    }

    /**
     * 在地图上添加marker
     *
     * @param playLocus playLocus
     */
    private void addWatchMarkersToMap(LocusPointBean playLocus, boolean isShowNum, int num) {
        if (isShowNum) {
            markerView = View.inflate(mContext, R.layout.map_markers_view, null);
            markerNumTv = (TextView) markerView.findViewById(R.id.marker_num_tv);
            markerNumTv.setText(String.valueOf(num));
            locusMarkerOption.icon(BitmapDescriptorFactory.fromView(markerView));
        } else {
            locusMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_current_location));
        }
        locusMarkerOption
                .snippet(playLocus.getLocationDescribe())
                .position(new LatLng(playLocus.getLat(), playLocus.getLng()))
                .draggable(false);
        aMap.addMarker(locusMarkerOption);
    }

    /**
     * 清除地图上所有的marker
     */
    public void clearAllMarkersToMap() {
        aMap.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        amapMapView.onPause();
        if (playLocus != null) {
            playLocus.suspend();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        amapMapView.onResume();
        if (playLocus != null) {
            playLocus.resume();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        amapMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (playLocus != null) {
            playLocus.endThread();
            playLocus = null;
        }
        if (mExecutorService != null) {
            mExecutorService.shutdown();
            mExecutorService.shutdownNow();
            mExecutorService = null;
        }
        amapMapView.onDestroy();
        LogUtils.d("locus destroy 轨迹界面 ");
        super.onDestroy();
    }

}
