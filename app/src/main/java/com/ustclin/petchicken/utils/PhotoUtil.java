package com.ustclin.petchicken.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ustclin.petchicken.customview.RectangleView;
import com.ustclin.robot.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PhotoUtil {

    public static void setPetRecHeader(Context context, RectangleView rectangleView) {
        // custom header
        if (PhotoUtil.isPetHeaderExists(context) && PhotoUtil.getPetHeaderPath(context)!=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtil.getPetHeaderPath(context));
            rectangleView.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
            rectangleView.setImageBitmap(bitmap);
        }
    }

    public static void setMasterRecHeader(Context context, RectangleView rectangleView) {
        // custom header
        if (PhotoUtil.isMasterHeaderExists(context) && PhotoUtil.getMasterHeaderPath(context)!=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtil.getMasterHeaderPath(context));
            rectangleView.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.my);
            rectangleView.setImageBitmap(bitmap);
        }
    }

    public static boolean isPetHeaderExists(Context activity) {
        File petHeaderFile = new File(getPetHeaderPath(activity));
        if (petHeaderFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMasterHeaderExists(Context activity) {
        File masterHeaderFile = new File(getMasterHeaderPath(activity));
        if (masterHeaderFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getPetHeaderPath(Context context) {
        return context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "petHeader.jpg";
    }

    public static String getMasterHeaderPath(Context context) {
        return context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "masterHeader.jpg";
    }


    /**
     * 生成一个随机的文件名
     *
     * @return
     */
    public static String getRandomFileName() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        rel = rel + new Random().nextInt(1000);
        return rel;
    }




}
