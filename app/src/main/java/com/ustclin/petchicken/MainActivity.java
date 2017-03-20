package com.ustclin.petchicken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ustclin.petchicken.about.AboutActivity;
import com.ustclin.petchicken.bean.ChatMessage;
import com.ustclin.petchicken.customconversation.CustomConversationActivity;
import com.ustclin.petchicken.db.ChatDAO;
import com.ustclin.petchicken.db.CustomConverDAO;
import com.ustclin.petchicken.db.SQLiteDBHelper;
import com.ustclin.petchicken.delete.DeleteActivity;
import com.ustclin.petchicken.detail.DetailActivity;
import com.ustclin.petchicken.functiondisplay.AbilitiesActivity;
import com.ustclin.petchicken.listview.OnRefreshListener;
import com.ustclin.petchicken.listview.RefreshListView;
import com.ustclin.petchicken.slidemenu.SlideMenu;
import com.ustclin.petchicken.utils.AdUtils;
import com.ustclin.petchicken.utils.Constant;
import com.ustclin.petchicken.utils.HttpUtils;
import com.ustclin.petchicken.utils.MyDateUtils;
import com.ustclin.petchicken.utils.SharedPreferencesUtils;
import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.petchicken.voice.JsonParser;
import com.ustclin.petchicken.voice.VoiceListenUtils;
import com.ustclin.petchicken.voice.VoiceSpeakUtils;
import com.ustclin.robot.R;
import com.ustclin.startpage.MyPagerAdapter;
import com.ustclin.startpage.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import cn.waps.AppConnect;


public class MainActivity extends Activity implements OnClickListener {

    private Context mContext;
    private RefreshListView mChatView;
    /**
     * 适配器
     */
    private ChatMessageAdapter mAdapter;
    /**
     * 文本域
     */
    private EditText mMsg;
    /**
     * 存储聊天消息
     */
    private List<ChatMessage> mDatas = new ArrayList<ChatMessage>();

    private TextView tvHint;
    private ChatDAO mChatDAO = null;
    //
    @SuppressWarnings("unused")
    private static boolean isHaveAD = false;
    private final static int SHOW_AD = 100;
    private final static int SINGLE_MESSAGE = 0;
    private final static int LIST_MESSAGE = 1;
    private static int API_NUMBER = 2;
    private static final int MESSAGE_RECOGNIZE = 3;
    private static final int MESSAGE_GET_RESULT = 4;
    //
    private SharedPreferences sp;
    private SharedPreferences isFirstSP;
    long exitTime = 0;
    static int indexNumber = 0;
    private boolean isStartPage = false; // 启动引导 页
    // private ComposerLayout clayout;
    private ViewPager mPager;
    private ArrayList<View> mPageViews;
    private MyPagerAdapter mPageAdapter;
    private ViewPager mFramePager;
    private ArrayList<View> mFramePageViews;
    private MyPagerAdapter mFramePageAdapter;
    private SlideMenu slideMenu;
    private ImageView menuImg;
    private TextView mAbilities;
    private TextView mTextViewSupport;
    private TextView mTextViewContactAuthor;
    private TextView mTextViewAbout;
    private TextView mTextViewPetSetting;
    private TextView mTextViewMasterSetting;
    private TextView mTextViewDeleteHis;
    private TextView mTextViewShare;
    private TextView mTextViewShare2;
    private TextView mTextViewCustomConver;
    // header
    private TextView mTextViewToolBarHeader;
    private String greetings; // 问候语

    private Button mSend;
    // add voice
    private RelativeLayout relativeLayoutText;
    private RelativeLayout relativeLayoutVoice;
    private Button btnChangeInput;
    // XUN FEI voice
    private static String XUN_FEI = "XunFei";
    VoiceSpeakUtils voicePet ;
    VoiceSpeakUtils voiceMaster ;

    // soft input
    InputMethodManager imm;
    public static boolean isSoftInputActive = false;

    // ListView content should update
    public static boolean shouldListViewUpdate = false;

    // 记录手势动作
    float mPosX = 0, mPosY = 0, mCurPosX = 0, mCurPosY = 0;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SINGLE_MESSAGE:
                    ChatMessage from = (ChatMessage) msg.obj;
                    mDatas.add(from);
                    mAdapter.notifyDataSetChanged();
                    mChatView.setSelection(mDatas.size() - 1);
                    break;
                case LIST_MESSAGE:
                    List<ChatMessage> tmpList = (List<ChatMessage>) msg.obj;
                    mDatas.addAll(0, tmpList);
                    mAdapter.notifyDataSetChanged();
                    // 焦点放在第一个item上
                    mChatView.setSelection(tmpList.size());
                    break;
                case SHOW_AD:
                    isHaveAD = AdUtils.showAd(mContext);
                    break;
                case MESSAGE_RECOGNIZE:
                    Bundle result = (Bundle) msg.obj;
                    if (result == null) {
                        return;
                    }
                    String type = result.getString("type");
                    if (type != null && type.equals(XUN_FEI)) {
                        String content = result.getString("result");
                        sendMessageString(content);
                        return;
                    }
                    if (result != null) {
                        ArrayList<String> list = result
                                .getStringArrayList("results_recognition");
                        String tempStr = list.get(0);
                        sendMessageString(tempStr);
                    }
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题栏
        mContext = this;
        isFirstSP = this.getSharedPreferences(SharedPreferencesUtils.IS_FIRST_SETTING, Context.MODE_PRIVATE);
        initDatabase(); // 初始化数据库
        initLib();
        // 第一次启动
        if (!isFirstSP.contains(SharedPreferencesUtils.IS_FIRST_START_APP)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
            SharedPreferencesUtils.setFirstSharedPreferences(isFirstSP);
            setContentView(R.layout.activity_viewpager_slash);
            initViewPager();
        } else {
            mainPage();
        }
    }



    /**
     * 初始化数据库
     */
    private void initDatabase() {
        // 初始化 database
        SQLiteDBHelper helper = new SQLiteDBHelper(this);
        helper.getReadableDatabase();
//        ImportDB importDB = new ImportDB(this);
//        importDB.copyDatabase();
        mChatDAO = new ChatDAO(this);
        mDatas = mChatDAO.find20();
        // list 翻转
        Collections.reverse(mDatas);
    }

    private void mainPage() {
        // exit full screen
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.main_chatting);
        StatusBarUtils.setMainChatActivityStatusBarColor(this);
        setSharedPreferences();
        // 设置发音人
        initVoice();
        initView();
        // setListener();
        setAnimation();
        // 给listView加载：下拉，异步刷新功能
        setListViewRefresh();
//        setListViewScroll();
        // 广告线程
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    //
                    Message msg0 = new Message();
                    msg0.what = SHOW_AD;
                    mHandler.sendMessage(msg0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void   initVoice() {
        // should read SharedPre.
        voicePet = new VoiceSpeakUtils(mContext);
        SharedPreferences petSP = this.getSharedPreferences(SharedPreferencesUtils.PET_SETTING, Context.MODE_PRIVATE);
        if (petSP.contains("Voicer")) {
            voicePet.setVoicer(petSP.getString("Voicer","xiaowanzi"));
        } else {
            voicePet.setVoicer("xiaowanzi");
        }
        voiceMaster = new VoiceSpeakUtils(mContext);
        SharedPreferences masterSP = this.getSharedPreferences(SharedPreferencesUtils.MASTER_SETTING, Context.MODE_PRIVATE);
        if (masterSP.contains("Voicer")) {
            voiceMaster.setVoicer(masterSP.getString("Voicer", "xiaoyan"));
        } else {
            voiceMaster.setVoicer("xiaoyan");
        }
    }

    private void setListViewRefresh() {
        mChatView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 异步查询数据
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        SystemClock.sleep(700);
                        // 找到比第一个记录时间更久的聊天记录，一次刷新取20条
                        getMoreList();
                        return null;
                    }

                    protected void onPostExecute(Void result) {
                        // 隐藏头布局
                        mChatView.onRefreshFinish();
                    }
                }.execute(new Void[]{});
            }
        });
    }

    /**
     * 每次从数据库中取出20个记录
     */
    private void getMoreList() {
        List<ChatMessage> list20 = mChatDAO.find20(mDatas.get(0));
        if (list20.size() > 0) {
            // 翻转
            Collections.reverse(list20);
            Message message = Message.obtain();
            message.what = LIST_MESSAGE;
            message.obj = list20;
            mHandler.sendMessage(message);
        }
    }

    // 设置“上滑进入首页”文字的动画效果
    private void setAnimation() {
        Animation ani = new AlphaAnimation(0f, 1f);
        ani.setDuration(1500);
        ani.setRepeatMode(Animation.REVERSE);
        ani.setRepeatCount(Animation.INFINITE);
        tvHint.startAnimation(ani);
    }

    private void initView() {
        relativeLayoutText = (RelativeLayout) findViewById(R.id.ly_text);
        relativeLayoutVoice = (RelativeLayout) findViewById(R.id.ly_voice);
        btnChangeInput = (Button) findViewById(R.id.btn_changeInput);
        relativeLayoutText.setVisibility(View.VISIBLE);
        relativeLayoutVoice.setVisibility(View.GONE);

        tvHint = (TextView) this.findViewById(R.id.tv_hint);
        mChatView = (RefreshListView) findViewById(R.id.refresh_listview);
        // send button
        mSend = (Button) findViewById(R.id.id_chat_send);
        mSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = mMsg.getText().toString();
                sendMessageString(msg);
            }
        });
        mSend.setEnabled(false);

        mMsg = (EditText) findViewById(R.id.id_chat_msg);
        mMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 判断输入不为空，按钮可点击
                if (mMsg.length() != 0) {
                    mSend.setEnabled(true);
                } else {
                    mSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        slideMenu = (SlideMenu) findViewById(R.id.slide_menu);

        menuImg = (ImageView) findViewById(R.id.title_bar_menu_btn);
        menuImg.setImageResource(R.drawable.ic_top_bar_category);
        menuImg.setOnClickListener(this);

        mAbilities = (TextView) findViewById(R.id.tv_abilities);
        mAbilities.setOnClickListener(this);

        mTextViewSupport = (TextView) findViewById(R.id.tv_support);
        mTextViewSupport.setOnClickListener(this);

        mTextViewContactAuthor = (TextView) findViewById(R.id.tv_contact_author);
        mTextViewContactAuthor.setOnClickListener(this);

        mTextViewAbout = (TextView) findViewById(R.id.tv_about);
        mTextViewAbout.setOnClickListener(this);

        mTextViewPetSetting = (TextView) findViewById(R.id.tv_pet_setting);
        mTextViewPetSetting.setOnClickListener(this);

        mTextViewMasterSetting = (TextView) findViewById(R.id.tv_master_setting);
        mTextViewMasterSetting.setOnClickListener(this);

        mTextViewDeleteHis = (TextView) findViewById(R.id.tv_delete);
        mTextViewDeleteHis.setOnClickListener(this);

        mTextViewShare = (TextView) findViewById(R.id.tv_share);
        mTextViewShare.setOnClickListener(this);

        mTextViewShare2 = (TextView) findViewById(R.id.tv_share2);
        mTextViewShare2.setOnClickListener(this);

        mTextViewCustomConver = (TextView) findViewById(R.id.tv_custom);
        mTextViewCustomConver.setOnClickListener(this);

        // set header
        mTextViewToolBarHeader = (TextView) findViewById(R.id.title_bar_name);
        mTextViewToolBarHeader.setText(RobotApp.gPetName);

        resetGreetings();

        mDatas.add(new ChatMessage(ChatMessage.MESSAGE_IN, MyDateUtils
                .getDate(), greetings));

        mAdapter = new ChatMessageAdapter(this, mDatas);
        mAdapter.setPetVoice(voicePet);
        mAdapter.setMasterVoice(voiceMaster);
        mChatView.setAdapter(mAdapter);
        mChatView.setSelection(mDatas.size() - 1);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void sendMessageString(final String msg) {
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, RobotApp.gMasterName + "，你想说啥？", Toast.LENGTH_SHORT).show();
            return;
        }
        // 构造函数，创建对象
        ChatMessage to = new ChatMessage(ChatMessage.MESSAGE_OUT,
                MyDateUtils.getDate(), msg);
        // Add date
        mDatas.add(to);
        mAdapter.notifyDataSetChanged();
        mChatView.setSelection(mDatas.size() - 1);
        mMsg.setText("");
        // 加入数据库
        mChatDAO.add(to);

        new Thread() {
            public void run() {
                ChatMessage from = null;
                try {
                    // 用户发出一句话时，先从DB中查找，如果有，则直接回复！
                    CustomConverDAO customConverDAO = new CustomConverDAO(mContext);
                    from = customConverDAO.findMsg(msg);
                    if (from == null) {
                        from = HttpUtils.sendMsg(msg);
                    }
                    // 加入数据库
                    mChatDAO.add(from);
                } catch (Exception e) {
                    from = new ChatMessage(ChatMessage.MESSAGE_IN,
                            MyDateUtils.getDate(), "主人，我连不上服务器了，是不是你把网络禁用掉啦！！");
                }
                // 如果达到API上限，系统将会提示“API调用已经用完”
                if (from.getMsg().contains("API")
                        || from.getMsg().contains("api")) {
                    from.setMsg("我刚刚走神了，你说什么来着？？");
                    try {
                        Message message = Message.obtain();
                        message.what = SINGLE_MESSAGE;
                        message.obj = from;
                        mHandler.sendMessage(message);
                        changeAPI_KEY();
                        return;
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        from.setMsg("哎，今天生病了，先睡了，明天和你聊吧，拜拜");
                        Message message = Message.obtain();
                        message.what = SINGLE_MESSAGE;
                        message.obj = from;
                        mHandler.sendMessage(message);
                        return;
                    }
                }
                Message message = Message.obtain();
                message.obj = from;
                mHandler.sendMessage(message);
                voicePet.play(from.getMsg());
            }
        }.start();

    }
    /**
     * 由于一个账号已经达到API调用上限，所以及时更改API_KEY,通过第三方广告平台的配置项来更改，比较稳定
     */
    protected void changeAPI_KEY() {
        // 获取API个数
        String countStr = AppConnect.getInstance(this).getConfig("API_NUMBER",
                "1");
        API_NUMBER = Integer.parseInt(countStr);
        Editor editor = sp.edit();
        editor.putString("API_NUMBER", API_NUMBER + "");
        editor.commit();

        // 顺序递增查询没有用到的API_KEY ， 但是，更好的做法应该是随机数（去掉已经知道调用完的那些数）
        String usedApiIndex = sp.getString("usedApiIndex", "0");
        String strArr[] = usedApiIndex.split("_");
        // 此处会有异常，当API_NUMBER =3 ， 且usedApiIndex = “0_2_3_1”，然后，for循环出错，数组越界。
        // 正好会抛出异常，因为只有当最后一个key用完的时候，才会进来该函数，出现越界情况，抛出异常被上层捕捉到
        // 正好输出：“哎，今天生病了，先睡了，明天和你聊吧，拜拜”
        int arr[] = new int[API_NUMBER];
        for (int i = 0; i < strArr.length; i++) {
            arr[i] = Integer.parseInt(strArr[i]);
        }
        // 随机生成一个在 1~API_NUMBER 之间的数
        boolean isAvailable = false;
        // 计算共有多少
        Arrays.sort(arr); // 首先对数组排序
        int searchcount = 0;
        // 找3倍 的API_NUMBER次，如果再找不到，就默认为API全部用完
        while (!isAvailable && searchcount < API_NUMBER * 3) {
            Random rand = new Random();
            int randNum = rand.nextInt(API_NUMBER) + 1;
            int result = Arrays.binarySearch(arr, randNum); // 在数组中搜索是否含有randNum
            // 小于0 ， 未找到
            if (result < 0) {
                System.err.println("随机查到的数为：" + randNum);
                Editor editor2 = sp.edit();
                editor2.putString("usedApiIndex", usedApiIndex + "_" + randNum);
                editor2.apply();
                indexNumber = randNum;
                isAvailable = true;
            }
            searchcount++;
            // 未查到
            if (searchcount == API_NUMBER * 3 && isAvailable == false) {
                System.err.println("-_-");
            }
        }
        String apiKey = AppConnect.getInstance(this).getConfig(
                "xiao" + indexNumber, "a32e0e48a053066bf4831ebd5fb0b2eb");
        Editor editor1 = sp.edit();
        editor1.putString("API_KEY", apiKey);
        editor1.apply();
        HttpUtils.API_KEY = apiKey;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void initViewPager() {
        isStartPage = true;
        mPager = (ViewPager) findViewById(R.id.pager);
        mFramePager = (ViewPager) findViewById(R.id.main_scrolllayout);

        mPageViews = new ArrayList<>();
        mFramePageViews = new ArrayList<>();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View frameView1 = inflater.inflate(R.layout.transparent_layer_image,
                null);
        initFramePagerView(frameView1, R.drawable.frame_view1);
        View frameView2 = inflater.inflate(R.layout.transparent_layer_image,
                null);
        initFramePagerView(frameView2, R.drawable.frame_view2);
        View frameView3 = inflater.inflate(R.layout.transparent_layer_image,
                null);
        initFramePagerView(frameView3, R.drawable.frame_view3);
        View frameView4 = inflater.inflate(R.layout.transparent_layer_image,
                null);
        initFramePagerView(frameView4, R.drawable.frame_view4);
        View frameView5 = inflater.inflate(R.layout.transparent_layer_image,
                null);
        initFramePagerView(frameView5, R.drawable.frame_view5);
        mFramePageViews.add(frameView1);
        mFramePageViews.add(frameView2);
        mFramePageViews.add(frameView3);
        mFramePageViews.add(frameView4);
        mFramePageViews.add(frameView5);

        mFramePageAdapter = new MyPagerAdapter(mFramePageViews);
        mFramePager.setAdapter(mFramePageAdapter);
        // mFramePager.setOnPageChangeListener(mFramePagerListener);
        View view1 = inflater.inflate(R.layout.transparent_layer, null);
        initPagerView(view1, "你可以找我聊天");
        View view2 = inflater.inflate(R.layout.transparent_layer, null);
        initPagerView(view2, "我有时候可能脾气不太好");
        View view3 = inflater.inflate(R.layout.transparent_layer, null);
        initPagerView(view3, "但是我很聪明！");
        View view4 = inflater.inflate(R.layout.transparent_layer, null);
        initPagerView(view4, "我有各种技能");
        View view5 = inflater.inflate(R.layout.transparent_layer, null);
        TextView textView5 = (TextView) view5.findViewById(R.id.text);
        textView5.setVisibility(View.GONE);
        Button btn = (Button) view5.findViewById(R.id.button);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartPage = false;
                mainPage();
            }
        });

        mPageViews.add(view1);
        mPageViews.add(view2);
        mPageViews.add(view3);
        mPageViews.add(view4);
        mPageViews.add(view5);
        mPageAdapter = new MyPagerAdapter(mPageViews);
        mPager.setAdapter(mPageAdapter);
        mPager.setFollowViewPager(mFramePager);

    }

    public void initFramePagerView(View frameView, int drawable) {
        ImageView image = (ImageView) frameView.findViewById(R.id.image);
        image.setImageResource(drawable);

    }

    public void initPagerView(View view, String text) {
        TextView textView1 = (TextView) view.findViewById(R.id.text);
        textView1.setText(text);
    }


    // 设置Sp
    private void setSharedPreferences() {
        // 创建SharedPreferences文件
        sp = this.getSharedPreferences("SP", Context.MODE_PRIVATE);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        // 第一次启动程序
        if (!sp.contains("lastExit")) {
            // 不包括lastExit字段，用户第一次启动该运用
            Editor editor = sp.edit();
            editor.putString("lastExit", df.format(new Date()).toString());
            editor.putString("API_KEY", HttpUtils.API_KEY);
            editor.putString("usedApiIndex", "0");
            editor.apply();

        }
        // 同一天启动应用
        else if (sp.getString("lastExit", df.format(new Date()).toString())
                .equals(df.format(new Date()).toString())) {
            //
            String API_KEY = sp.getString("API_KEY",
                    "b93b2d9c05a91658145d2431f3f18168");
            HttpUtils.API_KEY = API_KEY;
            String apiNumber = sp.getString("API_NUMBER", "3");
            API_NUMBER = Integer.parseInt(apiNumber);
        }
        // 隔天启动 ， 则删除sp中的所有字段
        else {
            Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            editor.putString("lastExit", df.format(new Date()).toString());
            editor.putString("API_KEY", HttpUtils.API_KEY);
            editor.putString("usedApiIndex", "0");
            editor.apply();
        }
    }

    @SuppressLint("NewApi")
    public void changeInput(View v) {

        if (relativeLayoutText.getVisibility() == View.VISIBLE) {
            btnChangeInput.setBackground(this.getResources().getDrawable(
                    R.drawable.icon_keyborad));
            // close soft input
            activeSoftInput(false);
            relativeLayoutText.setVisibility(View.GONE);
            relativeLayoutVoice.setVisibility(View.VISIBLE);
            return;
        } else {
            btnChangeInput.setBackground(this.getResources().getDrawable(
                    R.drawable.icon_mic));
            // open soft input
            mMsg.requestFocus();
            activeSoftInput(true);
            relativeLayoutText.setVisibility(View.VISIBLE);
            relativeLayoutVoice.setVisibility(View.GONE);
            return;
        }
    }

    /**
     * 开启 、 关闭 软键盘
     * @param isActive true : 开启软件盘； false： 关闭软键盘
     */
    public void activeSoftInput(boolean isActive) {
        if (isActive) {
            // open
            if (imm != null) {
                imm.showSoftInput(mMsg, InputMethodManager.SHOW_FORCED);
                isSoftInputActive = true;
            }
        } else {
            // close
            if (imm != null) {
                imm.hideSoftInputFromWindow(mMsg.getWindowToken(), 0); // 强制隐藏键盘
                isSoftInputActive = false;
            }

        }
    }

    public void pressToSpeek(View v) {
        VoiceListenUtils voiceListenUtils = new VoiceListenUtils(mContext);
        voiceListenUtils.listen(mRecognizerDialogListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConnect.getInstance(this).close();
    }

    // 连续按两次后退键，退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isStartPage) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }
            // 关闭侧边栏
            if (!slideMenu.isMainScreenShowing()) {
                slideMenu.closeMenu();
                return true;
            }
            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
            {

                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (isStartPage) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }
            if (slideMenu.isMainScreenShowing()) {
                slideMenu.openMenu();
            } else {
                slideMenu.closeMenu();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_menu_btn:
                if (slideMenu.isMainScreenShowing()) {
                    slideMenu.openMenu();
                } else {
                    slideMenu.closeMenu();
                }
                activeSoftInput(false);
                break;
            case R.id.tv_abilities:
                Intent intentAbilities = new Intent();
                intentAbilities.setClass(this, AbilitiesActivity.class);
                startActivity(intentAbilities);
                break;
            case R.id.tv_support:
                Toast.makeText(MainActivity.this, "我来点个赞！", Toast.LENGTH_SHORT)
                        .show();
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intentSupport = new Intent(Intent.ACTION_VIEW, uri);
                intentSupport.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentSupport);
                break;
            case R.id.tv_contact_author:
                Intent intetEmail = new Intent(Intent.ACTION_SENDTO);
                intetEmail.setData(Uri.parse("mailto:linkun199011@163.com"));
                intetEmail.putExtra(Intent.EXTRA_SUBJECT, "关于“" + getString(R.string.app_name) + "”应用的建议");
                intetEmail
                        .putExtra(Intent.EXTRA_TEXT, "作者你好，以下是我关于“"+ getString(R.string.app_name) + "”应用的一些建议：\n");
                intetEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intetEmail);
                break;
            case R.id.tv_about:
                Intent intentAbout = new Intent();
                intentAbout.setClass(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.tv_pet_setting:
                Intent intentPet = new Intent();
                intentPet.putExtra("type", Constant.TYPE.PET.ordinal());
                intentPet.setClass(mContext, DetailActivity.class);
                mContext.startActivity(intentPet);
                break;
            case R.id.tv_master_setting:
                Intent intentMaster = new Intent();
                intentMaster.putExtra("type", Constant.TYPE.MASTER.ordinal());
                intentMaster.setClass(mContext, DetailActivity.class);
                mContext.startActivity(intentMaster);

                break;
            case R.id.tv_delete:
                Intent intentDelete = new Intent();
                intentDelete.setClass(mContext, DeleteActivity.class);
                mContext.startActivity(intentDelete);
                break;
            case R.id.tv_share:
                weChatShare(1);//分享到微信朋友圈
                break;
            case R.id.tv_share2:
                weChatShare(0);//分享到微信好友
                break;
            case R.id.tv_custom:
                Intent intentCustom = new Intent();
                intentCustom.setClass(mContext, CustomConversationActivity.class);
                mContext.startActivity(intentCustom);
                break;
            default:
                break;
        }
    }

    private void resetListView() {
        initVoice();
        mDatas.clear();
        mDatas = mChatDAO.find20();
        // list 翻转
        Collections.reverse(mDatas);
        mDatas.add(new ChatMessage(ChatMessage.MESSAGE_IN, MyDateUtils
                .getDate(), greetings));

        mAdapter = new ChatMessageAdapter(this, mDatas);
        mAdapter.setPetVoice(voicePet);
        mAdapter.setMasterVoice(voiceMaster);

        mChatView.setAdapter(mAdapter);
        mChatView.setSelection(mDatas.size() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldListViewUpdate) {
            resetGreetings();
            resetListView();
            resetHeader();
            shouldListViewUpdate = false;
        }
        if (Constant.isNeedToReStart) {
            Constant.isNeedToReStart = false;
            mAdapter.notifyDataSetChanged();
        }
    }

    // 问候语
    private void resetGreetings() {
        greetings = "我是" + RobotApp.gPetName + "，很高兴为" + RobotApp.gMasterName + "服务";
    }

    private void resetHeader() {
        mTextViewToolBarHeader.setText(RobotApp.gPetName);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                mCurPosY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurPosY - mPosY > 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向下滑動
                    activeSoftInput(false);

                } else if (mCurPosY - mPosY < 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向上滑动
                    // do nothing
                }

                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 听写 监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            collectMessage(results);
            if (isLast) {
                sendResult();
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Toast.makeText(mContext, error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
        }

    };

    HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private void collectMessage(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
    }

    private void sendResult() {
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        mIatResults.clear();
        Bundle bundle = new Bundle();
        bundle.putString("result", resultBuffer.toString());
        bundle.putString("type", XUN_FEI);
        mHandler.obtainMessage(MESSAGE_RECOGNIZE, bundle).sendToTarget();

    }

    //---------------------- 微信分享 初始化
    private IWXAPI wxApi;
    String WX_APP_ID = "wx0455a8eedb2a8159"; // our

    private void initLib() {
        //实例化
        wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        wxApi.registerApp(WX_APP_ID);
    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void weChatShare(int flag) {
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = "https://www.zybuluo.com/linkun199011/note/622446";

        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = "萌宠";
        msg.description = "我是一只可以说话的“萌宠”,可以陪主人唠嗑,讲笑话...\n更多功能等待主人的发现哦^_^";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

}
