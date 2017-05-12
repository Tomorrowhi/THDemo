package com.tomorrowhi.thdemo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.blankj.utilcode.util.LogUtils;
import com.tomorrowhi.thdemo.MyApplication;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.activitys.MainActivity;
import com.tomorrowhi.thdemo.activitys.NotificationServiceActivity;
import com.tomorrowhi.thdemo.bean.event.NotificationServiceData;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhaotaotao on 2017/5/10.
 * 后台本地服务
 */
public class BackGroundNotificationService extends Service {

    private Disposable subscribe;
    private Notification mCustomerNotification;
    private NotificationManager notificationManager;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("onStartCommand");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //获取Notification的构造器
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent activityIntent = new Intent(this, NotificationServiceActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, activityIntent, 0))
                //设置下拉列表中的图标（大图标）
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("测试title")
                //下拉列表中的小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("测试内容")
                .setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;
        // 参数一：唯一的通知标识；参数二：通知消息。
        startForeground(120, notification);

        //自定义Notification布局
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        remoteViews.setOnClickPendingIntent(R.id.notification_left, PendingIntent.getActivity(this, 1101, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        Notification.Builder builderCustomer;
        builderCustomer = new Notification.Builder(this.getApplicationContext())
                .setContent(remoteViews);
        builderCustomer.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher);

        mCustomerNotification = builderCustomer.build();
        mCustomerNotification.defaults = Notification.DEFAULT_SOUND;
        startForeground(121, mCustomerNotification);

        testThread(0);
        return super.onStartCommand(intent, flags, startId);
    }

    public void testThread(long defaultLong) {
        LogUtils.d("Notification Service testThread：" + defaultLong);
        subscribe = Flowable.intervalRange(defaultLong, 100, 1, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                    }
                }).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        LogUtils.d("Notification Service thread：" + String.valueOf(aLong));
                        mCustomerNotification.contentView.setTextViewText(R.id.notification_tv, String.valueOf(aLong));
                        notificationManager.notify(121, mCustomerNotification);
                        MyApplication.getInstance().getEventBus().post(new NotificationServiceData(aLong));
                    }
                });

    }

    public void endThread() {
        if (subscribe != null) {
            subscribe.dispose();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        endThread();
        stopForeground(true);
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public BackGroundNotificationService getService() {
            return BackGroundNotificationService.this;
        }
    }

}
