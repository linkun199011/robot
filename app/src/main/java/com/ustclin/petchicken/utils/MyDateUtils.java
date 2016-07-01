package com.ustclin.petchicken.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateUtils {
	/**
	 * 获取String类型的日期格式 ： eg：2015-2-6 18:08:11
	 * @return
	 */
	public static String getDate() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));
		String sec = String.valueOf(c.get(Calendar.SECOND));
		month = FormatTwoDigit(month);
		day = FormatTwoDigit(day);
		hour = FormatTwoDigit(hour);
		mins = FormatTwoDigit(mins);
		sec = FormatTwoDigit(sec);
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins + ":" + sec);
		return sbBuffer.toString();
	}
	/**
	 * 将Calendar类获取到的个位数转成两位数 2015-7-8 9:7:5 -> 2015-07-08 09:07:05
	 * @param str
	 * @return
	 */
	private static String FormatTwoDigit(String str) {
		// TODO Auto-generated method stub
		if(str.length() == 1){
			str = "0" + str;
		}
		return str;
	}
	/**
	 * 判断 date1 - date2 > duration ,是，则返回true，否，则返回false
	 * @param date1 起始时间："yyyy-MM-dd HH:mm:ss"
	 * @param date2 最后时间："yyyy-MM-dd HH:mm:ss"
	 * @param duration 单位是（秒）！
	 * @return 判断 date1 - date2 > duration ,是，则返回true，否，则返回false
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean isLarger(String date1 , String date2 , int duration){
		boolean isLarger = false;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long seconds = 0L;
		try{
		    Date d1 = df.parse(date1);
		    Date d2 = df.parse(date2);
		    long diff = d1.getTime() - d2.getTime();
		    //System.out.println("diff :" + diff);
		    seconds = diff / 1000; // 相差的秒
		    //System.out.println("seconds : "+ seconds);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// 大于duration秒 , 则置标志位为true
		if(seconds >= duration){
			isLarger = true;
		}
		//System.out.println("isLarger :" +isLarger);
		return isLarger;
	}
}
