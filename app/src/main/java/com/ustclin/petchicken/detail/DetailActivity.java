package com.ustclin.petchicken.detail;

import android.app.Activity;
import android.os.Bundle;

import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;

/**
 * Created by LinKun on 2016/11/16.
 */
public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        StatusBarUtils.setMainChatActivityStatusBarColor(this);
    }
}
