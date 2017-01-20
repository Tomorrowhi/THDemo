package com.tomorrowhi.thdemo.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tomorrowhi.thdemo.MyApplication;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.tomorrowhi.thdemo.util.DialogUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by zhaotaotao on 2016/11/7.
 * Activity的基类
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    public MyApplication myApplication;
    public Context mContext;
    public Unbinder unbinder;
    public RxPermissions rxPermissions;
    public final PublishSubject<ActivityEvent> lifePublishSubject = PublishSubject.create();
    public RxSharedPreferences defaultRxPreferences;
    public RxSharedPreferences userRxPreferences;
    public CompositeDisposable mCompositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifePublishSubject.onNext(ActivityEvent.CREATE);
        mContext = this;
        rxPermissions = new RxPermissions(this);
        myApplication = MyApplication.getInstance();
        setContentView(getLayoutRes());
        unbinder = ButterKnife.bind(this);
        mCompositeDisposable = new CompositeDisposable();
        initSP();
        init(savedInstanceState);
        initView();
        initData();
        initEvent();
        initComplete(savedInstanceState);
        myApplication.addActivity(this);
    }

    private void initSP() {
        if (defaultRxPreferences == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            defaultRxPreferences = RxSharedPreferences.create(preferences);
        }
        if (userRxPreferences == null) {
            SharedPreferences preferences = mContext.getSharedPreferences(MyConstants.USER_INFO, Context.MODE_PRIVATE);
            userRxPreferences = RxSharedPreferences.create(preferences);
        }
    }


    protected abstract int getLayoutRes();

    protected abstract void initComplete(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void init(Bundle savedInstanceState);


    @Override
    protected void onDestroy() {
        myApplication.removeActivity(this);
        myApplication.getEventBus().unregister(this);
        unbinder.unbind();
        lifePublishSubject.onNext(ActivityEvent.DESTROY);
        DialogUtil.hide();
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifePublishSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifePublishSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifePublishSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifePublishSubject.onNext(ActivityEvent.START);
    }
}
