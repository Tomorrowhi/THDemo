package com.tomorrowhi.thdemo.activitys;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.util.StorageUtils;
import com.tomorrowhi.thdemo.util.voiceUtils.AmrEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhaotaotao on 2017/7/18.
 * 采用 AudioRecord 进行录音
 */

public class MyAudioRecordOne extends BaseActivity {

    @BindView(R.id.bt_record)
    Button mBtRecord;
    @BindView(R.id.bt_play)
    Button mBtPlay;
    @BindView(R.id.bt_stop_record)
    Button mBtStopRecord;
    private File pcmFile, amrFile;
    private boolean isRecording = false, isCancelRecord = false;
    private Disposable recordThread;
    //采样率
    int frequency = 16000;
    //格式
    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    //16Bit
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_audio_record_one;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    private void startRecordThread() {

        recordThread = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                LogUtils.d("录音开始");
                startRecord();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        LogUtils.d("录音完成");
                    }
                });
    }

    /**
     * 录音
     */
    private void startRecord() {
        LogUtils.a("开始录音");
        //生成PCM文件
        String commonPath = StorageUtils.getCacheDirectory(mContext) + "/voice/sound_" + System.currentTimeMillis();
        pcmFile = new File(commonPath + ".pcm");
        if (FileUtils.createOrExistsFile(pcmFile)) {
            LogUtils.a("PCM 创建成功");
        } else {
            LogUtils.a("PCM 创建失败");
        }

        try {
            OutputStream os = new FileOutputStream(pcmFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);

            short[] buffer = new short[bufferSize];
            audioRecord.startRecording();
            LogUtils.a("开始录音");
            isRecording = true;
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                for (int i = 0; i < bufferReadResult; i++) {
                    dos.writeShort(buffer[i]);
                }
            }
            audioRecord.stop();
            dos.close();
            LogUtils.a("录音完成");
        } catch (IOException e) {
            LogUtils.a("录音失败");
            e.printStackTrace();
        }
        //录音完成后，如果没有取消，开始进行转换操作
        if (!isCancelRecord) {
            amrFile = new File(commonPath + ".amr");
            if (FileUtils.createOrExistsFile(amrFile)) {
                LogUtils.a("amr PCM 创建成功");
            } else {
                LogUtils.a("amr PCM 创建失败");
            }
            AmrEncoder.pcm2Amr(pcmFile.getPath(), amrFile.getPath());
        }
    }

    /**
     * 播放音频
     */
    public void playRecord() {
        LogUtils.a("开始播放");
        if (pcmFile == null) {
            return;
        }
        //读取文件
        int pcmLength = (int) (pcmFile.length() / 2);
        short[] pcm = new short[pcmLength];

        try {
            FileInputStream is = new FileInputStream(pcmFile);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            int i = 0;
            while (dis.available() > 0) {
                pcm[i] = dis.readShort();
                i++;
            }
            dis.close();
            AudioTrack audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    frequency, AudioFormat.CHANNEL_OUT_MONO, audioEncoding,
                    pcmLength * 2,
                    AudioTrack.MODE_STREAM
            );
            audioTrack.play();
            audioTrack.write(pcm, 0, pcmLength);
            audioTrack.stop();
        } catch (IOException e) {
            LogUtils.a("播放失败");
            e.printStackTrace();
        }
    }

    /**
     * 播放音频
     */
    public void playRecordAmr() {
        LogUtils.a("开始播放");
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //播放完毕
                ToastUtils.showShort("播放完毕");
            }
        });

        try {
            mediaPlayer.setDataSource(amrFile.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRecording = false;
        recordThread.dispose();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_record, R.id.bt_play, R.id.bt_stop_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_record:
                ToastUtils.showShort("开始录音");
                startRecordThread();
                break;
            case R.id.bt_play:
                ToastUtils.showShort("播放录音");
                playRecord();
                break;
            case R.id.bt_stop_record:
                ToastUtils.showShort("录音结束");
                isRecording = false;
                break;
        }
    }
}
