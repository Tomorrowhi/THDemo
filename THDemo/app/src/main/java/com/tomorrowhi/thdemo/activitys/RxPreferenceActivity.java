package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.f2prateek.rx.preferences2.Preference;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.trello.rxlifecycle2.android.ActivityEvent;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by zhaotaotao on 16/01/2017.
 * RxPreference  https://github.com/f2prateek/rx-preferences
 */

public class RxPreferenceActivity extends BaseActivity {
    @BindView(R.id.input_text_et)
    EditText inputTextEt;
    @BindView(R.id.write_bt)
    Button writeBt;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.read_bt)
    Button readBt;
    @BindView(R.id.sp_listen)
    TextView spListen;
    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;

    private Preference<String> rxPreference;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_rx_preference;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        //设置数据监听
        rxPreference.asObservable()
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        spListen.setText(s);
                    }
                });
    }

    @Override
    protected void initData() {
        rxPreference = defaultRxPreferences.getString("rx_preference", MyConstants.DEFAULT);
        content.setText(rxPreference.get());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.write_bt, R.id.read_bt, R.id.title_return_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.write_bt:
                rxPreference.set(inputTextEt.getText().toString());
                break;
            case R.id.read_bt:
                content.setText(rxPreference.get());
                break;
            case R.id.title_return_iv:
                finish();
                break;
        }
    }

}
