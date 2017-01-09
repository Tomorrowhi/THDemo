package com.tomorrowhi.thdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhaotaotao on 2016/11/11.
 * 接收推送消息
 */

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //TODO 接收到的推送消息
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            postNotification(context, bundle, notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            openNotification(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 打开了通知栏消息的动作
     *
     * @param context context
     * @param bundle  bundle
     */
    private void openNotification(Context context, Bundle bundle) {
        //发送通知
//        JPushCustomNotificationMsgBean msgBean = parseBundle(bundle);
//        JPushCustomNotificationMsgAppNoShowBean msgAppNoShowBean = new JPushCustomNotificationMsgAppNoShowBean(
//                msgBean.getMessage(),
//                msgBean.getAlert(), msgBean.getExtras());
//        MyApplication.getInstance().getEventBus().postSticky(msgAppNoShowBean);
//        MyApplication.getInstance().getEventBus().postSticky(new UpdataMsgFragmentBean(true));
//        //根据app是否在前台，打开不同的页面
//        if (ToolUtils.isBackground(context)) {
//            LogUtils.d(LogUtils.JPUSHRECEIVER, "开始打开 LauncherActivity");
//            //打开自定义的Activity
//            Intent i = new Intent(context, LauncherActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);
//        } else {
//            LogUtils.d(LogUtils.JPUSHRECEIVER, "直接打开 MainActivity");
//            Intent i = new Intent(context, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);
//        }

    }

    /**
     * 发送通知消息到前台
     *
     * @param context       context
     * @param bundle        bundle
     * @param notifactionId id
     */
    private void postNotification(Context context, Bundle bundle, int notifactionId) {
//        JPushCustomNotificationMsgBean jPushCustomMsgBean = parseBundle(bundle);
//        String dataType = jPushCustomMsgBean.getExtras().get(MyConstants.DATA_TYPE);
//        if (dataType != null) {
//            Integer msgType = Integer.valueOf(dataType);
//            Long deviceId = Long.valueOf(jPushCustomMsgBean.getExtras().get(MyConstants.DEVICEID));
//            LastMsgInfoDao lastMsgInfoDao = MyApplication.getInstance().getDaoSession().getLastMsgInfoDao();
//            LastMsgInfo lastMsgInfo;
//            switch (msgType) {
//                //根据不同的消息类型，存储通知消息
//                case MyConstants.ALARM_MSG:
//                    //报警信息，区分消息类型
//                    lastMsgInfo = lastMsgInfoDao.queryBuilder().where(LastMsgInfoDao.Properties.Msg_type.eq(msgType)
//                    ).build().unique();
//                    if (lastMsgInfo == null) {
//                        //不存在，则插入
//                        lastMsgInfo = new LastMsgInfo(null,
//                                msgType,
//                                deviceId,
//                                DateUtil.parseDateStr(System.currentTimeMillis()),
//                                jPushCustomMsgBean.getAlert(),
//                                jPushCustomMsgBean.getExtras().get(MyConstants.MESSAGE), false);
//                        lastMsgInfoDao.insert(lastMsgInfo);
//                    } else {
//                        //存在，则更新
//                        lastMsgInfo.setCreate_time(DateUtil.parseDateStr(System.currentTimeMillis()));
//                        lastMsgInfo.setContent(jPushCustomMsgBean.getAlert());
//                        lastMsgInfo.setRemarks(jPushCustomMsgBean.getExtras().get(MyConstants.MESSAGE));
//                        lastMsgInfo.setIs_read(false);
//                        lastMsgInfoDao.update(lastMsgInfo);
//                    }
//                    break;
//                case MyConstants.APPLY_MSG:
//                    //申请请求，区分消息类型
//                    lastMsgInfo = lastMsgInfoDao.queryBuilder().where(LastMsgInfoDao.Properties.Msg_type.eq(msgType)
//                    ).build().unique();
//                    if (lastMsgInfo == null) {
//                        //不存在，则插入
//                        lastMsgInfo = new LastMsgInfo(null,
//                                msgType,
//                                deviceId,
//                                DateUtil.parseDateStr(System.currentTimeMillis()),
//                                jPushCustomMsgBean.getAlert(),
//                                jPushCustomMsgBean.getExtras().get(MyConstants.MESSAGE), false);
//                        lastMsgInfoDao.insert(lastMsgInfo);
//                    } else {
//                        //存在，则更新
//                        lastMsgInfo.setCreate_time(DateUtil.parseDateStr(System.currentTimeMillis()));
//                        lastMsgInfo.setContent(jPushCustomMsgBean.getAlert());
//                        lastMsgInfo.setRemarks(jPushCustomMsgBean.getExtras().get(MyConstants.MESSAGE));
//                        lastMsgInfo.setIs_read(false);
//                        lastMsgInfoDao.update(lastMsgInfo);
//                    }
//                    break;
//                case MyConstants.DEVICE_REPLY_MSG:
//                    //回复消息，区分设备id和消息类型
//                    //查询数据
//                    lastMsgInfo = lastMsgInfoDao.queryBuilder().where(LastMsgInfoDao.Properties.Msg_type.eq(msgType),
//                            LastMsgInfoDao.Properties.Device_id.eq(deviceId)).build().unique();
//                    if (lastMsgInfo == null) {
//                        //不存在，则插入
//                        lastMsgInfo = new LastMsgInfo(null,
//                                msgType,
//                                deviceId,
//                                DateUtil.parseDateStr(System.currentTimeMillis()),
//                                jPushCustomMsgBean.getAlert(),
//                                jPushCustomMsgBean.getExtras().get(MyConstants.IMEI), false);
//                        lastMsgInfoDao.insert(lastMsgInfo);
//                    } else {
//                        //存在，则更新
//                        lastMsgInfo.setCreate_time(DateUtil.parseDateStr(System.currentTimeMillis()));
//                        lastMsgInfo.setContent(jPushCustomMsgBean.getAlert());
//                        lastMsgInfo.setRemarks(jPushCustomMsgBean.getExtras().get(MyConstants.IMEI));
//                        lastMsgInfo.setIs_read(false);
//                        lastMsgInfoDao.update(lastMsgInfo);
//                    }
//                    break;
//            }
//        }
//        if (MainActivity.isMainForeground) {
//            //当Actiity在前台时，移除通知
//            JPushInterface.clearNotificationById(context, notifactionId);
//            //发送通知消息
//            MyApplication.getInstance().getEventBus().post(jPushCustomMsgBean);
//            MyApplication.getInstance().getEventBus().post(new UpdataMsgFragmentBean(true));
//        } else if (MyApplication.isAppForeground) {
//            //app存在，没有手动退出
//
//        }

    }

//    /**
//     * 解析数据
//     *
//     * @param bundle bundle
//     * @return object
//     */
//    private JPushCustomNotificationMsgBean parseBundle(Bundle bundle) {
//        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        Map<String, String> map = null;
//        if (!ExampleUtil.isEmpty(extras)) {
//            map = JsonUtils.toMap(extras);
//        }
//        JPushCustomNotificationMsgBean msgBean = new JPushCustomNotificationMsgBean(message, alert, map);
//        LogUtils.d("jPushCustomNotificationMsgBean" + msgBean.toString());
//        return msgBean;
//    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isMainForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            JPushCustomMsgBean jPushCustomMsgBean = new JPushCustomMsgBean(message, extras);
//            LogUtils.d(LogUtils.APP_MAINACTIVITY, jPushCustomMsgBean.toString());
//            MyApplication.getInstance().getEventBus().post(jPushCustomMsgBean);
////            context.sendBroadcast(msgIntent);
//        }
    }
}
