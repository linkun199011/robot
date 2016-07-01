package com.ustclin.petchicken;

import android.test.AndroidTestCase;

import com.ustclin.petchicken.utils.HttpUtils;

public class Test extends AndroidTestCase
{
	public void testSendMsg()
	{
		HttpUtils.sendMsg("西斜七路堵车吗");
		HttpUtils.sendMsg("你好");
		HttpUtils.sendMsg("讲个笑话");
		HttpUtils.sendMsg("新浪体育");
	}

}
