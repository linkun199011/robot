package com.ustclin.petchicken.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2016/11/19:13:04
 * description
 */
public class SharedPreferencesUtils {
    // 用户第一次使用
    public static void setFisrtSharedPreferences(Context context, SharedPreferences sp) {
        // 创建SharedPreferences文件
        sp = context.getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        if (!sp.contains("isFisrt")) {
            // 用户第一次启动该运用
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFisrt", false);
            editor.commit();
        }
    }
}
