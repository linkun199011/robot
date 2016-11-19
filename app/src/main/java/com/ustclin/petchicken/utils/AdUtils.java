package com.ustclin.petchicken.utils;

import android.content.Context;
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
    // 万普配置信息，设置“yes”后，接受广告
    public static boolean showAd(Context context) {
        String showad = AppConnect.getInstance(context).getConfig("showAd", "no");
        Log.e("fffffff", showad);
        if (showad.equals("yes")) {
            // 万普广告
            AppConnect.getInstance(context);
            // 预加载插屏广告内容（仅在使用到插屏广告的情况，才需要添加）
            AppConnect.getInstance(context).initPopAd(context);
            //
            AppConnect.getInstance(context).setPopAdNoDataListener(
                    new AppListener() {
                        @Override
                        public void onPopNoData() {
                            Log.i("debug", "插屏广告暂无可用数据");
                        }
                    });
            // 显示插屏广告
            AppConnect.getInstance(context).showPopAd(context);
//            isHaveAD = true;
            return true;
        }
        else {
            return false;
        }
    }
}
