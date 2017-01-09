package com.tomorrowhi.thdemo.bean;

/**
 * Created by zhaotaotao on 09/01/2017.
 */
public class EventBusTestStickEvent {

    public EventBusTestStickEvent(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "EventBusTestStickEvent{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
