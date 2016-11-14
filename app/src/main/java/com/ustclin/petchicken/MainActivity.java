package com.ustclin.petchicken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.tuling.util.ResultWatcher;
import com.tuling.util.TulingManager;
import com.ustclin.petchicken.about.AboutActivity;
import com.ustclin.petchicken.bean.ChatMessage;
import com.ustclin.petchicken.db.ChatDAO;
import com.ustclin.petchicken.db.ImportDB;
import com.ustclin.petchicken.db.SQLiteDBHelper;
import com.ustclin.petchicken.functiondisplay.AbilitiesActivity;
import com.ustclin.petchicken.listview.OnRefreshListener;
import com.ustclin.petchicken.listview.RefreshListView;
import com.ustclin.petchicken.slidemenu.SlideMenu;
import com.ustclin.petchicken.utils.HttpUtils;
import com.ustclin.petchicken.utils.MyDateUtils;
import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;
import com.ustclin.startpage.MyPagerAdapter;
import com.ustclin.startpage.ViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.waps.AppConnect;
import cn.waps.AppListener;


public class MainActivity extends Activity implements OnClickListener,
        ResultWatcher {

    private RefreshListView mChatView;
    /**
     * 文本域
     */
    private EditText mMsg;
    /**
     * 存储聊天消息
     */
    private List<ChatMessage> mDatas = new ArrayList<ChatMessage>();
    /**
     * 适配器
     */
    private ChatMessageAdapter mAdapter;
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
    private SharedPreferences isFisrtSP;
    private SlideMenu slideMenu;
    private ImageView menuImg;
    private TextView mAbilities;
    private TextView mTextViewSupport;
    private TextView mTextViewContactAuthor;
    private TextView mTextViewAbout;
    // add voice
    private RelativeLayout relativeLayoutText;
    private RelativeLayout relativeLayoutVoice;
    private Button btnChangeInput;
    private Button btnPressToSpeek;
    private TulingManager manager;

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
                    for (int i = 0; i < mDatas.size(); i++) {
                        System.out.println(mDatas.get(i).getId() + ":"
                                + mDatas.get(i).getMsg());
                    }
                    mAdapter.notifyDataSetChanged();
                    // 焦点放在第一个item上
                    mChatView.setSelection(tmpList.size());
                    break;
                case SHOW_AD:
                    showAd();
                    break;
                case MESSAGE_RECOGNIZE:
                    Bundle result = (Bundle) msg.obj;
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
        isFisrtSP = this.getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        initDatabase(); // 初始化数据库
        // 第一次启动
        if (!isFisrtSP.contains("isFisrt")) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
            setFisrtSharedPreferences();
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
        ImportDB importDB = new ImportDB(this);
        importDB.copyDatabase();
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
        initView();
        // setListener();
        setAnimation();

        mAdapter = new ChatMessageAdapter(this, mDatas);
        mChatView.setAdapter(mAdapter);
        mChatView.setSelection(mDatas.size() - 1);
        // 给listView加载：下拉，异步刷新功能
        setListViewRefresh();
        // listView 滚动时，关闭软键盘
        setListViewScroll();
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

    private void setListViewScroll() {
        mChatView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mMsg.getWindowToken(), 0); // 强制隐藏键盘
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
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
        } else {
            // Toast.makeText(this, "已经到头啦！", Toast.LENGTH_SHORT).show();;
        }
        for (int i = 0; i < list20.size(); i++) {
            System.out.println(list20.get(i).getId() + ":"
                    + list20.get(i).getMsg());
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
        btnPressToSpeek = (Button) findViewById(R.id.btn_pts);
        // btnSend = (Button) findViewById(R.id.btn_send);
        relativeLayoutText.setVisibility(View.VISIBLE);
        relativeLayoutVoice.setVisibility(View.GONE);
        manager = new TulingManager(this);

        tvHint = (TextView) this.findViewById(R.id.tv_hint);
        mChatView = (RefreshListView) findViewById(R.id.refresh_listview);
        mMsg = (EditText) findViewById(R.id.id_chat_msg);
        slideMenu = (SlideMenu) findViewById(R.id.slide_menu);

        menuImg = (ImageView) findViewById(R.id.title_bar_menu_btn);
        menuImg.setOnClickListener(this);

        mAbilities = (TextView) findViewById(R.id.tv_abilities);
        mAbilities.setOnClickListener(this);

        mTextViewSupport = (TextView) findViewById(R.id.tv_support);
        mTextViewSupport.setOnClickListener(this);

        mTextViewContactAuthor = (TextView) findViewById(R.id.tv_contact_author);
        mTextViewContactAuthor.setOnClickListener(this);

        mTextViewAbout = (TextView) findViewById(R.id.tv_about);
        mTextViewAbout.setOnClickListener(this);

        mDatas.add(new ChatMessage(ChatMessage.MESSAGE_IN, MyDateUtils
                .getDate(), "我是小黄鸡，很高兴为主人服务"));
    }

    public void sendMessage(View view) {
        final String msg = mMsg.getText().toString();
        sendMessageString(msg);
    }

    public void sendMessageString(final String msg) {
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, "主人，你想说啥？", Toast.LENGTH_SHORT).show();
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
                    from = HttpUtils.sendMsg(msg);
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
            }

            ;
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

        System.out.println("the API_NUMBER is " + API_NUMBER);
        // 顺序递增查询没有用到的API_KEY ， 但是，更好的做法应该是随机数（去掉已经知道调用完的那些数）
        String usedApiIndex = sp.getString("usedApiIndex", "0");
        String strArr[] = usedApiIndex.split("_");
        // 此处会有异常，当API_NUMBER =3 ， 且usedApiIndex = “0_2_3_1”，然后，for循环出错，数组越界。
        // 正好会抛出异常，因为只有当最后一个key用完的时候，才会进来该函数，出现越界情况，抛出异常被上层捕捉到
        // 正好输出：“哎，今天生病了，先睡了，明天和你聊吧，拜拜”
        int arr[] = new int[API_NUMBER];
        for (int i = 0; i < strArr.length; i++) {
            System.out.println("arr = " + strArr[i]);
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
                editor2.commit();
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
        editor1.commit();
        HttpUtils.API_KEY = apiKey;
        System.out.println("api has changed into " + apiKey);

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

        mPageViews = new ArrayList<View>();
        mFramePageViews = new ArrayList<View>();

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
        // mPager.setOnPageChangeListener();

    }

    public void initFramePagerView(View frameView, int drawable) {
        ImageView image = (ImageView) frameView.findViewById(R.id.image);
        image.setImageResource(drawable);

    }

    public void initPagerView(View view, String text) {
        TextView textView1 = (TextView) view.findViewById(R.id.text);
        textView1.setText(text);
    }

    // 用户第一次使用
    private void setFisrtSharedPreferences() {
        // 创建SharedPreferences文件
        sp = this.getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        if (!sp.contains("isFisrt")) {
            // 用户第一次启动该运用
            Editor editor = sp.edit();
            editor.putBoolean("isFisrt", false);
            editor.commit();
        }
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
            System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
            editor.putString("lastExit", df.format(new Date()).toString());
            editor.putString("API_KEY", HttpUtils.API_KEY);
            editor.putString("usedApiIndex", "0");
            editor.commit();

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
            System.out.println("同一天启动，API_NUMBER:" + API_NUMBER);
        }
        // 隔天启动 ， 则删除sp中的所有字段
        else {
            Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            editor.putString("lastExit", df.format(new Date()).toString());
            editor.putString("API_KEY", HttpUtils.API_KEY);
            editor.putString("usedApiIndex", "0");
            editor.commit();
        }
    }

    @SuppressLint("NewApi")
    public void changeInput(View v) {

        if (relativeLayoutText.getVisibility() == View.VISIBLE) {
            btnChangeInput.setBackground(this.getResources().getDrawable(
                    R.drawable.text3));
            // close soft input
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mMsg.getWindowToken(), 0); // 强制隐藏键盘
            relativeLayoutText.setVisibility(View.GONE);
            relativeLayoutVoice.setVisibility(View.VISIBLE);
            return;
        } else {
            btnChangeInput.setBackground(this.getResources().getDrawable(
                    R.drawable.voice3));
            // open soft input
            mMsg.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mMsg, InputMethodManager.SHOW_FORCED);
            relativeLayoutText.setVisibility(View.VISIBLE);
            relativeLayoutVoice.setVisibility(View.GONE);
            return;
        }
    }

    public void pressToSpeek(View v) {
        manager.showRecognizeDialog(new DialogRecognitionListener() {
            @Override
            public void onResults(Bundle result) {
                mHandler.obtainMessage(MESSAGE_RECOGNIZE, result).sendToTarget();
            }
        });
    }

    // 万普配置信息，设置“yes”后，接受广告
    private void showAd() {
        String showad = AppConnect.getInstance(this).getConfig("showAd", "no");
        Log.e("fffffff", showad);
        if (showad.equals("yes")) {
            isHaveAD = true;
            // 万普广告
            AppConnect.getInstance(this);
            // 预加载插屏广告内容（仅在使用到插屏广告的情况，才需要添加）
            AppConnect.getInstance(this).initPopAd(this);
            //
            AppConnect.getInstance(this).setPopAdNoDataListener(
                    new AppListener() {
                        @Override
                        public void onPopNoData() {
                            Log.i("debug", "插屏广告暂无可用数据");
                        }
                    });
            // 显示插屏广告
            AppConnect.getInstance(this).showPopAd(this);
        }
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
            if (isStartPage == true) {
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
            if (isStartPage == true) {
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
                intetEmail.putExtra(Intent.EXTRA_SUBJECT, "关于“小黄鸡”应用的建议");
                intetEmail
                        .putExtra(Intent.EXTRA_TEXT, "作者你好，以下是我关于“小黄鸡”应用的一些建议：\n");
                startActivity(intetEmail);
                break;
            case R.id.tv_about:
                Intent intentAbout = new Intent();
                intentAbout.setClass(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
        }
    }

    @Override
    public void onResults(String arg0) {
        // TODO Auto-generated method stub

    }

}
