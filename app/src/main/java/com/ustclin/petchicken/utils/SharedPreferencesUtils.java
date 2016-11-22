package com.ustclin.petchicken.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ustclin.robot.R;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2016/11/19:13:04
 * description
 */
public class SharedPreferencesUtils {
    /**
     * set first sharedPreferences
     */
    public static void setFirstSharedPreferences(SharedPreferences isFirstSP) {
        if (!isFirstSP.contains("isFirst")) {
            // 用户第一次启动该运用
            SharedPreferences.Editor editor = isFirstSP.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();
        }
    }

    /**
     * set default pet settings
     */
    public static void setDefaultPetSharedPreferences(Context context, SharedPreferences defaultSP) {
        SharedPreferences.Editor editor = defaultSP.edit();
        if (!defaultSP.contains("petName")) {
            editor.putString("petName", context.getString(R.string.pet_name));
        }
        if (!defaultSP.contains("petSex")) {
            editor.putString("petSex", context.getString(R.string.pet_sex));
        }
        if (!defaultSP.contains("petAge")) {
            editor.putString("petAge", context.getString(R.string.pet_age));
        }
        if (!defaultSP.contains("pet_switch")) {
            editor.putBoolean("pet_switch", true);
        }
        if (!defaultSP.contains("pet_voicer")) {
            editor.putString("pet_voicer", "xiaowanzi");
        }
        if (!defaultSP.contains("pet_voice_type")) {
            editor.putString("pet_voice_type", "auto"); // auto / manual
        }
        editor.commit();
    }

    /**
     * set default master settings
     */
    public static void setDefaultMasterSharedPreferences(Context context, SharedPreferences defaultSP) {
        SharedPreferences.Editor editor = defaultSP.edit();
        if (!defaultSP.contains("masterName")) {
            editor.putString("masterName", context.getString(R.string.master_name));
        }
        if (!defaultSP.contains("masterSex")) {
            editor.putString("masterSex", context.getString(R.string.master_sex));
        }
        if (!defaultSP.contains("masterAge")) {
            editor.putString("masterAge", context.getString(R.string.master_age));
        }
        if (!defaultSP.contains("master_switch")) {
            editor.putBoolean("master_switch", true);
        }
        if (!defaultSP.contains("master_voicer")) {
            editor.putString("master_voicer", "xiaoxin");
        }
        if (!defaultSP.contains("master_voice_type")) {
            editor.putString("master_voice_type", "manual"); // auto / manual
        }
        editor.commit();
    }

}
