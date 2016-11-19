package com.ustclin.petchicken.detail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    private Button mBtnChangeHeader;
    // nickName
    private EditText mNickName;
    //
    private RadioGroup mRadioGroupMasterSex;
    // sex
    private EditText mSex;
    // age
    private EditText mAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getStringExtra("type");
        if (mType!=null && mType.equals("pet")) {
            setContentView(R.layout.details_pet);
        } else {
            setContentView(R.layout.details_master);
        }
        StatusBarUtils.setMainChatActivityStatusBarColor(this);
        initView();
    }

    private void initView() {
        // 公共view 初始化
        mTitleBarBtn = (ImageView) findViewById(R.id.title_bar_menu_btn);
        mTitleBarBtn.setBackgroundResource(R.drawable.ic_actionbar_back_normal);
        mTitleName = (TextView) findViewById(R.id.title_bar_name);
        if (mType.equals("pet")) {
            mTitleName.setText(R.string.pet_name);
        } else {
            mTitleName.setText(R.string.master_name);
        }
        mTitleBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // init by type
        mImageViewHeader = (ImageView) findViewById(R.id.header);
        mBtnChangeHeader = (Button) findViewById(R.id.change_header);
//        mBtnChangeHeader.setFocusable(true);
//        mBtnChangeHeader.requestFocus();
        mNickName = (EditText) findViewById(R.id.et_nick_name);
        // close soft input


    }

    @Override
    protected void onResume() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mNickName.getWindowToken(), 0); // 强制隐藏键盘
        super.onResume();
    }
}
