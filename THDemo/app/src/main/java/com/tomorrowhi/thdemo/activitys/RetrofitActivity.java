package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                break;
            case R.id.retrofit_download:
                break;
        }
    }
}
