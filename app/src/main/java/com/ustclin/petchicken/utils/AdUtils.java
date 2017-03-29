package com.ustclin.petchicken.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ustclin.ads.AppConnect;
import com.ustclin.ads.AppListener;

/**
 * author: LinKun EmployeeID:151750
 * email : linkun199011@163.com
 * created on: 2016/11/1 15:35
 * description:
 */
public class AdUtils {
    private final String TAG = "AdUtils";
    // should connect to you-know-where website
    private boolean shouldConnectToWP = true;

    private volatile static AdUtils singleton;

    private AdUtils() {
    }

    public static AdUtils getInstance() {
        if (singleton == null) {
            synchronized (AdUtils.class) {
                if (singleton == null) {
                    singleton = new AdUtils();
                }
            }
        }
        return singleton;
    }

    public boolean shouldConnect(Context context) {
        boolean isHit = isBaidu(context) && isSensitiveTime();
        Log.i(TAG, "isLimited = " + isHit);
        return !isHit;
    }

    public boolean showAd(Context context) {
        if (!shouldConnect(context)) {
            return false;
        }
        SharedPreferences isFirstSP = context.getSharedPreferences(SharedPreferencesUtils.IS_FIRST_SETTING, Context.MODE_PRIVATE);
        boolean isBuy;
        if (isFirstSP.contains(SharedPreferencesUtils.IS_BUY_APP)) {
            isBuy = true;
        } else {
            isBuy = false;
        }
        if (isBuy) {
            // VIP 不显示ads
            return false;
        }

        String showAd = AppConnect.getInstance(context).getConfig("showAd", "no");
        Log.i(TAG, showAd);
        if (showAd.equals("yes")) {
            AppConnect.getInstance(context);
            // 预加载插 屏内容（仅在使用到插 屏的情况，才需要添加）
            AppConnect.getInstance(context).initPopAd(context);
            //
            AppConnect.getInstance(context).setPopAdNoDataListener(
                    new AppListener() {
                        @Override
                        public void onPopNoData() {
                            Log.i("debug", "插 屏暂无可用数据");
                        }
                    });
            // 显示插 屏
            AppConnect.getInstance(context).showPopAd(context);
//            isHaveAD = true;
            return true;
        } else {
            return false;
        }
    }


    /**
     * 是否显示购买对话框
     */
    public boolean showPurchaseDialog(Context context) {
        if (!shouldConnect(context)) {
            return false;
        }
        String purchaseStr = AppConnect.getInstance(context).getConfig("showPurchaseDialog", "no");
        if (purchaseStr.equals("yes")) {
            return true;
        }
        return false;
    }


    private boolean isBaidu(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo == null) {
            return false;
        }
        String msg = appInfo.metaData.getString("APP_PID", "360");
        if (msg != null && ( msg.equals("baidu")
                || msg.equals("QQ")
                || msg.equals("360")
                || msg.equals("xiaomi")
                || msg.equals("huawei")
                || msg.equals("wandoujia"))) {
            Log.i(TAG, " msg == " + msg);
            return true;
        }
        return false;
    }


    /**
     * 是否是敏感时间
     */
    private boolean isSensitiveTime() {
        long sensitiveTime = 1492214400000l; // 2017-04-15
        long currentTime = System.currentTimeMillis();
        if (currentTime < sensitiveTime) {
            Log.i(TAG, "is SensitiveTime");
            return true;
        } else {
            return false;
        }
    }
}
