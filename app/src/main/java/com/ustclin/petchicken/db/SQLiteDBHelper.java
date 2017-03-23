package com.ustclin.petchicken.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.logging.Logger;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    private static final String DBFILENAME = "chat.db";
    private static int db_version = 3;
    private final String CHAT = "chat";// 聊天记录
    private final String CUSTOM_CONVERSATION = "custom_conversation";// 自定义聊天

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
        createAllTable(db);
//        createFirstVersionTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DBHelper", "update DB from " + oldVersion + " to " + newVersion);
        if (newVersion > oldVersion) {
            dropAllTable(db);
            createAllTable(db);
        }
    }

    public void dropAllTable(SQLiteDatabase db) {
        String dropChatTable = "DROP TABLE IF EXISTS " + CHAT;
        db.execSQL(dropChatTable);
        String dropCustomTable = "DROP TABLE IF EXISTS " + CUSTOM_CONVERSATION;
        db.execSQL(dropCustomTable);
    }

    public void createFirstVersionTable(SQLiteDatabase db) {
        // chat history
        String sqlInitChat = "CREATE TABLE ["
                + CHAT
                + "] "
                + "( [id] integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + "   [chater] VARCHAR,  [date] VARCHAR,  "
                + "[content] VARCHAR);";
        db.execSQL(sqlInitChat);
    }

    public void createAllTable(SQLiteDatabase db) {
        // chat history
        String sqlInitChat = "CREATE TABLE ["
                + CHAT
                + "] "
                + "( [id] integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + "   [chater] VARCHAR,  [date] VARCHAR,  "
                + "[content] VARCHAR);";
        db.execSQL(sqlInitChat);
        // custom conversation
		String sqlInitCustomConver = "CREATE TABLE ["
				+ CUSTOM_CONVERSATION
				+ "] "
				+ "( [id] integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "   [pet_content] VARCHAR NOT NULL ,"
				+ "[master_content] VARCHAR NOT NULL);";
		db.execSQL(sqlInitCustomConver);
    }

}
