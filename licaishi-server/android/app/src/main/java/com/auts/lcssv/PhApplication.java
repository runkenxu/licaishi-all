package com.auts.lcssv;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.auts.lcssv.util.LogUtils;
import com.auts.lcssv.util.UmengUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.litepal.LitePal;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;


/**
 * Application基类.
 */
public class PhApplication extends Application {
    private static Context mContext;
    public static boolean isJunitTest;
    private static PhApplication mApplication;
    public int count;
    private static boolean isBackGround;//判断当前App是否处于后台
    public static boolean mHasCheckUpdate;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mApplication = this;
        initThirdParty();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) {
                    LogUtils.debug(">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                    isBackGround = false;
                }
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    LogUtils.debug(">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                    isBackGround = true;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d("Destory", activity.getClass().getName());
            }
        });
    }

    private void initThirdParty() {
        LitePal.initialize(this);
        //极光推送
        JPushInterface.init(this);
        //友盟统计
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
//        MobclickAgent.setDebugMode( true );
        LogUtils.debug(UmengUtil.getDeviceInfo(getPhApplication()));
        //XLog
//        LogUtils.init();
        //bugly
//        CrashReport.initCrashReport(getApplicationContext());
        //xutils
//        x.Ext.init(this);
        //X5WebView
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
            }

            @Override
            public void onViewInitFinished(boolean b) {
                LogUtils.debug("isX5initSuccess: " + b);
            }
        });
        //阿里FeedBack
        FeedbackAPI.init(this, "24570564", "bdb43f3b69b749621de7fe23b4184043");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        LeakCanary.install(this);
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static PhApplication getPhApplication() {
        return mApplication;
    }

    public static boolean isBackGround() {
        return isBackGround;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
