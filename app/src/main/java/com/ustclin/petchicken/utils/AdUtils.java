package com.ustclin.petchicken.utils;

import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String TAG = "AdUtils";
    public static boolean showAd(Context context) {
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
        }
        else {
            return false;
        }
    }

    /**
     * 是否显示购买对话框
     */
    public static boolean showPurchaseDialog(Context context) {
        String purchaseStr = AppConnect.getInstance(context).getConfig("showPurchaseDialog", "no");
        if (purchaseStr.equals("yes")) {
            return true;
        }
        return false;
    }
}
