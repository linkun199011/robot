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
    // ----------------  isFirstSetting.xml ------------------------------
    public static final String IS_FIRST_SETTING = "isFirstSetting";
    public static final String IS_FIRST_START_APP = "isFirstStartApp";
    public static final String IS_FIRST_ENTER_PET_DETAIL = "isFirstEnterPetDetail";
    public static final String IS_FIRST_ENTER_MASTER_DETAIL = "isFirstEnterMasterDetail";

    // ---------------- petSetting.xml  ----------------
    public static final String PET_SETTING = "petSetting";
    // ---------------- masterSetting.xml ----------------
    public static final String MASTER_SETTING = "masterSetting";


    /**
     * set first sharedPreferences
     */
    public static void setFirstSharedPreferences(SharedPreferences isFirstSP) {
        if (!isFirstSP.contains(IS_FIRST_START_APP)) {
            // 用户第一次启动该运用,会有引导页
            SharedPreferences.Editor editor = isFirstSP.edit();
            editor.putBoolean(IS_FIRST_START_APP, false);
            editor.apply();
        }
    }

    /**
     * set first pet detail
     */
    public static void setFirstPetDetail(SharedPreferences isFirstSP) {
        if (!isFirstSP.contains(IS_FIRST_ENTER_PET_DETAIL)) {
            // 用户第一次启动PetDetail, 会初始化一些参数
            SharedPreferences.Editor editor = isFirstSP.edit();
            editor.putBoolean(IS_FIRST_ENTER_PET_DETAIL, false);
            editor.apply();
        }
    }

    /**
     * set first master detail
     */
    public static void setFirstMasterDetail(SharedPreferences isFirstSP) {
        if (!isFirstSP.contains(IS_FIRST_ENTER_MASTER_DETAIL)) {
            // 用户第一次启动PetDetail, 会初始化一些参数
            SharedPreferences.Editor editor = isFirstSP.edit();
            editor.putBoolean(IS_FIRST_ENTER_MASTER_DETAIL, false);
            editor.apply();
        }
    }

    /**
     * set default pet settings
     */
    public static void setDefaultPetSharedPreferences(Context context, SharedPreferences defaultSP) {
        SharedPreferences.Editor editor = defaultSP.edit();
        if (!defaultSP.contains("Name")) {
            editor.putString("Name", context.getString(R.string.pet_name));
        }
        if (!defaultSP.contains("Sex")) {
            editor.putString("Sex", context.getString(R.string.pet_sex));
        }
        if (!defaultSP.contains("Age")) {
            editor.putString("Age", context.getString(R.string.pet_age));
        }
        if (!defaultSP.contains("Voicer")) {
            editor.putString("Voicer", "xiaowanzi");
        }
        if (!defaultSP.contains("VoiceType")) {
            editor.putString("VoiceType", "auto"); // auto / manual
        }
        editor.apply();
    }

    /**
     * set custom pet settings
     */
    public static void setCustomPetSettings(Context context, String name, String voicer,
                                            String voiceType, SharedPreferences petSp) {
        SharedPreferences.Editor editor = petSp.edit();
        editor.putString("Name", name);
        editor.putString("Voicer", voicer);
        editor.putString("VoiceType", voiceType);
        editor.apply();
    }

    /**
     * set default master settings
     */
    public static void setDefaultMasterSharedPreferences(Context context, SharedPreferences defaultSP) {
        SharedPreferences.Editor editor = defaultSP.edit();
        if (!defaultSP.contains("Name")) {
            editor.putString("Name", context.getString(R.string.master_name));
        }
        if (!defaultSP.contains("Sex")) {
            editor.putString("Sex", context.getString(R.string.master_sex));
        }
        if (!defaultSP.contains("Age")) {
            editor.putString("Age", context.getString(R.string.master_age));
        }
        if (!defaultSP.contains("Voicer")) {
            editor.putString("Voicer", "xiaoxin");
        }
        if (!defaultSP.contains("VoiceType")) {
            editor.putString("VoiceType", "manual"); // auto / manual
        }
        editor.apply();
    }


    /**
     * set custom master settings
     */
    public static void setCustomMasterSettings(Context context, String name, String sex, String age,
                                               String voicer, SharedPreferences masterSp) {
        SharedPreferences.Editor editor = masterSp.edit();
        editor.putString("Name", name);
        editor.putString("Sex", sex);
        editor.putString("Age", age);
        editor.putString("Voicer", voicer);
        editor.apply();
    }

}
