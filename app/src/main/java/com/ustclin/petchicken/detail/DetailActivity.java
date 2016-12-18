package com.ustclin.petchicken.detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.ustclin.petchicken.utils.Constant;
import com.ustclin.petchicken.utils.PhotoUtil;
import com.ustclin.petchicken.utils.SharedPreferencesUtils;
import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * 详情页面
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2016/11/20:02:13
 * description
 */
public class DetailActivity extends Activity implements View.OnClickListener {
    private static final String TAG = DetailActivity.class.getName();
    private int mType = Constant.TYPE.PET.ordinal(); // 0: pet , 1: master; default is 0
    private Context mContext;
    private SharedPreferences detailSP;
    // title 左上角按键
    private ImageView mTitleBarBtn;
    // title name
    private TextView mTitleName;

    // header
    private ImageView mImageViewHeader;
    private Button mBtnChangeHeader;
    // nickName
    private EditText mNickName;
    // sex
    private EditText mPetSex;
    private RadioGroup mMasterSex;
    private RadioButton mMasterMale;
    private RadioButton mMasterFemale;

    // age
    private EditText mAge;
    // voicer
    private TextView mVoicer;
    // play
    private RadioGroup mPetVoiceType;
    private RadioButton mPetAuto;
    private RadioButton mPetManual;
    private RadioButton mMasterVoiceType;

    // change voicer
    private Button mChangeVoicer;
    private String[] mCloudVoicersEntries;
    private String[] mCloudVoicersValue;
    private int selectedNum = 0;
    // 默认发音人
    private String voicer = "xiaowanzi";
    // save
    private Button mSave;

    // header function
    private PopupWindow mSetPhotoPop;
    private LinearLayout mMainView;
    private static final int PHOTO_PICKED_WITH_DATA = 1881;
    private static final int CAMERA_WITH_DATA = 1882;
    private static final int CAMERA_CROP_RESULT = 1883;
    private static final int PHOTO_CROP_RESULT = 1884;
    private static final int ICON_SIZE = 96;
    private Bitmap imageBitmap;
    private String mHeaderPath;
    private File mCurrentPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.details);
        StatusBarUtils.setMainChatActivityStatusBarColor(this);
        mType = getIntent().getIntExtra("type", 0);
        initSharedPreference();
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mHeaderPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdcardRootPath = Environment.getExternalStorageDirectory();
            String packageName = getPackageName();
            String headerRootPath = sdcardRootPath.getPath() + File.separator + packageName;
            if (mType == Constant.TYPE.PET.ordinal()) {
                mHeaderPath = headerRootPath + File.separator + "petHeader.jpg";
            } else {
                mHeaderPath = headerRootPath + File.separator + "masterHeader.jpg";
            }
            File headerRootPathFile = new File(headerRootPath);
            if (!headerRootPathFile.exists()) {
                if (headerRootPathFile.mkdirs()) {
                    Log.i(TAG, "header root dir created!");
                }
            }
        }
        // 云端发音人名称列表
        mCloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        mCloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);

    }

    /**
     * 初始化视图
     */
    private void initView() {
        mMainView = (LinearLayout) findViewById(R.id.detailLayout);
        // 公共view 初始化
        mTitleBarBtn = (ImageView) findViewById(R.id.title_bar_menu_btn);
        mTitleBarBtn.setImageResource(R.drawable.ic_actionbar_back_normal);
        mTitleBarBtn.setPadding(20, 10, 20, 10);
        mTitleBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleName = (TextView) findViewById(R.id.title_bar_name);

        mImageViewHeader = (ImageView) findViewById(R.id.header);
        if (mHeaderPath != null && new File(mHeaderPath).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(mHeaderPath);
            mImageViewHeader.setImageBitmap(bitmap);
        }

        mBtnChangeHeader = (Button) findViewById(R.id.change_header);
        mBtnChangeHeader.setOnClickListener(this);
        mNickName = (EditText) findViewById(R.id.et_nick_name);
        mNickName.setText(detailSP.getString("Name", getPackageName()));
        mPetSex = (EditText) findViewById(R.id.et_sex);
        mMasterSex = (RadioGroup) findViewById(R.id.rg_master_sex);
        mMasterMale = (RadioButton) findViewById(R.id.masterMale);
        mMasterFemale = (RadioButton) findViewById(R.id.masterFemale);
        mAge = (EditText) findViewById(R.id.et_age);
        mVoicer = (TextView) findViewById(R.id.tv_voicer_value);
        mPetVoiceType = (RadioGroup) findViewById(R.id.rg_pet_voice_type);
        mPetAuto = (RadioButton) findViewById(R.id.rb_pet_auto);
        mPetManual = (RadioButton) findViewById(R.id.rb_pet_manual);
        mMasterVoiceType = (RadioButton) findViewById(R.id.rg_master_voice_type);
        mChangeVoicer = (Button) findViewById(R.id.change_voicer);
        mChangeVoicer.setOnClickListener(this);
        mSave = (Button) findViewById(R.id.save_detail);
        mSave.setOnClickListener(this);

        if (mType == Constant.TYPE.PET.ordinal()) {
            mTitleName.setText(R.string.pet_name);
            mPetSex.setVisibility(View.VISIBLE);
            mMasterSex.setVisibility(View.GONE);
            mAge.setText(detailSP.getString("Age", "3"));
            mAge.setEnabled(false);
            mVoicer.setText(detailSP.getString("Voicer", "xiaowanzi"));
            selectedNum = getIndexOfArray(mVoicer.getText().toString());
            mPetVoiceType.setVisibility(View.VISIBLE);
            mMasterVoiceType.setVisibility(View.GONE);
            if (detailSP.getString("VoiceType", "auto").equals("auto")) {
                mPetAuto.setChecked(true);
            } else {
                mPetManual.setChecked(true);
            }
        } else {
            mTitleName.setText(R.string.master_name);
            mPetSex.setVisibility(View.GONE);
            mMasterSex.setVisibility(View.VISIBLE);
            if (detailSP.getString("Sex", "女").equals("女")) {
                mMasterFemale.setChecked(true);
            } else {
                mMasterMale.setChecked(true);
            }
            mAge.setText(detailSP.getString("Age", "18"));
            mAge.setEnabled(true);
            mVoicer.setText(detailSP.getString("Voicer", "xiaoxin"));
            selectedNum = getIndexOfArray(mVoicer.getText().toString());
            mPetVoiceType.setVisibility(View.GONE);
            mMasterVoiceType.setVisibility(View.VISIBLE);
        }
        // init voicer
        voicer = mVoicer.getText().toString();
    }

    private int getIndexOfArray(String voicer) {
        int index = 0;
        for (String tempVoicer : mCloudVoicersValue) {
            if (tempVoicer.equals(voicer)) {
                return index;
            }
            index ++;
        }
        return 0;
    }

    /**
     * 初始化sharedPreference
     */
    private void initSharedPreference() {
        SharedPreferences isFirstSP = this.getSharedPreferences(SharedPreferencesUtils.IS_FIRST_SETTING, Context.MODE_PRIVATE);
        if (mType == Constant.TYPE.PET.ordinal()) {
            detailSP = mContext.getSharedPreferences(SharedPreferencesUtils.PET_SETTING, Context.MODE_PRIVATE);
            if (!isFirstSP.contains(SharedPreferencesUtils.IS_FIRST_ENTER_PET_DETAIL)) {
                SharedPreferencesUtils.setFirstPetDetail(isFirstSP);
                SharedPreferencesUtils.setDefaultPetSharedPreferences(mContext, detailSP);
            }
        } else {
            detailSP = mContext.getSharedPreferences(SharedPreferencesUtils.MASTER_SETTING, Context.MODE_PRIVATE);
            if (!isFirstSP.contains(SharedPreferencesUtils.IS_FIRST_ENTER_MASTER_DETAIL)) {
                SharedPreferencesUtils.setFirstMasterDetail(isFirstSP);
                SharedPreferencesUtils.setDefaultMasterSharedPreferences(mContext, detailSP);
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_header:
                showPop();
                break;
            case R.id.change_voicer:
                if (mType == Constant.TYPE.PET.ordinal()) {
                    showPersonSelectDialog();
                } else {
                    showPersonSelectDialog();
                }
                break;
            // Save
            case R.id.save_detail:
                String name = mNickName.getText().toString();
                if (name.trim().length() == 0) {
                    Toast.makeText(this, "昵称不能为空哦！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mType == Constant.TYPE.PET.ordinal()) {
                    String voiceType;
                    if (mPetAuto.isChecked()) {
                        voiceType = "auto";
                    } else {
                        voiceType = "manual";
                    }
                    SharedPreferencesUtils.setCustomPetSettings(this, name, voicer, voiceType, detailSP);
                } else {
                    if (mAge.getText().toString().trim().length() == 0) {
                        Toast.makeText(this, "昵称不能为空哦！", Toast.LENGTH_SHORT).show();
                    }
                    String sex;
                    if (mMasterMale.isChecked()) {
                        sex = "男";
                    } else {
                        sex = "女";
                    }
                    SharedPreferencesUtils.setCustomMasterSettings(this, name, sex,
                            mAge.getText().toString(), voicer, detailSP);

                }
                Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 发音人选择。
     */
    private void showPersonSelectDialog() {
        // 选择在线合成
        new AlertDialog.Builder(this)
                .setTitle("在线合成发音人选项")
                .setSingleChoiceItems(mCloudVoicersEntries, // 单选框有几项,各是什么名字
                        selectedNum, // 默认的选项
                        new DialogInterface.OnClickListener() { // 点击单选框后的处理
                            public void onClick(DialogInterface dialog, int which) { // 点击了哪一项
                                voicer = mCloudVoicersValue[which];
                                selectedNum = which;
                                mVoicer.setText(voicer);
                                dialog.dismiss();
                            }
                        }).show();

    }

//--------------------------------------------header function----------------------------------

    /**
     * 弹出 popUpWindow
     */
    public void showPop() {
        View mainView = LayoutInflater.from(this).inflate(R.layout.alert_setphoto_menu_layout, null);
        Button btnTakePhoto = (Button) mainView.findViewById(R.id.btn_take_photo);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetPhotoPop.dismiss();
                // 拍照获取
                doTakePhoto();
            }
        });
        Button btnCheckFromGallery = (Button) mainView.findViewById(R.id.btn_check_from_gallery);
        btnCheckFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetPhotoPop.dismiss();
                // 相册获取
                doPickPhotoFromGallery();
            }
        });
        Button btnCancel = (Button) mainView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetPhotoPop.dismiss();
            }
        });
        mSetPhotoPop = new PopupWindow(this);
        mSetPhotoPop.setBackgroundDrawable(new BitmapDrawable());
        mSetPhotoPop.setFocusable(true);
        mSetPhotoPop.setTouchable(true);
        mSetPhotoPop.setOutsideTouchable(true);
        mSetPhotoPop.setContentView(mainView);
        mSetPhotoPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mSetPhotoPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mSetPhotoPop.setAnimationStyle(R.style.bottomStyle);
        mSetPhotoPop.showAtLocation(mMainView, Gravity.BOTTOM, 0, 0);
        mSetPhotoPop.update();
    }

    /**
     * 调用系统相机拍照
     */
    protected void doTakePhoto() {
        try {
            // Launch camera to take photo for selected contact
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Photo");
            if (!file.exists()) {
                file.mkdirs();
            }
            mCurrentPhotoFile = new File(file, PhotoUtil.getRandomFileName());
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.photoPickerNotFoundText, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Constructs an intent for capturing a photo and storing it in a temporary
     * file.
     */
    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 相机剪切图片
     */
    protected void doCropPhoto(File f) {
        try {
            // Add the image to the media store
            MediaScannerConnection.scanFile(this, new String[]{
                    f.getAbsolutePath()
            }, new String[]{
                    null
            }, null);

            // Launch gallery to crop the photo
            final Intent intent = getCropImageIntent(Uri.fromFile(f));
            startActivityForResult(intent, CAMERA_CROP_RESULT);
        } catch (Exception e) {
            Toast.makeText(this, R.string.photoPickerNotFoundText, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取系统剪裁图片的Intent.
     */
    public static Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 从相册选择图片
     */
    protected void doPickPhotoFromGallery() {
        try {
            // Launch picker to choose photo for selected contact
            final Intent intent = getPhotoPickIntent();
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.photoPickerNotFoundText, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取调用相册的Intent
     */
    public static Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /**
     * 相册裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CROP_RESULT);
    }

    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bm) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            System.out.println("sdcard mounted !");

            if (mHeaderPath != null) {
                File f = new File(mHeaderPath);
                if (f.exists()) {
                    if (f.delete()) {
                        Log.i(TAG, "header.jpg exists, delete the old one!");
                    }
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                    Log.w(TAG, "已经保存");
                } catch (IOException e) {
                    Log.e(TAG, "header image save failed! " + e.getMessage());
                }
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            switch (requestCode) {
                case PHOTO_PICKED_WITH_DATA:
                    // 相册选择图片后裁剪图片
                    startPhotoZoom(data.getData());
                    break;
                case PHOTO_CROP_RESULT:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        imageBitmap = extras.getParcelable("data");
                        //imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        mImageViewHeader.setImageBitmap(imageBitmap);
                        // save bitmap image
                        saveBitmap(imageBitmap);
                    }
                    break;
                case CAMERA_WITH_DATA:
                    // 相机拍照后裁剪图片
                    doCropPhoto(mCurrentPhotoFile);
                    break;
                case CAMERA_CROP_RESULT:
                    imageBitmap = data.getParcelableExtra("data");
                    // imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    mImageViewHeader.setImageBitmap(imageBitmap);
                    // save bitmap image
                    saveBitmap(imageBitmap);
                    break;
            }
        }
    }

}
