//package com.ustclin.petchicken.voice;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//public class MainActivity extends Activity {
//
//
//    private Context mContext;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext = this;
//        setContentView(R.layout.main);
//        Button btn1 = (Button) findViewById(R.id.btn1);
//        Button btn2 = (Button) findViewById(R.id.btn2);
//
//        if (btn1 == null || btn2 == null) {
//            return;
//        }
//
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext, TtsDemo.class);
//                startActivity(intent);
//            }
//        });
//
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext, IatDemo.class);
//                startActivity(intent);
//            }
//        });
//    }
//}
