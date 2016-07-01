package com.ustclin.petchicken.db;

import android.content.Context;
import android.os.Environment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImportDB {
	private final int BUFFER_SIZE = 409600;
	public static final String DB_NAME = "chat.db"; // 保存的数据库文件名
	public static final String PACKAGE_NAME = "com.ustclin.petchicken";// 工程包名
	public static final String DB_PATH = "/data"
	+ Environment.getDataDirectory().getAbsolutePath() + "/"
	+ PACKAGE_NAME + "/databases"; // 在手机里存放数据库的位置
	private Context context;
	public ImportDB(Context context) {
		this.context = context;
	}
	public void copyDatabase() {
		String dbfile = DB_PATH + "/" + DB_NAME;
		try {
			// 执行数据库导入
			System.out.println(">>>>>Starting to import the outside database......");
			InputStream is = this.context.getResources().getAssets().open("chat.db"); // 欲导入的数据库
			FileOutputStream fos = new FileOutputStream(dbfile);
			byte[] buffer = new byte[BUFFER_SIZE];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();// 关闭输出流
			is.close();// 关闭输入流
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}