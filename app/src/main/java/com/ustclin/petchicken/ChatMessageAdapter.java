package com.ustclin.petchicken;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustclin.petchicken.bean.ChatMessage;
import com.ustclin.petchicken.detail.DetailActivity;
import com.ustclin.petchicken.utils.MyDateUtils;
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
	
	public ChatMessageAdapter(Context context, List<ChatMessage> datas)
	{
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
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
		System.out.println("current.getDate() "+ msg_current.getDate());
		System.out.println("before.getDate() "+ msg_before.getDate());
		if (MyDateUtils.isLarger(msg_current.getDate(), msg_before.getDate(),duration)
				&& msg_current.getType().equals(ChatMessage.MESSAGE_IN)) {
			System.out.println("this is getItemViewType : TYPE_1");
			return TYPE_1;
		}
		else if (!MyDateUtils.isLarger(msg_current.getDate(),msg_before.getDate(), duration)
				&& msg_current.getType().equals(ChatMessage.MESSAGE_IN)) {
			System.out.println("this is getItemViewType : TYPE_2");
			return TYPE_2;
		}
		if (MyDateUtils.isLarger(msg_current.getDate(), msg_before.getDate(),duration)
				&& msg_current.getType().equals(ChatMessage.MESSAGE_OUT)) {
			System.out.println("this is getItemViewType : TYPE_3");
			return TYPE_3;
		}
		else if (!MyDateUtils.isLarger(msg_current.getDate(),msg_before.getDate(), duration)
				&& msg_current.getType().equals(ChatMessage.MESSAGE_OUT)) {
			System.out.println("this is getItemViewType : TYPE_4");
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
				viewHolder.createDate = (TextView) convertView
						.findViewById(R.id.chat_from_createDate);
				viewHolder.createDate.setText(chatMessage.getDate());
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_from_content);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.chat_from_icon);
				viewHolder.icon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("type", "pet");
						intent.setClass(mContext, DetailActivity.class);
						mContext.startActivity(intent);
					}
				});
				convertView.setTag(viewHolder);
				break;
			case TYPE_2:
				// in : without date
				convertView = mInflater.inflate(R.layout.main_chat_from_msg,
						parent, false);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_from_content);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.chat_from_icon);
				viewHolder.icon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("type", "pet");
						intent.setClass(mContext, DetailActivity.class);
						mContext.startActivity(intent);
					}
				});
				convertView.setTag(viewHolder);
				break;
			case TYPE_3:
				// out : with date
				convertView = mInflater.inflate(R.layout.main_chat_send_msg,
						null);
				viewHolder.createDate = (TextView) convertView
						.findViewById(R.id.chat_send_createDate);
				viewHolder.createDate.setText(chatMessage.getDate());
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_send_content);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.chat_send_icon);
				viewHolder.icon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
                        intent.putExtra("type", "master");
						intent.setClass(mContext, DetailActivity.class);
						mContext.startActivity(intent);
					}
				});
				convertView.setTag(viewHolder);
				break;
			case TYPE_4:
				// out : without date
				convertView = mInflater.inflate(R.layout.main_chat_send_msg,
						null);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_send_content);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.chat_send_icon);
				viewHolder.icon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
                        intent.putExtra("type", "master");
						intent.setClass(mContext, DetailActivity.class);
						mContext.startActivity(intent);
					}
				});
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
		System.out.println("get view : "+position);
		return convertView;
	}

	private class ViewHolder
	{
		public TextView createDate;
		public TextView name;
		public TextView content;
		public ImageView icon;
	}

}
