package com.ustclin.petchicken.detail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;

/**
 * Created by LinKun on 2016/11/16.
 */
public class DetailActivity extends Activity {
    // type: pet / master
    private String mType;

    // title 左上角按键
    private ImageView mTitleBarBtn;
    // title name
    private TextView mTitleName;

    // header
    private ImageView mImageViewHeader;
    // nickName
    private EditText mNickName;
    // sex relative layout
    private RelativeLayout mRelativeLayoutMasterSex;
    private RelativeLayout mRelativeLayoutPetSex;
    //
    private RadioGroup mRadioGroupMasterSex;
    // sex
    private EditText mSex;
    // age
    private EditText mAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        StatusBarUtils.setMainChatActivityStatusBarColor(this);
        mType = getIntent().getStringExtra("type");
        initView();
    }

    private void initView() {
        // 公共view 初始化
        mTitleBarBtn = (ImageView) findViewById(R.id.title_bar_menu_btn);
        mTitleBarBtn.setBackgroundResource(R.drawable.ic_actionbar_back_normal);
        mTitleName = (TextView) findViewById(R.id.title_bar_name);
        mTitleBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // init by type
        mRelativeLayoutMasterSex = (RelativeLayout) findViewById(R.id.rl_master_sex);
        mRelativeLayoutPetSex = (RelativeLayout) findViewById(R.id.rl_pet_sex);
        mImageViewHeader = (ImageView) findViewById(R.id.header);
        mNickName = (EditText) findViewById(R.id.et_nick_name);
        mSex = (EditText) findViewById(R.id.et_pet_sex);
        mRadioGroupMasterSex = (RadioGroup) findViewById(R.id.rg_master_sex);
        mAge = (EditText) findViewById(R.id.et_age);

        if (mType.equals("pet")) {
            mTitleName.setText("萌宠");
            mRelativeLayoutPetSex.setVisibility(View.VISIBLE);
            mRelativeLayoutMasterSex.setVisibility(View.GONE);
            mImageViewHeader.setImageResource(R.drawable.icon);
            mNickName.setText("小黄鸡");
            mSex.setText("女");
            mSex.setEnabled(false);
            mAge.setText("3");
            mAge.setEnabled(false);
        } else {
            mTitleName.setText("主人");
            mRelativeLayoutMasterSex.setVisibility(View.VISIBLE);
            mRelativeLayoutPetSex.setVisibility(View.GONE);
            mImageViewHeader.setImageResource(R.drawable.my);
            mNickName.setText("主人");
            mAge.setText("18");
        }

    }
}
