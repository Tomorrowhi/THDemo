package com.tomorrowhi.thdemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tbruyelle.rxpermissions2.RxPermissionsFragment;
import com.tomorrowhi.thdemo.MyApplication;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.RxFragment;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by zhaotaotao on 2016/11/9.
 * fragment
 */
public abstract class BaseFragment extends RxFragment {

    public MyApplication myApplication;
    public Context mContext;
    public View mView;
    public RxPermissionsFragment rxPermissionsFragment;
    public final PublishSubject<FragmentEvent> lifePublishSubject = PublishSubject.create();
    public RxSharedPreferences defaultRxPreferences;
    public RxSharedPreferences userRxPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifePublishSubject.onNext(FragmentEvent.CREATE);
        mContext = getActivity();
        myApplication = MyApplication.getInstance();
        rxPermissionsFragment = new RxPermissionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lifePublishSubject.onNext(FragmentEvent.CREATE_VIEW);
        mView = inflater.inflate(getLayoutRes(), container, false);
        initView(mView);
        initSP();
        initData();
        initEvent();
        initComplete(savedInstanceState);
        return mView;
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

    protected abstract void initView(View mView);

    protected abstract void initComplete(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();


    protected abstract int getLayoutRes();

    @Override
    public void onDestroy() {
        myApplication.getEventBus().unregister(this);
        lifePublishSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lifePublishSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifePublishSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifePublishSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifePublishSubject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifePublishSubject.onNext(FragmentEvent.STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifePublishSubject.onNext(FragmentEvent.DESTROY_VIEW);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifePublishSubject.onNext(FragmentEvent.DETACH);
    }
}
