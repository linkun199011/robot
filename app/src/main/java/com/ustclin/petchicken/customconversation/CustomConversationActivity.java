package com.ustclin.petchicken.customconversation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ustclin.petchicken.MainActivity;
import com.ustclin.petchicken.bean.CustomConverBean;
import com.ustclin.petchicken.db.CustomConverDAO;
import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;

import java.util.Map;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2017/1/9:21:23
 * description
 */
public class CustomConversationActivity extends Activity implements View.OnClickListener{
    private ImageView mBack;
    private Button mBtnDelete;
    private Button mSelectAll;
    private RelativeLayout mRLAdd;
    private ListView mConversationListView;
    private CustomConversationAdapter mConversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_conversation);
        StatusBarUtils.setDeleteActivityStatusBarColor(this);
        initView();
        initData();
    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.del_back);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mSelectAll = (Button) findViewById(R.id.btnSelectAll);
        mRLAdd = (RelativeLayout) findViewById(R.id.btnAdd);
        mConversationListView = (ListView) findViewById(R.id.lvListView);
    }

    private void initData() {
        mBack.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mRLAdd.setOnClickListener(this);
        mConversationAdapter = new CustomConversationAdapter(this);
        mConversationListView.setAdapter(mConversationAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.del_back:
                finish();
                break;
            case R.id.btnDelete:
                Map<Integer, Boolean> map = mConversationAdapter.getCheckMap();
                int count = mConversationAdapter.getCount();
                CustomConverDAO customConverDAO = new CustomConverDAO(this);
                for (int i = count - 1; i >= 0; i--) {
                    if (map.get(i) != null && map.get(i)) {
                        CustomConverBean bean = (CustomConverBean) mConversationAdapter.getItem(i);
                        mConversationAdapter.remove(i);
                        customConverDAO.delete(bean);
                    }
                }
                // set to default
                mConversationAdapter.configCheckMap(false);
                mConversationAdapter.notifyDataSetChanged();
                break;
            case R.id.btnSelectAll:
                if (mSelectAll.getText().toString().trim().equals("全选")) {
                    //
                    mConversationAdapter.configCheckMap(true);
                    mConversationAdapter.notifyDataSetChanged();
                    mSelectAll.setText("全不选");
                } else {
                    mConversationAdapter.configCheckMap(false);
                    mConversationAdapter.notifyDataSetChanged();
                    mSelectAll.setText("全选");
                }
                break;
            case R.id.btnAdd:
                // pop up dialog
                break;
            default:
                break;
        }
    }
}
