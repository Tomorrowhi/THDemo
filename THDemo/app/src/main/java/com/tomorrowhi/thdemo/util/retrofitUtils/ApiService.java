package com.tomorrowhi.thdemo.util.retrofitUtils;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zhaotaotao on 14/01/2017.
 * http api
 */
public interface ApiService {

    @GET("/")
    Flowable<ResponseBody> getText();

    @GET("{url}")
    Flowable<ResponseBody> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps
    );

    @POST("{url}")
    Flowable<ResponseBody> executePost(
            @Path("url") String url,
            @FieldMap Map<String, String> maps
    );

    @POST("{url}")
    Flowable<ResponseBody> json(
            @Path("url") String url,
            @Body RequestBody jsonStr
    );

    @Multipart
    @POST("{url}")
    Flowable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody avatar
    );

    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Path("filename") String description,
            @PartMap() Map<String, RequestBody> maps
    );

    @Streaming
    @GET
    Flowable<ResponseBody> downloadFile(@Url String fileUlr);

}
