package com.tomorrowhi.thdemo.util.voiceUtils;

/**
 * Created by zhaotaotao on 2017/7/18.
 */

public interface OnAudioStatusUpdateListener {

    /**
     * 录音中的数据更新
     *
     * @param db   声音分贝
     * @param time 录音时长
     */
    public void onUpdate(double db, long time);

    /**
     * 停止录音
     *
     * @param filePath 录音文件的路径
     */
    public void onStop(String filePath);
}
