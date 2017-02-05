package com.ustclin.petchicken;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.iflytek.cloud.SpeechUtility;
import com.ustclin.petchicken.utils.SharedPreferencesUtils;
import com.ustclin.robot.R;

public class RobotApp extends Application {

    public static String gPetName = "萌宠";
    public static String gMasterName = "主人";

    @Override
    public void onCreate() {
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

        SpeechUtility.createUtility(RobotApp.this, "appid=" + getString(R.string.app_id));
        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);
        initSP();
        super.onCreate();
    }

    private void initSP() {
        SharedPreferences petSetting = this.getSharedPreferences(SharedPreferencesUtils.PET_SETTING, Context.MODE_PRIVATE);
        SharedPreferences masterSetting = this.getSharedPreferences(SharedPreferencesUtils.MASTER_SETTING, Context.MODE_PRIVATE);
        if (petSetting.contains("Name")) {
            gPetName = petSetting.getString("Name", "萌宠");
        }

        if (masterSetting.contains("Name")) {
            gMasterName = masterSetting.getString("Name", "主人");
        }
    }

}
