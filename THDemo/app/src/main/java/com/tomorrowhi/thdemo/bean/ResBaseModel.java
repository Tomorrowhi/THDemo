package com.tomorrowhi.thdemo.bean;

/**
 * Created by zhaotaotao on 19/01/2017.
 * 数据响应模型
 */
public class ResBaseModel<T> {

    private String status;
    protected T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResBaseModel{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
