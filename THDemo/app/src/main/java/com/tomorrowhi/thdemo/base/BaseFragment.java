package com.tomorrowhi.thdemo.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomorrowhi.thdemo.MyApplication;

/**
 * Created by zhaotaotao on 2016/11/9.
 * fragment
 */
public abstract class BaseFragment extends Fragment {

    public MyApplication myApplication;
    public Context mContext;
    public View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        myApplication = MyApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutRes(), container, false);
        initView(mView);
        initData();
        initEvent();
        initComplete(savedInstanceState);
        return mView;
    }

    protected abstract void initView(View mView);

    protected abstract void initComplete(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();


    protected abstract int getLayoutRes();

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApplication.getEventBus().unregister(this);

    }
}
