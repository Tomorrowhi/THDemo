package com.tomorrowhi.thdemo.util.locationUtiils;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by zhaotaotao on 2016/11/10.
 */
public class LocationUtil {

    /**
     * 开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP = 2;

    public final static String KEY_URL = "URL";
    public final static String URL_H5LOCATION = "file:///android_asset/location.html";

    //可视区域的缩放级别，高德地图支持3-19级的缩放级别
    public static int MAP_LEVEL = 16;

    /**
     * 默认的定位参数
     */
    public static AMapLocationClientOption getDefaultOption(boolean isOnceLocation) {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(300000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(isOnceLocation);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(isOnceLocation);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTPS);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
    }

    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @param location location
     * @return
     */
    public synchronized static String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: ").append(location.getLocationType()).append("\n");
            sb.append("经    度    : ").append(location.getLongitude()).append("\n");
            sb.append("纬    度    : ").append(location.getLatitude()).append("\n");
            sb.append("精    度    : ").append(location.getAccuracy()).append("米").append("\n");
            sb.append("提供者    : ").append(location.getProvider()).append("\n");

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : ").append(location.getSpeed()).append("米/秒").append("\n");
                sb.append("角    度    : ").append(location.getBearing()).append("\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : ").append(location.getSatellites()).append("\n");
            } else {
                // 提供者是GPS时是没有以下信息的
                sb.append("国    家    : ").append(location.getCountry()).append("\n");
                sb.append("省            : ").append(location.getProvince()).append("\n");
                sb.append("市            : ").append(location.getCity()).append("\n");
                sb.append("城市编码 : ").append(location.getCityCode()).append("\n");
                sb.append("区            : ").append(location.getDistrict()).append("\n");
                sb.append("区域 码   : ").append(location.getAdCode()).append("\n");
                sb.append("地    址    : ").append(location.getAddress()).append("\n");
                sb.append("兴趣点    : ").append(location.getPoiName()).append("\n");
                //定位完成的时间
                sb.append("定位时间: ").append(formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
            }
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:").append(location.getErrorCode()).append("\n");
            sb.append("错误信息:").append(location.getErrorInfo()).append("\n");
            sb.append("错误描述:").append(location.getLocationDetail()).append("\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: ").append(formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        return sb.toString();
    }

    private static SimpleDateFormat sdf = null;

    public synchronized static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

}
