package com.ustclin.petchicken.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {
	private static final String DBFILENAME = "chat.db";
	private static int db_version = 1;
	private final String CHAT = "chat";// 数据库TABLE名称
	
	/**
	 * 数据库的构造方法，用来定义数据库的名称，数据库查询的结果集 ，数据库的版本等信息
	 * 
	 * @param context
	 */
	public SQLiteDBHelper(Context context) {
		super(context, DBFILENAME, null, db_version);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 数据库第一次被创建时候调用的方法 db即使被创建的数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlPlayer = "CREATE TABLE ["
				+ CHAT
				+ "] "
				+ "( [id] integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "   [chater] VARCHAR,  [date] VARCHAR,  "
				+ "[content] VARCHAR);";
		db.execSQL(sqlPlayer);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
