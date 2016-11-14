package com.ustclin.petchicken.about;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		setContentView(R.layout.about);
		StatusBarUtils.setMainChatActivityStatusBarColor(this);
	}
}
