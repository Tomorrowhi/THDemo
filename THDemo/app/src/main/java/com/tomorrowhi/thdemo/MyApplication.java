package com.tomorrowhi.thdemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.google.android.gms.ads.MobileAds;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.tomorrowhi.thdemo.dao.DaoMaster;
import com.tomorrowhi.thdemo.dao.DaoSession;
import com.tomorrowhi.thdemo.util.GreenDaoUtils.GreenDaoOptionHelper;
import com.tomorrowhi.thdemo.util.imageLoderUtils.PicassoUtil;
import com.tomorrowhi.thdemo.util.retrofitUtils.ComApi;
import com.tomorrowhi.thdemo.util.retrofitUtils.ComInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.database.Database;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by zhaotaotao on 09/01/2017.
 * MyApplication
 */
public class MyApplication extends MultiDexApplication {

    public static final String BASE_URL = "https://api.waqi.info";
    public static final String BASE_URL_APPID = "";
    public static final String BASE_URL_KEY = "";
    public static final String BASE_URL_KEY_VALUE = "";
    public static final String BASE_URL_MAP_TYPE_AMAP = "AMap"; //高德地图
    public static boolean isLogAndDebug = true; //log和debug模式的开关
    public static String logTag = "TH-Demo"; //log Tag
    public static final String DB_NAME = "th—demo";
    public static final String BUG_LY = "41fccb5801";

    private static MyApplication application;
    private static Context applicationContext;
    private ComInterface comApi;
    private EventBus eventBus;
    private DaoSession daoSession;
    private Database db;
    private static List<Activity> activityList = new LinkedList<>();

    public MyApplication() {
    }

    /**
     * 获取唯一的Application对象
     *
     * @return application
     */
    public static MyApplication getInstance() {
        if (null == application) {
            application = new MyApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (initLeakCanary()) return;
        applicationContext = this;
        initDebug();
        initUtils();
        initDefaultFont();
        initEventBus();
        initApisCache();
        initBugLy();
        initJPush();
        getLanguage();
//        initPicasso();
        initDAO();
//        initService();
        initADMob();
    }

    private void initADMob() {
        MobileAds.initialize(this, BuildConfig.ADMob);
    }

    private void initDefaultFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private boolean initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return true;
        }
        LeakCanary.install(this);
        return false;
    }


    private void initDebug() {
        //根据是否是Debug模式，决定日志部分的开关
        ApplicationInfo applicationInfo = this.getApplicationInfo();
        isLogAndDebug = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void initUtils() {
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(isLogAndDebug)
                .setGlobalTag(logTag)
                .setLog2FileSwitch(false)
                .setBorderSwitch(true);
    }

    private void initBugLy() {
        CrashReport.initCrashReport(getApplicationContext(), BUG_LY, isLogAndDebug);
//        CrashReport.putUserData(this, "appUser", appUser.toString());
//        CrashReport.putUserData(this, "watchBeanList", watchBeanList.toString());
    }

    private void initService() {
    }


    private void initDAO() {
        GreenDaoOptionHelper optionHelper = new GreenDaoOptionHelper(this.applicationContext(), DB_NAME, null);
        db = optionHelper.getReadableDb();
        daoSession = new DaoMaster(db).newSession();
    }


    public DaoSession getDaoSession() {
        if (null == daoSession) {
            initDAO();
        }
        return daoSession;
    }

    public Database getDb() {
        if (null == db) {
            initDAO();
        }
        return db;
    }

    public void closeDao() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (db != null) {
            db.close();
            db = null;
        }
    }

    private void initJPush() {
        JPushInterface.setDebugMode(isLogAndDebug);
        JPushInterface.init(this);
        JPushInterface.setLatestNotificationNumber(this, 5);//设置最近保留的通知条数
    }

    private void initPicasso() {
        PicassoUtil.setPicasso(this);
        PicassoUtil.setDefaultDrawable(R.mipmap.ic_launcher);
        PicassoUtil.setErrDrawable(R.mipmap.ic_launcher);
    }


    public ComInterface httpApis() {
        if (null == comApi) {
            initApisCache();
        }
        return comApi;
    }


    public Context applicationContext() {
        if (null == applicationContext) {
            applicationContext = this;
        }
        return applicationContext;

    }

    public EventBus getEventBus() {
        if (null == eventBus) {
            initEventBus();
        }
        return eventBus;
    }


    public void initEventBus() {
        eventBus = EventBus.builder().addIndex(new MyEventBusIndex()).build();
    }


    /**
     * 初始化网络接口服务
     */
    private void initApisCache() {
        comApi = ComApi.getInstance(applicationContext, BASE_URL, isLogAndDebug).getApi();
    }

    public String getLanguage() {
        return Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();

    }


    /**
     * 添加Activity到容器中
     *
     * @param activity activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 遍历所有Activity并finish
     */
    public void exit() {
        for (Activity activity : activityList) {
            if (activity != null)
                activity.finish();
        }
        activityList.clear();
    }

}
