package com.ustclin.petchicken.delete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ustclin.petchicken.bean.ChatMessage;
import com.ustclin.petchicken.db.ChatDAO;
import com.ustclin.robot.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2017/1/8:12:32
 * description
 */
public class DeleteAdapter extends BaseAdapter{
    private Context mContext;
    private List<ChatMessage> messages;
    private Map<Integer, Boolean> isCheckMap = new HashMap<>();

    public DeleteAdapter(Context context) {
        mContext = context;
        messages = getAllChatHistory();
        // init map
        configCheckMap(false);
    }

    // 默认情况下，所有item都没有选中
    public void configCheckMap(boolean b) {
        for (int i = 0; i < messages.size(); i++) {
            isCheckMap.put(i, b);
        }
    }

    private List<ChatMessage> getAllChatHistory() {
        ChatDAO chatDAO = new ChatDAO(mContext);
        return chatDAO.findAll();
    }

    @Override
    public int getCount() {
        return messages == null ? 0 : messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewGroup layout = null ;
        if (convertView == null) {
            layout = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.delete_item_layout, parent, false);
        } else {
            layout = (ViewGroup) convertView;
        }
        ChatMessage bean = messages.get(position);

        TextView tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
        String chater;
        if (bean.getType() != null && bean.getType().equals("in")) {
            chater = mContext.getResources().getString(R.string.pet_name);
        } else {
            chater = mContext.getResources().getString(R.string.master_name);
        }
        String content = chater + ": " + bean.getMsg();
        tvTitle.setText(content);

        CheckBox cbCheck = (CheckBox) layout.findViewById(R.id.cbCheckBox);
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckMap.put(position, isChecked);
            }
        });

        cbCheck.setVisibility(View.VISIBLE);
        cbCheck.setChecked(isCheckMap.get(position));
        ViewHolder holder = new ViewHolder();
        holder.cbCheckBox = cbCheck;
        holder.tvTitle = tvTitle;
        layout.setTag(holder);

        return layout;
    }

    public Map<Integer, Boolean> getCheckMap() {
        return this.isCheckMap;
    }

    public void remove(int position) {
        this.messages.remove(position);
    }

    public static class ViewHolder {
        public TextView tvTitle = null;
        public CheckBox cbCheckBox = null;
    }
}
