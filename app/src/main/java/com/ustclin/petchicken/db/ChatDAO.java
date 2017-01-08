package com.ustclin.petchicken.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;

import com.ustclin.petchicken.bean.ChatMessage;

import java.util.ArrayList;

public class ChatDAO {
	private SQLiteDBHelper helper;

	public ChatDAO(Context context){
		helper = new SQLiteDBHelper(context);
	}
	/**
	 * 保存聊天记录
	 * @param cm
	 */
	public void add(ChatMessage cm){
		SQLiteDatabase db = helper.getWritableDatabase();
		// 为了防止SQL注入，输入的参数用?来占位符来指定
		String addSql = "insert into chat (chater , date , content ) values (?,?,?)";
		db.execSQL(addSql,new Object[] {cm.getType() , cm.getDate() , cm.getMsg()});
		System.out.println("getType : " + cm.getType());
		//System.out.println("getDate :" + cm.getDate().toString());
		db.close();
	}

	public void delete(ChatMessage cm) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String delSql = "delete from chat where id = ?";
		db.execSQL(delSql,new Object[] {cm.getId()});
		db.close();
	}
	/**
	 * 返回所有信息
	 * @return
	 * @throws 
	 */
	public ArrayList<ChatMessage> findAll() throws ParseException{
		ArrayList<ChatMessage> cmList = new ArrayList<ChatMessage>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String queryAllSql = "select * from chat";
		Cursor cursor = db.rawQuery(queryAllSql, null);
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String chater = cursor.getString(cursor.getColumnIndex("chater"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			ChatMessage cm = new ChatMessage( id ,chater , date , content );
			cmList.add(cm);
		}
		cursor.close();
		db.close();
		return cmList;
	}
	/**
	 * 返回20条信息
	 * @return
	 */
	public ArrayList<ChatMessage> find20(ChatMessage chatMes) {
		ArrayList<ChatMessage> cmList = new ArrayList<ChatMessage>();
		SQLiteDatabase db = helper.getReadableDatabase();
		System.out.println(">>>>>>chatDAO chatMes.getId() = "+chatMes.getId());
		System.out.println(">>>>>>chatDAO chatMes.getMes() = "+chatMes.getMsg());
		String queryAllSql = "select * from [chat] where id<"+chatMes.getId()+" order by id desc limit 20 ";
		Cursor cursor = db.rawQuery(queryAllSql, null);
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String chater = cursor.getString(cursor.getColumnIndex("chater"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			ChatMessage cm = new ChatMessage( id ,chater , date , content );
			cmList.add(cm);
		}
		cursor.close();
		db.close();
		return cmList;
	}
	/**
	 * 初始化，返回20条信息
	 * @return
	 */
	public ArrayList<ChatMessage> find20() {
		ArrayList<ChatMessage> cmList = new ArrayList<ChatMessage>();
		SQLiteDatabase db = helper.getReadableDatabase();
		// 取完之后要记得翻转顺序
		String queryAllSql = "select * from [chat] order by id desc limit 20 ";
		Cursor cursor = db.rawQuery(queryAllSql, null);
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String chater = cursor.getString(cursor.getColumnIndex("chater"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			ChatMessage cm = new ChatMessage( id ,chater , date , content );
			cmList.add(cm);
		}
		cursor.close();
		db.close();
		return cmList;
	}

}
