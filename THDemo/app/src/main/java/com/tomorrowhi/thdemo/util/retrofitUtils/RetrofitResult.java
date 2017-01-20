package com.tomorrowhi.thdemo.util.retrofitUtils;

/**
 * Created by zhaotaotao on 20/01/2017.
 */

public interface RetrofitResult {

    void handleResponse(Object object);

    void handleError(Throwable error);

    void handleComplete();

}
