package com.ustclin.petchicken.utils;

import com.google.gson.Gson;
import com.ustclin.petchicken.RobotApp;
import com.ustclin.petchicken.bean.ChatMessage;
import com.ustclin.petchicken.bean.CommonException;
import com.ustclin.petchicken.bean.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpUtils {
    public static String API_KEY = "b93b2d9c05a91658145d2431f3f18168";
    private static String URL = "http://www.tuling123.com/openapi/api";

    /**
     * 发送一个消息，并得到返回的消息
     *
     * @param msg
     * @return
     */
    public static ChatMessage sendMsg(String msg) {
        ChatMessage message = new ChatMessage();
        String url = setParams(msg);
        String res = doGet(url);
        Gson gson = new Gson();
        Result result = gson.fromJson(res, Result.class);

        if (result.getCode() > 400000 || result.getText() == null
                || result.getText().trim().equals("")) {
            message.setMsg("主人，我的爸爸还没有开发出这个功能呢，你问我，我也很为难呢。。");
        } else {
            String responseStr = result.getText().replace(Constant.XHJ, RobotApp.gPetName);
            message.setMsg(responseStr);
        }
        message.setType(ChatMessage.MESSAGE_IN);
        message.setDate(MyDateUtils.getDate());

        return message;
    }

    /**
     * 拼接Url
     *
     * @param msg
     * @return
     */
    private static String setParams(String msg) {
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL + "?key=" + API_KEY + "&info=" + msg;
    }

    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @return
     */
    private static String doGet(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new CommonException("主人，我找不到服务器啦~");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("主人，我找不到服务器啦~");
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }

    }

}
