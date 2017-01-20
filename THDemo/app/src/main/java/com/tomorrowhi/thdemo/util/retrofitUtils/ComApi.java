package com.tomorrowhi.thdemo.util.retrofitUtils;


import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by zhaotaotao on 19/01/2017.
 * 网络通用接口
 */
public class ComApi {

    private static ComApi INSTANCE;
    private Function<Observable, ObservableSource> composeFunction;
    private ComInterface mComInterface;

    private ComApi(Context context, String baseUrl, boolean isLog) {
        mComInterface = RetrofitUtils.createRetrofit(baseUrl, isLog, context).create(ComInterface.class);
    }

    public static ComApi getInstance(Context context, String baseUrl, boolean isLog) {
        if (INSTANCE == null) {
            //两次判断 INSTANCE 是为了避免多线程时操纵上的异常。synchronized保证线程安全
            synchronized (ComApi.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ComApi(context, baseUrl, isLog);
                }
            }
        }
        return INSTANCE;
    }

    public ComInterface getApi(){
        return mComInterface;
    }


}
