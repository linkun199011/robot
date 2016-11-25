package com.ustclin.petchicken.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * author: LinKun EmployeeID:151750
 * email : linkun199011@163.com
 * created on: 2016/11/1 15:35
 * description:
 */
public class StatusBarUtils {

    /**
     * 设置状态栏背景色
     * @param activity 上下文
     */
    public static void setMainChatActivityStatusBarColor(Activity activity) {
        try {
            if (activity != null
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                // #FF313131
                window.setStatusBarColor(Color.argb(255,49,49,49));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAbilitiesActivityStatusBarColor(Activity activity) {
        try {
            if (activity != null
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                // ##3C3F41
                window.setStatusBarColor(Color.argb(255,60,63,65));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
