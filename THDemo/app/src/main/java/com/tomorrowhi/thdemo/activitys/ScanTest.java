package com.tomorrowhi.thdemo.activitys;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.functions.Consumer;

/**
 * Created by zhaotaotao on 2017/12/29.
 */
public class ScanTest extends BaseActivity implements QRCodeView.Delegate {

    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;
    @BindView(R.id.z_xing_view)
    ZXingView mZXingView;
    @BindView(R.id.et_result)
    EditText mEtResult;
    @BindView(R.id.tv_sacn_num)
    TextView mTvSacnNum;
    @BindView(R.id.bt_copy)
    Button mBtCopy;
    @BindView(R.id.bt_qcode)
    Button mBtQcode;
    @BindView(R.id.bt_bar_code)
    Button mBtBarCode;
    @BindView(R.id.bt_start_code)
    Button mBtStartCode;
    @BindView(R.id.bt_check_code)
    Button mBtCheckCode;
    @BindView(R.id.bt_2_check_code)
    Button mBt2CheckCode;

    private Set<Long> imeiSet = new HashSet<>();
    private List<Long> tempList;
    private boolean isNoErr = true;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
        new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //同意
                    mZXingView.startCamera();
                    onViewClicked(mBtBarCode);
                } else {
                    ToastUtils.showShort("请打开拍照权限");
                    //拒绝
                }
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        mEtResult.setText("");
    }

    @Override
    protected void initView() {
        mZXingView.setDelegate(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera();
    }

    @Override
    protected void onResume() {
        mZXingView.showScanRect();
        mZXingView.startSpot();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mZXingView.stopSpot();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtils.d("扫描到的数据：" + result);
        //震动一下
//        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        if (vibrator != null) {
//            vibrator.vibrate(200);
//        }
        if (result.length() == 15) {
            if (imeiSet.contains(Long.valueOf(result))) {
                ToastUtils.showShort(result + " 已存在");
            } else {
                imeiSet.add(Long.valueOf(result));
                tempList = new ArrayList(imeiSet);
                Collections.sort(tempList);
                mEtResult.setText(Arrays.toString(tempList.toArray()));
                mTvSacnNum.setText(tempList.size() + "");
            }
        } else {
            LogUtils.d("无效扫描");
        }
        mZXingView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }


    @OnClick({R.id.bt_qcode, R.id.bt_bar_code, R.id.bt_copy, R.id.title_return_iv
            , R.id.bt_start_code, R.id.bt_check_code,R.id.bt_2_check_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_qcode:
                mZXingView.changeToScanQRCodeStyle();
                mZXingView.startSpot();
                break;
            case R.id.bt_bar_code:
                mZXingView.changeToScanBarcodeStyle();
                mZXingView.startSpot();
                break;
            case R.id.bt_copy:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", mEtResult.getText().toString());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtils.showShort("复制成功，可以粘贴到QQ里面进行发送");
                break;
            case R.id.title_return_iv:
                finish();
                break;
            case R.id.bt_start_code:
                mZXingView.startSpot();
                break;
            case R.id.bt_check_code:
                if (tempList != null) {
                    for (int i = 0; i < (tempList.size() - 1); i++) {
                        if ((tempList.get(i + 1) - tempList.get(i)) > 30) {
                            //查询有没有间隔大于30的，有的话，进行提示
                            ToastUtils.showShort(String.valueOf(tempList.get(i)) + "附近可能有异常");
                            isNoErr = false;
                            break;

                        }else {
                            isNoErr = true;
                        }
                    }
                    if (isNoErr){
                        ToastUtils.showShort("未检测到异常，请注意开头和结尾的IMEI");
                    }
                }
                break;
            case R.id.bt_2_check_code:
                if (tempList != null) {
                    for (int i = 0; i < (tempList.size() - 1); i++) {
                        if ((tempList.get(i + 1) - tempList.get(i)) > 16) {
                            //查询有没有间隔大于30的，有的话，进行提示
                            ToastUtils.showShort(String.valueOf(tempList.get(i)) + "附近可能有异常");
                            isNoErr = false;
                            break;

                        }else {
                            isNoErr = true;
                        }
                    }
                    if (isNoErr){
                        ToastUtils.showShort("未检测到异常，请注意开头和结尾的IMEI");
                    }
                }
                break;
        }
    }

}
