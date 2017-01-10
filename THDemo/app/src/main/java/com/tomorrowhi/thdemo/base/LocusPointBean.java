package com.tomorrowhi.thdemo.base;

/**
 * Created by zhaotaotao on 10/01/2017.
 * 地图 历史轨迹
 */
public class LocusPointBean {

    public LocusPointBean(double lat, double lng, boolean isOver) {
        this.lat = lat;
        this.lng = lng;
        this.isOver = isOver;
    }

    private double lat;
    private double lng;
    private boolean isOver;
    private String locationDescribe;

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public void setLocationDescribe(String locationDescribe) {
        this.locationDescribe = locationDescribe;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }
}
