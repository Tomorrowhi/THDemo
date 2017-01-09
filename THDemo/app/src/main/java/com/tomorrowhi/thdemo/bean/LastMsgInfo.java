package com.tomorrowhi.thdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhaotaotao on 2016/12/29.
 * 接受到的推送消息
 */
@Entity(nameInDb = "last_msg_info")
public class LastMsgInfo {

    @Id(autoincrement = true)
    private Long msg_id;

    private int msg_type;
    private Long device_id;
    private String create_time;
    private String content;
    private String remarks;
    private boolean is_read;
    @Generated(hash = 297323533)
    public LastMsgInfo(Long msg_id, int msg_type, Long device_id,
            String create_time, String content, String remarks, boolean is_read) {
        this.msg_id = msg_id;
        this.msg_type = msg_type;
        this.device_id = device_id;
        this.create_time = create_time;
        this.content = content;
        this.remarks = remarks;
        this.is_read = is_read;
    }
    @Generated(hash = 1958834435)
    public LastMsgInfo() {
    }
    public Long getMsg_id() {
        return this.msg_id;
    }
    public void setMsg_id(Long msg_id) {
        this.msg_id = msg_id;
    }
    public int getMsg_type() {
        return this.msg_type;
    }
    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }
    public Long getDevice_id() {
        return this.device_id;
    }
    public void setDevice_id(Long device_id) {
        this.device_id = device_id;
    }
    public String getCreate_time() {
        return this.create_time;
    }
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public boolean getIs_read() {
        return this.is_read;
    }
    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
}
