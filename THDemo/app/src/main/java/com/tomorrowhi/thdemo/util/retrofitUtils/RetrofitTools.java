package com.tomorrowhi.thdemo.util.retrofitUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tomorrowhi.thdemo.bean.ResBaseModel;
import com.tomorrowhi.thdemo.util.DialogUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhaotaotao on 20/01/2017.
 * retrofit2 tool
 */
public class RetrofitTools {

    /**
     * 同意针对结果判断返回值中的state
     *
     * @param isShowProgress       是否显示进度条 true显示
     * @param isCancelRequest      进度条是否可以取消
     * @param context              context
     * @param showContent          进度条现显示的内容
     * @param mCompositeDisposable CompositeDisposable
     * @param request              request
     * @param result               resultCallBack
     */
    public static void httpRequest(boolean isShowProgress, boolean isCancelRequest, Context context, String showContent,
                                   CompositeDisposable mCompositeDisposable,
                                   Observable request, final RetrofitResult result) {
        if (initRequest(context, showContent, isCancelRequest, isShowProgress, mCompositeDisposable))
            return;
        mCompositeDisposable.add(request
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                result.handleResponse(o);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                DialogUtil.hide();
                                ToastUtils.showShortToast("请求失败，请稍后重试");
                                result.handleError(throwable);
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                DialogUtil.hide();
                                result.handleComplete();
                            }
                        }, new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        }
                ));
    }

    /**
     * 同意针对结果判断返回值中的state
     *
     * @param isShowProgress       是否显示进度条 true显示
     * @param isCancelRequest      进度条是否可以取消
     * @param context              context
     * @param showContent          进度条现显示的内容
     * @param mCompositeDisposable CompositeDisposable
     * @param request              request
     * @param result               resultCallBack
     */
    public static void httpCommonRequest(boolean isShowProgress, boolean isCancelRequest, Context context, String showContent,
                                         CompositeDisposable mCompositeDisposable,
                                         Observable request, final RetrofitResult result) {
        if (initRequest(context, showContent, isCancelRequest, isShowProgress, mCompositeDisposable))
            return;
        mCompositeDisposable.add(request
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<ResBaseModel<?>>() {
                            @Override
                            public void accept(ResBaseModel<?> resBaseModel) throws Exception {
                                LogUtils.d("ResBaseModel status: " + resBaseModel.getStatus());
                                if ("ok".equals(resBaseModel.getStatus())) {
                                    result.handleResponse(resBaseModel);
                                } else {
                                    //返回结果有误
                                    ToastUtils.showShortToast("返回数据有误");
                                }

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                DialogUtil.hide();
                                ToastUtils.showShortToast("请求失败，请稍后重试");
                                result.handleError(throwable);
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                DialogUtil.hide();
                                result.handleComplete();
                            }
                        },
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        }
                ));
    }

    /**
     * 请求之前的操作
     *
     * @param context              context
     * @param showContent          弹窗显示内容
     * @param isCancelRequest      是否可以取消弹窗
     * @param isShowProgress       是否显示弹窗
     * @param mCompositeDisposable mCompositeDisposable
     * @return true，终止往下执行
     */
    private static boolean initRequest(Context context, String showContent, boolean isCancelRequest, boolean isShowProgress, final CompositeDisposable mCompositeDisposable) {
        //检查网络
        if (!NetworkUtils.isConnected()) {
            //网络未连接
            ToastUtils.showShortToast("请检查网络");
            return true;
        }
        //设置弹窗
        if (isShowProgress) {
            AlertDialog alertDialog = DialogUtil.progressDialog(context, showContent, isCancelRequest);
            if (alertDialog != null) {
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        LogUtils.d("弹窗被关闭");
                        mCompositeDisposable.clear();
                    }
                });
            }
        }
        return false;
    }

}
