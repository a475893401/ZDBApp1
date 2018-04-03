package com.zhuandanbao.app.callbackinterface;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by BFTECH on 2016/4/29.
 */
public class CallbackEntity implements Serializable {

    public int showMainFragment;
    public String msg;
    public boolean isMode;
    public String time;
    public String jieSid;
    public boolean isOther;

    public CallbackEntity() {
    }

    protected CallbackEntity(Parcel in) {
        showMainFragment = in.readInt();
        msg = in.readString();
        isMode = in.readByte() != 0;
        time = in.readString();
        jieSid = in.readString();
        isOther=in.readByte()!=0;
    }
}
