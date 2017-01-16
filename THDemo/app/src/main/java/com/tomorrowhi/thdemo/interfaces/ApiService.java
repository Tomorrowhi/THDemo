package com.tomorrowhi.thdemo.interfaces;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by zhaotaotao on 14/01/2017.
 * api
 */
public interface ApiService {

    @GET("/")
    Flowable<ResponseBody> getText();
}
