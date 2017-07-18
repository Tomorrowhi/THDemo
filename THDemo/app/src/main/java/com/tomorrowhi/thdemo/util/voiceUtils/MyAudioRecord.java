package com.tomorrowhi.thdemo.util.voiceUtils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.tomorrowhi.thdemo.util.StorageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhaotaotao on 2017/7/18.
 * MediaRecorder 录音、播放
 */

public class MyAudioRecord {

    private static MyAudioRecord INSTANCE;
    public static final int MAX_LENGTH_TIME = 1000 * 60 * 2;// 最大录音时长 2min

    private String filePath;
    private File saveFile;
    private MediaRecorder mMediaRecorder;
    private long startTime, endTime;

    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    private final Handler mHandler = new Handler();
    //用来循环检测分贝数
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMICStatus();
        }
    };


    public static MyAudioRecord getInstance(Context context) {
        if (INSTANCE == null) {
            //两次判断 INSTANCE 是为了避免多线程时操纵上的异常。synchronized保证线程安全
            synchronized (MyAudioRecord.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyAudioRecord(context);
                }
            }
        }
        return INSTANCE;
    }

    public MyAudioRecord(Context context) {
        filePath = StorageUtils.getCacheDirectory(context) + "/voice/";
        if (FileUtils.createOrExistsDir(filePath)) {
            LogUtils.a("文件夹 创建成功");
        } else {
            LogUtils.a("文件夹 创建失败");
        }
    }

    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        try {
            saveFile = new File(filePath + System.currentTimeMillis() + ".amr");
            //设置MIC
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(saveFile.getPath());
            mMediaRecorder.setMaxDuration(MAX_LENGTH_TIME);
            mMediaRecorder.prepare();

            mMediaRecorder.start();
            startTime = System.currentTimeMillis();
            updateMICStatus();
            LogUtils.d("开始录音");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音，返回录音总时长
     *
     * @return long 录音总时长
     */
    public long stopRecord() {
        if (mMediaRecorder == null) {
            return 0L;
        }
        endTime = System.currentTimeMillis();

        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            audioStatusUpdateListener.onStop(saveFile.getPath());

            saveFile = null;
        } catch (RuntimeException e) {
            //停止时如果出现异常，会进行清空操作
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            FileUtils.deleteFile(saveFile);
            saveFile = null;
        }

        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void canelRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        FileUtils.deleteFile(saveFile);
        saveFile = null;
    }

    private void updateMICStatus() {
        //如果 mMediaRecorder ==null 则结束监听循环
        if (mMediaRecorder != null) {
            double ratio = mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    /**
     * 播放音频
     *
     * @param filePath 文件路径
     * @param listener 播放回调
     */
    public void playRecord(String filePath, MediaPlayer.OnCompletionListener listener) {
        LogUtils.d("开始播放");
        if (!EmptyUtils.isEmpty(filePath)) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(listener);
            try {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
