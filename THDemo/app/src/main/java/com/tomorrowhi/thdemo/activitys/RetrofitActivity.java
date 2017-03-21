package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.bean.AndroidApiTest;
import com.tomorrowhi.thdemo.bean.Repos;
import com.tomorrowhi.thdemo.bean.ResBaseModel;
import com.tomorrowhi.thdemo.bean.ShangHaiBean;
import com.tomorrowhi.thdemo.util.retrofitUtils.RetrofitResult;
import com.tomorrowhi.thdemo.util.retrofitUtils.RetrofitTools;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * Created by zhaotaotao on 10/01/2017.
 * retrofit 2 使用
 */
public class RetrofitActivity extends BaseActivity {
    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;
    @BindView(R.id.retrofit_result_tv)
    TextView retrofitResultTv;
    @BindView(R.id.app_upgrade_progress_bar)
    ProgressBar appUpgradeProgressBar;
    @BindView(R.id.app_upgrade_progress_bar_tv)
    TextView appUpgradeProgressBarTv;
    @BindView(R.id.app_update_download_rl)
    RelativeLayout appUpdateDownloadRl;
    @BindView(R.id.retrofit_post)
    Button retrofitPost;
    @BindView(R.id.retrofit_get)
    Button retrofitGet;
    @BindView(R.id.retrofit_download)
    Button retrofitDownload;


    private List<AndroidApiTest> apiTests;
    private ResBaseModel<ShangHaiBean> shangHaiBeanResBaseModel;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_retrofit;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @OnClick({R.id.title_return_iv, R.id.retrofit_post, R.id.retrofit_get, R.id.retrofit_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_return_iv:
                this.finish();
                break;
            case R.id.retrofit_post:
                //TODO retrofit 
                break;
            case R.id.retrofit_get:
                Observable<Repos> reposObservable = myApplication.httpApis().getRepos().compose(this.bindUntilEvent(ActivityEvent.DESTROY));
                RetrofitTools.httpRequest(false, false, null, null,
                        mCompositeDisposable, reposObservable, new RetrofitResult() {
                            @Override
                            public void handleResponse(Object object) {
                                Repos repos = (Repos) object;
                                LogUtils.d("Repos " + repos.toString());
                                LogUtils.d("Repos handleResponse");
                            }

                            @Override
                            public void handleError(Throwable error) {
                                LogUtils.d("Repos handleError");
                            }

                            @Override
                            public void handleComplete() {
                                LogUtils.d("Repos handleComplete");
                            }
                        });

                Observable<List<AndroidApiTest>> compose = myApplication.httpApis().androidApiTest()
                        .compose(this.bindUntilEvent(ActivityEvent.DESTROY));
                RetrofitTools.httpRequest(true, true, mContext, "正在加载AndroidAPI数据",
                        mCompositeDisposable, compose, new RetrofitResult() {
                            @Override
                            public void handleResponse(Object object) {
                                apiTests = (List<AndroidApiTest>) object;
                                retrofitResultTv.setText(Arrays.toString(apiTests.toArray()));
                            }

                            @Override
                            public void handleError(Throwable error) {
                                LogUtils.d(error.toString());
                            }

                            @Override
                            public void handleComplete() {
                                LogUtils.d("请求结束2");
                            }
                        });
                Observable<ResBaseModel<ShangHaiBean>> demo = myApplication.httpApis().getShanghaiData("demo")
                        .compose(this.bindUntilEvent(ActivityEvent.DESTROY));
                RetrofitTools.httpCommonRequest(false, false, null, null,
                        mCompositeDisposable, demo, new RetrofitResult() {

                            @Override
                            public void handleResponse(Object object) {
                                if (object != null) {
                                    shangHaiBeanResBaseModel = (ResBaseModel<ShangHaiBean>) object;
                                    LogUtils.d("shangHaiBeanResBaseModel:" + shangHaiBeanResBaseModel.toString());
                                }
                            }

                            @Override
                            public void handleError(Throwable error) {
                                LogUtils.d(error.toString());
                            }

                            @Override
                            public void handleComplete() {
                                LogUtils.d("请求结束3");
                            }
                        });
                break;
            case R.id.retrofit_download:
                break;
        }
    }


}
