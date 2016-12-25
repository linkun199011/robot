package com.ustclin.petchicken;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ustclin.petchicken.bean.ChatMessage;
import com.ustclin.petchicken.customview.RectangleView;
import com.ustclin.petchicken.detail.DetailActivity;
import com.ustclin.petchicken.utils.Constant;
import com.ustclin.petchicken.utils.MyDateUtils;
import com.ustclin.petchicken.utils.PhotoUtil;
import com.ustclin.petchicken.voice.VoiceSpeakUtils;
import com.ustclin.robot.R;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<ChatMessage> mDatas;
	/**
	 * 所有的Type
	 */
	private final static int TYPE_COUNT = 4;
	/**
	 * in  : with date
	 */
	private final static int TYPE_1 = 0; //
	/**
	 * in  : without date
	 */
	private final static int TYPE_2 = 1; //
	/**
	 * out : with date
	 */
	private final static int TYPE_3 = 2; //
	/**
	 * out : without date 
	 */
	private final static int TYPE_4 = 3; //

	private Context mContext;

    VoiceSpeakUtils voicePet ;
    VoiceSpeakUtils voiceMaster ;

	public ChatMessageAdapter(Context context, List<ChatMessage> datas)
	{
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
	}

    public void swapDatas(List<ChatMessage> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void setPetVoice(VoiceSpeakUtils voicePet) {
        this.voicePet = voicePet;
    }

    public void setMasterVoice(VoiceSpeakUtils voiceMaster) {
        this.voiceMaster = voiceMaster;
    }

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/**
	 * 接受到消息为1，发送消息为0
	 */
	@Override
	public int getItemViewType(int position)
	{
		ChatMessage msg_current = mDatas.get(position);
		ChatMessage msg_before ;
		if(position > 0){
			msg_before = mDatas.get(position-1);
		}
		else{
			msg_before = mDatas.get(position);
		}
		
		if(position == 0 && msg_current.getType().equals(ChatMessage.MESSAGE_IN))
		{
			return TYPE_1;
		}
		else if(position == 0 && msg_current.getType().equals(ChatMessage.MESSAGE_OUT)){
			return TYPE_3;
		}
		int duration = 5*60; // 5分钟
		if (MyDateUtils.isLarger(msg_current.getDate(), msg_before.getDate(),duration)
				&& msg_current.getType().equals(ChatMessage.MESSAGE_IN)) {
			return TYPE_1;
		}
		else if (!MyDateUtils.isLarger(msg_current.getDate(),msg_before.getDate(), duration)
				&& msg_current.getType().equals(ChatMessage.MESSAGE_IN)) {
			return TYPE_2;
		}
		if (MyDateUtils.isLarger(msg_current.getDate(), msg_before.getDate(),duration)
				&& msg_current.getType().equals(ChatMessage.MESSAGE_OUT)) {
			return TYPE_3;
		}
		else if (!MyDateUtils.isLarger(msg_current.getDate(),msg_before.getDate(), duration)
				&& msg_current.getType().equals(ChatMessage.MESSAGE_OUT)) {
			return TYPE_4;
		}
		// 上面的例子已经都cover到，这个return是默认的
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		ChatMessage chatMessage = mDatas.get(position);
		int type = getItemViewType(position);
		ViewHolder viewHolder = null;
		if (convertView == null){
			viewHolder = new ViewHolder();
			// 按照所需样式，确定布局
			switch (type) {
			case TYPE_1:
				// in : with date
				convertView = mInflater.inflate(R.layout.main_chat_from_msg,
						parent, false);
//				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				viewHolder.type = Type.PET;
				viewHolder.createDate = (TextView) convertView
						.findViewById(R.id.chat_from_createDate);
				viewHolder.createDate.setText(chatMessage.getDate());
				viewHolder.content = (Button) convertView
						.findViewById(R.id.chat_from_content);
				setOnClickListener(viewHolder);
				viewHolder.icon = (RectangleView) convertView.findViewById(R.id.chat_from_icon);
				viewHolder.icon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("type", Constant.TYPE.PET.ordinal());
						intent.setClass(mContext, DetailActivity.class);
						mContext.startActivity(intent);
					}
				});
				// custom header
                PhotoUtil.setPetRecHeader(mContext, viewHolder.icon);
				convertView.setTag(viewHolder);
				break;
			case TYPE_2:
				// in : without date
				convertView = mInflater.inflate(R.layout.main_chat_from_msg,
						parent, false);
				viewHolder.type = Type.PET;
				viewHolder.content = (Button) convertView
						.findViewById(R.id.chat_from_content);
				setOnClickListener(viewHolder);
				viewHolder.icon = (RectangleView) convertView.findViewById(R.id.chat_from_icon);
				viewHolder.icon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("type", Constant.TYPE.PET.ordinal());
						intent.setClass(mContext, DetailActivity.class);
						mContext.startActivity(intent);
					}
				});
				// custom header
                PhotoUtil.setPetRecHeader(mContext, viewHolder.icon);
				convertView.setTag(viewHolder);
				break;
			case TYPE_3:
				// out : with date
				convertView = mInflater.inflate(R.layout.main_chat_send_msg,
						null);
				viewHolder.type = Type.MASTER;
				viewHolder.createDate = (TextView) convertView
						.findViewById(R.id.chat_send_createDate);
				viewHolder.createDate.setText(chatMessage.getDate());
				viewHolder.content = (Button) convertView
						.findViewById(R.id.chat_send_content);
				setOnClickListener(viewHolder);
				viewHolder.icon = (RectangleView) convertView.findViewById(R.id.chat_send_icon);
				viewHolder.icon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("type", Constant.TYPE.MASTER.ordinal());
						intent.setClass(mContext, DetailActivity.class);
						mContext.startActivity(intent);
					}
				});
				// custom header
                PhotoUtil.setMasterRecHeader(mContext, viewHolder.icon);
				convertView.setTag(viewHolder);
				break;
			case TYPE_4:
				// out : without date
				convertView = mInflater.inflate(R.layout.main_chat_send_msg,
						null);
				viewHolder.type = Type.MASTER;
				viewHolder.content = (Button) convertView
						.findViewById(R.id.chat_send_content);
				setOnClickListener(viewHolder);
				viewHolder.icon = (RectangleView) convertView.findViewById(R.id.chat_send_icon);
				viewHolder.icon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("type", Constant.TYPE.MASTER.ordinal());
						intent.setClass(mContext, DetailActivity.class);
						mContext.startActivity(intent);
					}
				});
				// custom header
                PhotoUtil.setMasterRecHeader(mContext, viewHolder.icon);
				convertView.setTag(viewHolder);
				break;
			}
		} 
		else{
			viewHolder = (ViewHolder) convertView.getTag();
			
			switch(type){
			case TYPE_1:
				viewHolder.createDate.setText(chatMessage.getDate());
				viewHolder.createDate.setVisibility(View.VISIBLE);
				break;
			case TYPE_2:
				break;
			case TYPE_3:
				viewHolder.createDate.setText(chatMessage.getDate());
				viewHolder.createDate.setVisibility(View.VISIBLE);
				break;
			case TYPE_4:
				break;
			}
			
		}
		
		viewHolder.content.setText(chatMessage.getMsg());
		//如果位置是第一个的话，显示时间
		if(position == 0){
			viewHolder.createDate.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private void setOnClickListener(final ViewHolder viewHolder) {
		viewHolder.content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (viewHolder.type == Type.PET) {
                    Log.i("ChatMessageAdapter", "type = " + Type.PET.toString());
                    voicePet.play(viewHolder.content.getText().toString());
				} else {
                    Log.i("ChatMessageAdapter", "type = " + Type.MASTER.toString());
                    voiceMaster.play(viewHolder.content.getText().toString());
                }
			}
		});
	}

	private class ViewHolder
	{
		Type type = Type.PET;
		public TextView createDate;
		public TextView name;
		public Button content;
		public RectangleView icon;
	}

	public enum Type {
		PET,
		MASTER
	}

}
