package com.ustclin.petchicken.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cn.waps.AppConnect;
import cn.waps.AppListener;

/**
 * author: LinKun EmployeeID:151750
 * email : linkun199011@163.com
 * created on: 2016/11/1 15:35
 * description:
 */
public class AdUtils {
    private static final String TAG = "AdUtils";
    // 万普配置信息，设置“yes”后，接受广告
    public static boolean showAd(Context context) {
        return false;
//        SharedPreferences isFirstSP = context.getSharedPreferences(SharedPreferencesUtils.IS_FIRST_SETTING, Context.MODE_PRIVATE);
//        boolean isBuy;
//        if (isFirstSP.contains(SharedPreferencesUtils.IS_BUY_APP)) {
//            isBuy = true;
//        } else {
//            isBuy = false;
//        }
//        if (isBuy) {
//            // VIP 不显示广告
//            return false;
//        }
//
//        String showAd = AppConnect.getInstance(context).getConfig("showAd", "no");
//        Log.i(TAG, showAd);
//        if (showAd.equals("yes")) {
//            // 万普广告
//            AppConnect.getInstance(context);
//            // 预加载插屏广告内容（仅在使用到插屏广告的情况，才需要添加）
//            AppConnect.getInstance(context).initPopAd(context);
//            //
//            AppConnect.getInstance(context).setPopAdNoDataListener(
//                    new AppListener() {
//                        @Override
//                        public void onPopNoData() {
//                            Log.i("debug", "插屏广告暂无可用数据");
//                        }
//                    });
//            // 显示插屏广告
//            AppConnect.getInstance(context).showPopAd(context);
////            isHaveAD = true;
//            return true;
//        }
//        else {
//            return false;
//        }
    }
}
