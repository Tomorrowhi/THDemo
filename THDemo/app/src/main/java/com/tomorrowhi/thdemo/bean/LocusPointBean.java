package com.tomorrowhi.thdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaotaotao on 10/01/2017.
 * 地图 历史轨迹
 */
public class LocusPointBean implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeByte(this.isOver ? (byte) 1 : (byte) 0);
        dest.writeString(this.locationDescribe);
    }

    protected LocusPointBean(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.isOver = in.readByte() != 0;
        this.locationDescribe = in.readString();
    }

    public static final Parcelable.Creator<LocusPointBean> CREATOR = new Parcelable.Creator<LocusPointBean>() {
        @Override
        public LocusPointBean createFromParcel(Parcel source) {
            return new LocusPointBean(source);
        }

        @Override
        public LocusPointBean[] newArray(int size) {
            return new LocusPointBean[size];
        }
    };
}
