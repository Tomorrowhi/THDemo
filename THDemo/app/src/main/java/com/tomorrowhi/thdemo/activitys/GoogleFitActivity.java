package com.tomorrowhi.thdemo.activitys;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.common.MyConstants;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhaotaotao on 2017/5/17.
 * Google fit 接入测试
 */

public class GoogleFitActivity extends BaseActivity implements OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.title_return_iv)
    ImageButton mTitleReturnIv;
    @BindView(R.id.connect_google_fit)
    Button mConnectGoogleFit;
    @BindView(R.id.disconnect_google_fit)
    Button mDisconnectGoogleFit;
    @BindView(R.id.read_sensor_data)
    TextView mReadSensorData;
    @BindView(R.id.show_subscriptions_bt)
    Button mShowSubscriptionsBt;
    @BindView(R.id.cancel_subscriptions_bt)
    Button mCancelSubscriptionsBt;
    @BindView(R.id.btn_view_steps)
    Button mBtnViewSteps;
    @BindView(R.id.btn_view_today)
    Button mBtnViewToday;
    @BindView(R.id.btn_add_steps)
    Button mBtnAddSteps;
    @BindView(R.id.btn_update_steps)
    Button mBtnUpdateSteps;
    @BindView(R.id.btn_delete_steps)
    Button mBtnDeleteSteps;
    @BindView(R.id.tv_data_update)
    TextView mTvDataUpdate;
    @BindView(R.id.tv_update_feedback)
    TextView mTvUpdateFeedback;
    @BindView(R.id.btn_update_height)
    Button mBtnUpdateHeight;
    @BindView(R.id.btn_update_weight)
    Button mBtnUpdateWeight;


    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;

    private ResultCallback<Status> mSubscribeResultCallback;
    private ResultCallback<Status> mCancelSubscriptionResultCallback;
    private ResultCallback<ListSubscriptionsResult> mListSubscriptionsResultCallback;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_google_fit;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        initCallBacks();
    }

    private void initCallBacks() {
        mSubscribeResultCallback = new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                        LogUtils.d("RecordingAPI mSubscribeResultCallback Already subscribed to the Recording API");
                    } else {
                        LogUtils.d("RecordingAPI mSubscribeResultCallback Subscribed to the Recording API");
                    }
                }
            }
        };

        mCancelSubscriptionResultCallback = new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    LogUtils.d("RecordingAPI mCancelSubscriptionResultCallback Cancel subscriptions");
                } else {
                    //Subscription not removed
                    LogUtils.d("RecordingAPI mCancelSubscriptionResultCallback Failed to cancel subscriptions");
                }
            }
        };

        mListSubscriptionsResultCallback = new ResultCallback<ListSubscriptionsResult>() {
            @Override
            public void onResult(@NonNull ListSubscriptionsResult listSubscriptionsResult) {
                for (Subscription subscription : listSubscriptionsResult.getSubscriptions()) {
                    DataType dataType = subscription.getDataType();
                    LogUtils.d("RecordingAPI mListSubscriptionsResultCallback " + dataType.getName());
                    for (Field field : dataType.getFields()) {
                        LogUtils.d("RecordingAPI mListSubscriptionsResultCallback " + field.toString());
                    }
                }
            }
        };


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(MyConstants.AUTH_PENDING);
        }

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, 0, this)
                .build();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MyConstants.AUTH_PENDING, authInProgress);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick({R.id.title_return_iv, R.id.connect_google_fit, R.id.disconnect_google_fit,
            R.id.show_subscriptions_bt, R.id.cancel_subscriptions_bt, R.id.btn_view_steps,
            R.id.btn_view_today, R.id.btn_add_steps, R.id.btn_update_steps,
            R.id.btn_delete_steps, R.id.btn_update_height, R.id.btn_update_weight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.connect_google_fit:
                mApiClient.connect();
                break;
            case R.id.disconnect_google_fit:
                disConnectFit();
                break;
            case R.id.show_subscriptions_bt:
                showSubscriptions();
                break;
            case R.id.cancel_subscriptions_bt:
                cancelSubscriptions();
                break;
            case R.id.btn_view_steps:
                findWeekData();
                break;
            case R.id.btn_view_today:
                findDayData();
                break;
            case R.id.btn_add_steps:
                insertData();
                break;
            case R.id.btn_update_steps:
                updateData();
                break;
            case R.id.btn_update_height:
                updateHeight();
                break;
            case R.id.btn_update_weight:
                updateWeight();
                break;
            case R.id.btn_delete_steps:
                break;
        }
    }

    private void updateWeight() {
        mTvUpdateFeedback.setText("正在更新体重....");
        mTvDataUpdate.setText("");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_WEIGHT)
                .setName("weight")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet dataSet = DataSet.create(dataSource);

        DataPoint point = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_WEIGHT).setFloat(80);
        dataSet.add(point);

        final DataUpdateRequest updateRequest = new DataUpdateRequest.Builder()
                .setDataSet(dataSet)
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        Flowable.create(new FlowableOnSubscribe<Status>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Status> e) throws Exception {
                Status status = Fitness.HistoryApi.updateData(mApiClient, updateRequest).await(1, TimeUnit.MINUTES);
                e.onNext(status);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Status>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Status status) throws Exception {
                        LogUtils.d("status " + status.toString());
                        mTvUpdateFeedback.setText("更新结果：");
                        if (status.isSuccess()) {
                            mTvDataUpdate.setText("更新成功");
                        } else {
                            mTvDataUpdate.setText("更新失败");
                        }
                    }
                });
    }

    /**
     * 未生效
     */
    private void updateHeight() {
        mTvUpdateFeedback.setText("正在更新身高数据....");
        mTvDataUpdate.setText("");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_HEIGHT)
                .setName("height")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet dataSet = DataSet.create(dataSource);

        DataPoint point = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_HEIGHT).setFloat(1.81f);
        dataSet.add(point);

        final DataUpdateRequest updateRequest = new DataUpdateRequest.Builder()
                .setDataSet(dataSet)
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        Flowable.create(new FlowableOnSubscribe<Status>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Status> e) throws Exception {
                Status status = Fitness.HistoryApi.updateData(mApiClient, updateRequest).await(1, TimeUnit.MINUTES);
                e.onNext(status);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Status>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Status status) throws Exception {
                        LogUtils.d("status " + status.toString());
                        mTvUpdateFeedback.setText("更新结果：");
                        if (status.isSuccess()) {
                            mTvDataUpdate.setText("更新成功");
                        } else {
                            mTvDataUpdate.setText("更新失败");
                        }
                    }
                });

    }

    /**
     * 更新数据
     */
    private void updateData() {
        mTvUpdateFeedback.setText("正在更新数据....");
        mTvDataUpdate.setText("");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName("Step Count")
                .setType(DataSource.TYPE_RAW)
                .build();

        int stepCountDelta = 2000;
        DataSet dataSet = DataSet.create(dataSource);

        DataPoint point = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(point);

        final DataUpdateRequest updateRequest = new DataUpdateRequest.Builder()
                .setDataSet(dataSet)
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        Flowable.create(new FlowableOnSubscribe<Status>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Status> e) throws Exception {
                Status status = Fitness.HistoryApi.updateData(mApiClient, updateRequest).await(1, TimeUnit.MINUTES);
                e.onNext(status);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Status>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Status status) throws Exception {
                        LogUtils.d("status " + status.toString());
                        mTvUpdateFeedback.setText("更新结果：");
                        if (status.isSuccess()) {
                            mTvDataUpdate.setText("更新成功");
                        } else {
                            mTvDataUpdate.setText("更新失败");
                        }
                    }
                });


    }

    /**
     * 插入数据
     */
    private void insertData() {
        mTvUpdateFeedback.setText("正在插入数据....");
        mTvDataUpdate.setText("");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName("测试数据")
                .setType(DataSource.TYPE_RAW)
                .build();

        int stepCountDelta = 1000;
        final DataSet dataSet = DataSet.create(dataSource);
        DataPoint point = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(point);

        Flowable.create(new FlowableOnSubscribe<Status>() {
            @Override
            public void subscribe(FlowableEmitter<Status> e) throws Exception {
                Status status = Fitness.HistoryApi.insertData(mApiClient, dataSet).await(1, TimeUnit.MINUTES);
                e.onNext(status);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Status>() {
                    @Override
                    public void accept(Status status) throws Exception {
                        LogUtils.d("status " + status.toString());
                        mTvUpdateFeedback.setText("插入结果：");
                        if (status.isSuccess()) {
                            mTvDataUpdate.setText("插入成功");
                        } else {
                            mTvDataUpdate.setText("插入失败");
                        }
                    }
                });


    }

    /**
     * 查询天数据
     */
    private void findDayData() {
        mTvUpdateFeedback.setText("正在查询数据....");
        mTvDataUpdate.setText("");
        Flowable.create(new FlowableOnSubscribe<DailyTotalResult>() {
            @Override
            public void subscribe(FlowableEmitter<DailyTotalResult> e) throws Exception {
                DailyTotalResult dailyTotalResult = Fitness.HistoryApi.readDailyTotal(mApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
                e.onNext(dailyTotalResult);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DailyTotalResult>() {
                    @Override
                    public void accept(DailyTotalResult dailyTotalResult) throws Exception {
                        showDataSet(dailyTotalResult.getTotal());
                    }
                });
    }

    /**
     * 查询周数据
     */
    private void findWeekData() {
        mTvUpdateFeedback.setText("正在查询数据....");
        mTvDataUpdate.setText("");
        Flowable.create(new FlowableOnSubscribe<DataReadResult>() {
            @Override
            public void subscribe(FlowableEmitter<DataReadResult> e) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                long endTime = calendar.getTimeInMillis();
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                long startTime = calendar.getTimeInMillis();
                DateFormat dateFormat = DateFormat.getDateInstance();
                LogUtils.d("History Range Start " + dateFormat.format(startTime));
                LogUtils.d("History Range End " + dateFormat.format(endTime));
                //check hoe many steps walked and recorded in the last 7 days
                DataReadRequest readRequst = new DataReadRequest.Builder()
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
                //请求数据，同时使用await，如果出现问题时，会在一分钟后继续。
                DataReadResult dataReadResult = Fitness.HistoryApi.readData(mApiClient, readRequst).await(1, TimeUnit.MINUTES);
                e.onNext(dataReadResult);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataReadResult>() {
                    @Override
                    public void accept(DataReadResult dataReadResult) throws Exception {
                        //used for aggregated data
                        if (dataReadResult.getBuckets().size() > 0) {
                            LogUtils.d("History Number of buckets" + dataReadResult.getBuckets().size());
                            for (Bucket bucket : dataReadResult.getBuckets()) {
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
                                    showDataSet(dataSet);
                                }
                            }
                            //used for non-aggregated data

                        } else if (dataReadResult.getDataSets().size() > 0) {
                            LogUtils.d("History Number of return DataSets: " + dataReadResult.getDataSets().size());
                            for (DataSet dataSet : dataReadResult.getDataSets()) {
                                showDataSet(dataSet);
                            }
                        }
                    }
                });


    }

    private void showDataSet(DataSet dataSet) {
        mTvUpdateFeedback.setText("查询结果为：");
        LogUtils.d("History Data return for data type: " + dataSet.getDataType().getName());
        DateFormat dateInstance = DateFormat.getDateInstance();
        DateFormat timeInstance = DateFormat.getTimeInstance();
        for (DataPoint dp : dataSet.getDataPoints()) {
            LogUtils.d("History Data point: \n"
                    + "\n Type:" + dp.getDataType().getName()
                    + "\n Start:" + dateInstance.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                    + " " + timeInstance.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                    + "\n End:" + dateInstance.format(dp.getEndTime(TimeUnit.MILLISECONDS))
                    + " " + timeInstance.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                LogUtils.d("History Field: " + field.getName() + "value: " + dp.getValue(field));
                mTvDataUpdate.setText(mTvDataUpdate.getText().toString() + "\n Type:" + dp.getDataType().getName()
                        + "\n Start:" + dateInstance.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                        + " " + timeInstance.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                        + "\n End:" + dateInstance.format(dp.getEndTime(TimeUnit.MILLISECONDS))
                        + " " + timeInstance.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                        + "\n History Field:" + field.getName() + "value: " + dp.getValue(field)
                        + "\n ------------ \n");
            }
        }
    }

    private void cancelSubscriptions() {
        Fitness.RecordingApi.unsubscribe(mApiClient, DataType.TYPE_STEP_COUNT_DELTA)
                .setResultCallback(mCancelSubscriptionResultCallback);
    }

    private void showSubscriptions() {
        Fitness.RecordingApi.listSubscriptions(mApiClient)
                .setResultCallback(mListSubscriptionsResultCallback);
    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {
        LogUtils.d("onDataPoint");
        for (final Field field : dataPoint.getDataType().getFields()) {
            final Value value = dataPoint.getValue(field);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d("读取到的传感器数据: " + field.getName() + " Value: " + value);
                    mReadSensorData.setText("读取到的传感器数据：" + field.getName() + " Value: " + value);
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LogUtils.d("onConnected");
        connectSensorTest();

        Fitness.RecordingApi.subscribe(mApiClient, DataType.TYPE_STEP_COUNT_DELTA)
                .setResultCallback(mSubscribeResultCallback);
    }

    private void connectSensorTest() {
        DataSourcesRequest dataSourcesRequest = new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build();

        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                    if (DataType.TYPE_STEP_COUNT_CUMULATIVE.equals(dataSource.getDataType())) {
                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                    }
                }
            }
        };
        Fitness.SensorsApi.findDataSources(mApiClient, dataSourcesRequest)
                .setResultCallback(dataSourcesResultCallback);
    }

    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {

        SensorRequest request = new SensorRequest.Builder()
                .setDataSource(dataSource)
                .setDataType(dataType)
                .setSamplingRate(3, TimeUnit.SECONDS)
                .build();

        Fitness.SensorsApi.add(mApiClient, request, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            LogUtils.d("SensorApi successfully added");
                        }
                    }
                });
    }


    @Override
    public void onConnectionSuspended(int i) {
        LogUtils.d("onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.d("onConnectionFailed");
        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(this, MyConstants.REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            LogUtils.d("authInProgress");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.REQUEST_OAUTH) {
            authInProgress = false;
            if (resultCode == RESULT_OK) {
                if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
                    mApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                LogUtils.d("RESULT_CANCELED");
            }
        } else {
            LogUtils.d("requestCode NOT request_oauth");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void disConnectFit() {
        if (mApiClient != null && mApiClient.isConnected()) {
            Fitness.SensorsApi.remove(mApiClient, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                mApiClient.disconnect();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnectFit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
