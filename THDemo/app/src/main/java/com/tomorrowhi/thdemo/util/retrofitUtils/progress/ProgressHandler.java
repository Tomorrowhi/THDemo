package com.tomorrowhi.thdemo.util.retrofitUtils.progress;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by ljd on 4/12/16.
 * 通过Handler更新进度
 */
public abstract class ProgressHandler {

    protected abstract void sendMessage(ProgressBean progressBean);

    protected abstract void handleMessage(Message message);

    protected abstract void onProgress(long progress, long total, boolean done);

    protected static class ResponseHandler extends Handler {

        private ProgressHandler mProgressHandler;
        public ResponseHandler(ProgressHandler mProgressHandler, Looper looper) {
            super(looper);
            this.mProgressHandler = mProgressHandler;
        }

        @Override
        public void handleMessage(Message msg) {
            mProgressHandler.handleMessage(msg);
        }
    }

}
