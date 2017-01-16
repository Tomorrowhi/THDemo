package com.tomorrowhi.thdemo.util.retrofitUtils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.utils.LogUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tomorrowhi.thdemo.MyApplication;
import com.tomorrowhi.thdemo.util.retrofitUtils.progress.ProgressHelper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by zhaotaotao on 2016/11/8.
 * retrofit网络服务
 */

public class RetrofitUtils {

    private static final long CONNECT_TIME_OUT = 40;  //超时时间40s

    private RetrofitUtils() {
    }


    /**
     * 创建retrofit对象
     *
     * @param service api接口
     * @param baseUrl 请求的域名地址
     * @param isLog   日志开关
     * @param <T>     泛型标记
     * @return retrofit对象
     */
    public static <T> T createRetrofitService(final Class<T> service, String baseUrl, boolean isLog, Context context) {
        OkHttpClient.Builder builder = getOkHttpBuilder(isLog, context);
        Retrofit retrofit = new Retrofit.Builder()
                //okhttp的进度条
                .client(ProgressHelper.addProgress(builder).build())
//                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)

                .build();

        return retrofit.create(service);
    }

    @NonNull
    private static OkHttpClient.Builder getOkHttpBuilder(boolean isLog, final Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (isLog) {
            //控制属否开始Log日志，如果打开，会将所有的内容加载到内存，调试时用，发布时务必关闭！
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            HttpLoggingInterceptor.Level body;
            body = HttpLoggingInterceptor.Level.BODY;
            httpLoggingInterceptor.setLevel(body);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        File cacheFile = new File(context.getCacheDir().getAbsolutePath(), "cacheData");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);//缓存文件为10MB

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                int maxAge = 60 * 60; // 有网络时 设置缓存超时时间1个小时
                int maxStale = 60 * 60 * 24 * 7 * 4; // 无网络时，设置超时为4周
                Request request = chain.request();
                if (NetworkUtil.isNetworkAvailable(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)//有网络时只从网络获取
                            .addHeader(MyApplication.BASE_URL_KEY, MyApplication.BASE_URL_KEY_VALUE)
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .build();
                } else {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)//无网络时只从缓存中读取
                            .addHeader(MyApplication.BASE_URL_KEY, MyApplication.BASE_URL_KEY_VALUE)
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .build();
                    LogUtils.d("使用缓存数据");
                }
                Response response = chain.proceed(request);
                if (NetworkUtil.isNetworkAvailable(context)) {
                    response = response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    response = response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
                return response;
            }

        })
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .cache(cache)
                .build();

        return builder;
    }


}
