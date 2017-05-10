package com.tomorrowhi.thdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by zhaotaotao on 2017/5/10.
 * 后台远程服务
 */
public class BackGroundRemoteService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
