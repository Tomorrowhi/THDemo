package com.tomorrowhi.thdemo.activitys;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.util.MathUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/5/9.
 * 计步传感器
 */
public class SensorActivity extends BaseActivity implements SensorEventListener {

    @BindView(R.id.title_return_iv)
    ImageButton mTitleReturnIv;
    @BindView(R.id.steps_num)
    TextView mStepsNum;

    private boolean mSupportSensor;
    private SensorManager sensorManager;
    private Sensor mStepCounter;
    private Sensor mStepDetector;
    private float mSteps;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_sensor;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        //注册监听传感器事件
        sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void initData() {
        mSteps = 0;
        if (isSupportSensor()) {
            //支持计步传感器

            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            LogUtils.d("mStepDetector:" + mStepDetector.toString());

        } else {
            //不支持
            ToastUtils.showShort("暂不支持计步传感器");
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    public boolean isSupportSensor() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                && getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)) {
            //判断手机是否支持计步传感器
            return true;
        }
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_STEP_COUNTER:
                //event.values[0]为计步历史累加值
                LogUtils.d("onSensorChanged TYPE_STEP_COUNTER:" + Arrays.toString(event.values)
                        + ":" + event.sensor.toString() + ":" + TimeUtils.millis2String(event.timestamp));
                if (mSteps == 0) {
                    mSteps = (int) event.values[0];
                }
                mStepsNum.setText(getString(R.string.steps_num, MathUtils.getdfInt(event.values[0] - mSteps)));
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                if (event.values[0] == 1.0) {
                    //event.values[0]一次有效计步数据
                    LogUtils.d("onSensorChanged TYPE_STEP_DETECTOR 有效计步");
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(this, mStepCounter);
        sensorManager.unregisterListener(this, mStepDetector);
        super.onDestroy();
    }

    @OnClick({R.id.title_return_iv, R.id.steps_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.steps_num:
                break;
        }
    }
}
