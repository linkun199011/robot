package com.ustclin.petchicken.delete;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.ustclin.petchicken.MainActivity;
import com.ustclin.petchicken.bean.ChatMessage;
import com.ustclin.petchicken.db.ChatDAO;
import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;

import java.util.Map;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2017/1/8:12:00
 * description
 */
public class DeleteActivity extends Activity implements View.OnClickListener{
    private ImageView mBack;
    private Button mBtnDelelte;
    private Button mSelectAll;
    private ListView mChatListView;
    private DeleteAdapter mDeleteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);
        StatusBarUtils.setDeleteActivityStatusBarColor(this);
        initView();
        initData();

    }

    // 初始化视图
    private void initView() {
        mBack = (ImageView) findViewById(R.id.del_back);
        mBtnDelelte = (Button) findViewById(R.id.btnDelete);
        mSelectAll = (Button) findViewById(R.id.btnSelectAll);
        mChatListView = (ListView) findViewById(R.id.lvListView);
    }

    // 初始化控件
    private void initData() {
        mBack.setOnClickListener(this);
        mBtnDelelte.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mDeleteAdapter = new DeleteAdapter(this);
        mChatListView.setAdapter(mDeleteAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.del_back:
                finish();
                break;
            case R.id.btnDelete:
                Map<Integer, Boolean> map = mDeleteAdapter.getCheckMap();
                int count = mDeleteAdapter.getCount();
                ChatDAO chatDao = new ChatDAO(this);
                for (int i = count - 1; i >= 0; i--) {
                    if (map.get(i) != null && map.get(i)) {
                        ChatMessage bean = (ChatMessage) mDeleteAdapter.getItem(i);
                        mDeleteAdapter.remove(i);
                        chatDao.delete(bean);
                    }
                }
                // set to default
                mDeleteAdapter.configCheckMap(false);
                mDeleteAdapter.notifyDataSetChanged();
                MainActivity.shouldListViewUpdate = true;
                break;
            case R.id.btnSelectAll:
                if (mSelectAll.getText().toString().trim().equals("全选")) {
                    //
                    mDeleteAdapter.configCheckMap(true);
                    mDeleteAdapter.notifyDataSetChanged();
                    mSelectAll.setText("全不选");
                } else {
                    mDeleteAdapter.configCheckMap(false);
                    mDeleteAdapter.notifyDataSetChanged();
                    mSelectAll.setText("全选");
                }
                break;
            default:
                break;
        }
    }
}
