package com.ustclin.petchicken.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ustclin.petchicken.bean.ChatMessage;
import com.ustclin.petchicken.bean.CustomConverBean;
import com.ustclin.petchicken.utils.MyDateUtils;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2017/1/9:21:58
 * description
 */
public class CustomConverDAO {
    private SQLiteDBHelper helper;

    public CustomConverDAO(Context context) {
        helper = new SQLiteDBHelper(context);
    }

    public void add(CustomConverBean customConverBean) {
        SQLiteDatabase db = helper.getWritableDatabase();
        // 为了防止SQL注入，输入的参数用?来占位符来指定
        String addSql = "insert into custom_conversation (pet_content, master_content ) values (?,?)";
        db.execSQL(addSql, new Object[]{customConverBean.getPetContent(), customConverBean.getMasterContent()});
        db.close();
    }

    public void delete(CustomConverBean customConverBean) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String delSql = "delete from custom_conversation where id = ?";
        db.execSQL(delSql, new Object[]{customConverBean.getId()});

        db.close();
    }

    public ArrayList<CustomConverBean> findAll() throws ParseException {
        ArrayList<CustomConverBean> cmList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String queryAllSql = "select * from custom_conversation";
        Cursor cursor = db.rawQuery(queryAllSql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String petContent = cursor.getString(cursor.getColumnIndex("pet_content"));
            String masterContent = cursor.getString(cursor.getColumnIndex("master_content"));
            CustomConverBean customConverBean = new CustomConverBean(id, petContent, masterContent);
            cmList.add(customConverBean);
        }
        cursor.close();
        db.close();
        return cmList;
    }

    /**
     * 通过匹配定制对话，如果查到，就直接返回。
     */
    public ChatMessage findMsg(String msg) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String querySql = "select pet_content from custom_conversation where master_content = '" + msg + "'";
        Cursor cursor = db.rawQuery(querySql, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToNext()) {
            String petContent = cursor.getString(cursor.getColumnIndex("pet_content"));
            System.out.print("==========" + petContent);
            ChatMessage message = new ChatMessage();
            message.setMsg(petContent);
            message.setType(ChatMessage.MESSAGE_IN);
            message.setDate(MyDateUtils.getDate());
            cursor.close();
            return message;
        } else {
            cursor.close();
            return null;
        }

    }

}
