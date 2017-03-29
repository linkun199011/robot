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
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, "wx0455a8eedb2a8159", false);
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
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "share success");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                Toast.makeText(this, "分享取消", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "share cancel");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                Toast.makeText(this, "分享拒绝", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "share refuse");
                break;
        }
    }
}
