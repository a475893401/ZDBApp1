package com.zhuandanbao.app.mvp.callback;

import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.request.BaseRequest;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.utils.AppUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.Sign;

import java.util.HashMap;

/**
 * Created by BFTECH on 2017/2/21.
 */

public abstract class MBitmapDialogCallback extends BitmapCallback {

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.APPID_NAME, Constants.APPID);
        map.put(Constants.DEVICEID, PerfHelper.getStringData(Constants.DEVICEID));
        map.put(Constants.ACCESS_TOKEN, PerfHelper.getStringData(Constants.ACCESS_TOKEN));
        map.put("app_type", "1");
        map.put("app_version", AppUtils.getVersionName());
        map.put("format", "JSON");
        map.put(Constants.APP_SIGN, Sign.createSign(map));
        request.params(map);
    }
}
