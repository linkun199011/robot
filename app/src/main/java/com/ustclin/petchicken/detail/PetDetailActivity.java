package com.ustclin.petchicken.detail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ustclin.petchicken.utils.SharedPreferencesUtils;
import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;

/**
 * 详情页面
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2016/11/20:02:13
 * description
 */
public class PetDetailActivity extends Activity {
    private static final String TAG = PetDetailActivity.class.getName();
    private Context mContext;
    private SharedPreferences petSP;
    // title 左上角按键
    private ImageView mTitleBarBtn;
    // title name
    private TextView mTitleName;

    // header
    private ImageView mImageViewHeader;
    private Button mBtnChangeHeader;
    // nickName
    private EditText mNickName;

    // switch
    private Switch mVoiceSwitch;
    // change voicer
    private Button mChangeVoicer;
    // save
    private Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.details_pet);
        StatusBarUtils.setMainChatActivityStatusBarColor(this);
        initView();
        initSharedPreference();
    }

    private void initSharedPreference() {
        petSP = mContext.getSharedPreferences("petSetting", Context.MODE_PRIVATE);
        SharedPreferencesUtils.setDefaultPetSharedPreferences(mContext, petSP);
    }

    private void initView() {
        // 公共view 初始化
        mTitleBarBtn = (ImageView) findViewById(R.id.title_bar_menu_btn);
        mTitleBarBtn.setBackgroundResource(R.drawable.ic_actionbar_back_normal);
        mTitleBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleName = (TextView) findViewById(R.id.title_bar_name);
        mTitleName.setText(R.string.pet_name);

        mImageViewHeader = (ImageView) findViewById(R.id.pet_header);
        mBtnChangeHeader = (Button) findViewById(R.id.change_pet_header);
        mNickName = (EditText) findViewById(R.id.et_pet_nick_name);

        mVoiceSwitch = (Switch) findViewById(R.id.pet_switch);
        mChangeVoicer = (Button) findViewById(R.id.change_pet_voicer);
        mSave = (Button) findViewById(R.id.save_pet_detail);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
