package com.tomorrowhi.thdemo.util.retrofitUtils;

import com.tomorrowhi.thdemo.bean.AndroidApiTest;
import com.tomorrowhi.thdemo.bean.ResBaseModel;
import com.tomorrowhi.thdemo.bean.ShangHaiBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhaotaotao on 19/01/2017.
 * 网络接口
 */
public interface ComInterface {

    @GET("/feed/shanghai/")
    Observable<ResBaseModel<ShangHaiBean>> getShanghaiData(@Query("token") String token);

    @GET("https://api.learn2crack.com/android/jsonarray")
    Observable<List<AndroidApiTest>> androidApiTest();
}
