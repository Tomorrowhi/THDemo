package com.tomorrowhi.thdemo.util.retrofitUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.tomorrowhi.thdemo.bean.ResBaseModel;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhaotaotao on 19/01/2017.
 * BaseObserver
 */
public abstract class BaseObserver<T> implements Observer<ResBaseModel<T>> {

    private AlertDialog mDialog;
    private Disposable mDisposable;
    private final String SUCCESS_CODE = "ok";

    public BaseObserver(AlertDialog mDialog) {
        this.mDialog = mDialog;
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mDisposable.dispose();
            }
        });
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(ResBaseModel<T> value) {
        if (SUCCESS_CODE.equals(value.getStatus())) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getStatus(), value.getData());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d("retrofit2", "error " + e.toString());
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        ToastUtils.showShortToast("网络异常，请稍后再试");
    }

    @Override
    public void onComplete() {
        Log.d("retrofit2", "onComplete");
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void onHandleError(String code, T data) {
        LogUtils.d("net error status code is " + code + " and data is " + data.toString());
        ToastUtils.showShortToast(data.toString());
    }

    protected abstract void onHandleSuccess(T t);
}
