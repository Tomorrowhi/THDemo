package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.tomorrowhi.thdemo.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhaotaotao on 2016/12/23.
 * 导航界面
 */

public class AMapNavigationActivity extends BaseActivity implements AMapNaviViewListener, AMapNaviListener {

    @BindView(R.id.amap_navigation_view)
    AMapNaviView mAMapNaviView;

    private AMapNavi mAMapNavi;
    private final List<NaviLatLng> sList = new ArrayList<>();
    private final List<NaviLatLng> eList = new ArrayList<>();
    protected List<NaviLatLng> mWayPointList = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_amap_navigation;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNavi = AMapNavi.getInstance(mContext);
        mAMapNavi.addAMapNaviListener(this);
    }

    @Override
    protected void initData() {
        LatLng appLatLng = getIntent().getParcelableExtra(MyConstants.APP_LOCATION_LATLNG);
        LatLng navigationLatLng = getIntent().getParcelableExtra(MyConstants.NAVIGATION_LATLNG);
        if (appLatLng != null) {
            LogUtils.d("appLatLng:" + appLatLng.latitude + ":" + appLatLng.longitude);
            sList.add(new NaviLatLng(appLatLng.latitude, appLatLng.longitude));
        }
        if (navigationLatLng != null) {
            LogUtils.d("appLatLng:" + navigationLatLng.latitude + ":" + navigationLatLng.longitude);
            eList.add(new NaviLatLng(navigationLatLng.latitude, navigationLatLng.longitude));
        }
    }

    @Override
    protected void initView() {
        DialogUtil.progressDialog(mContext, "正在获取路线....", true);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mAMapNaviView.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        mAMapNaviView.onDestroy();
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        super.onDestroy();
    }

    @Override
    public void onNaviSetting() {
    }

    @Override
    public void onNaviCancel() {
        this.finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {
        //计算驾车路线
        /**
         * 方法:
         *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
         * 参数:
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         * 说明:
         *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         * 注意:
         *      不走高速与高速优先不能同时为true
         *      高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {

    }

    @Override
    public void onArriveDestination(AMapNaviStaticInfo aMapNaviStaticInfo) {

    }

    @Override
    public void onCalculateRouteSuccess() {
        DialogUtil.hide();
        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        DialogUtil.hide();
        ToastUtils.showShortToast("路径规划失败，请返回重试");
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }
}
