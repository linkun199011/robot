package com.ustclin.petchicken.functiondisplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustclin.robot.R;

import java.lang.reflect.Field;

public class FunctionActivity extends Activity {
	//private Intent intent=getIntent();
	private String type;
	private TextView mTextViewTitle;
	private ImageView mImageView1;
	private ImageView mImageView2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		setContentView(R.layout.function_activity);
		Intent intent = getIntent();
		//this.intentValue =intent.getStringExtra("name_en");
		this.type = intent.getStringExtra("type");
		mTextViewTitle = (TextView) findViewById(R.id.title_function_name);
		mImageView1 = (ImageView) findViewById(R.id.iv_example1);
		mImageView2 = (ImageView) findViewById(R.id.iv_example2);
		int drawable1 = 0;
		int drawable2 = 0;
		try {
			Field field1 = R.drawable.class.getField(type+1);
			Field field2 = R.drawable.class.getField(type+2);
			drawable1 = field1.getInt(new R.drawable());
			drawable2 = field2.getInt(new R.drawable());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mImageView1.setImageResource(drawable1);
		mImageView2.setImageResource(drawable2);
		
		if(type.equals("function_joke")){
			mTextViewTitle.setText("讲笑话");
		}
		else if(type.equals("function_constellation")){
			mTextViewTitle.setText("查星座");
		}
		else if(type.equals("function_poem")){
			mTextViewTitle.setText("背诗词");
		}
		else if(type.equals("function_lyrics")){
			mTextViewTitle.setText("查歌词");
		}
		else if(type.equals("function_weather")){
			mTextViewTitle.setText("问天气");
		}
		else if(type.equals("function_translation")){
			mTextViewTitle.setText("让翻译");
		}
		else if(type.equals("function_baike")){
			mTextViewTitle.setText("查百科");
		}
		else if(type.equals("function_calculation")){
			mTextViewTitle.setText("计算器");
		}
		else
			mTextViewTitle.setText("举例");
		
		
		
	}
}
