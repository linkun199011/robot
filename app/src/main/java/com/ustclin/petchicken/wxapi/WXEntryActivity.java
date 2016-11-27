package com.ustclin.petchicken.wxapi;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2016/11/27:22:32
 * description
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.logging.LogManager;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, "这里替换第一步申请的APP_ID", false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onReq(BaseReq arg0) { }

    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG, "resp.errCode:" + resp.errCode + ",resp.errStr:" + resp.errStr);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                Log.e(TAG, "share success");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                Log.e(TAG, "share cancel");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                Log.e(TAG, "share refuse");
                break;
        }
    }
}
