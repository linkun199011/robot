package com.ustclin.petchicken.customconversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ustclin.petchicken.MainActivity;
import com.ustclin.petchicken.bean.CustomConverBean;
import com.ustclin.petchicken.db.CustomConverDAO;
import com.ustclin.petchicken.delete.DeleteAdapter;
import com.ustclin.robot.R;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2017/1/9:22:08
 * description
 */
public class CustomConversationAdapter extends BaseAdapter{
    private Context mContext;
    private List<CustomConverBean> customConverBeans;
    private Map<Integer, Boolean> isCheckMap = new HashMap<>();

    public CustomConversationAdapter(Context context) {
        mContext = context;
        // need to init
        customConverBeans = getAllConverHistory();
        // init map
        configCheckMap(false);
    }

    public Map<Integer, Boolean> getCheckMap() {
        return isCheckMap;
    }

    public void remove(int position) {
        this.customConverBeans.remove(position);
    }

    private List<CustomConverBean> getAllConverHistory() {
        CustomConverDAO customConverDAO = new CustomConverDAO(mContext);
        try {
            return customConverDAO.findAll();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 默认情况下， 所有item都没有选中
    public void configCheckMap(boolean b) {
        for (int i = 0; i < customConverBeans.size(); i++) {
            isCheckMap.put(i, b);
        }

    }

    @Override
    public int getCount() {
        return customConverBeans == null ? 0 : customConverBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return customConverBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewGroup layout = null;
        if (convertView == null) {
            layout = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.custom_conver_item_layout, parent, false);
        } else {
            layout = (ViewGroup) convertView;
        }
        CustomConverBean bean = customConverBeans.get(position);

        TextView masterContent = (TextView) layout.findViewById(R.id.masterAsk);
        TextView petContent = (TextView) layout.findViewById(R.id.petAnswer);

        CheckBox cbCheck = (CheckBox) layout.findViewById(R.id.cbCheckBox);
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckMap.put(position, isChecked);
            }
        });

        cbCheck.setVisibility(View.VISIBLE);
        cbCheck.setChecked(isCheckMap.get(position));

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.petContent = petContent;
        viewHolder.masterContent = masterContent;
        viewHolder.cbCheckBox = cbCheck;

        layout.setTag(viewHolder);
        return layout;
    }

    public static class ViewHolder {
        public TextView petContent = null;
        public TextView masterContent = null;
        public CheckBox cbCheckBox = null;
    }
}
