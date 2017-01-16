package com.tomorrowhi.thdemo.interfaces;

import com.tomorrowhi.thdemo.bean.BookMsgBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhaotaotao on 09/01/2017.
 * api
 */
public interface RetrofitApis {
    //    @Headers({
//            "Accept: application/json",
//            "key: 3A73DE89-2C32-4DD8-A8F8-B43C1FC26C17",
//            "Content-Type: application/json"
//    })

    @GET("https://api.douban.com/v2/book/search")
    Call<BookMsgBean> getBookMsg(@Query("q") String bookName);

}
