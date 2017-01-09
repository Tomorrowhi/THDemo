package com.tomorrowhi.thdemo.bean;

/**
 * Created by zhaotaotao on 09/01/2017.
 * eventBus事件传递测试
 */
public class EventBusTestCommonEvent {

    public EventBusTestCommonEvent(String msg) {
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
        return "EventBusTestMainToFragment{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
