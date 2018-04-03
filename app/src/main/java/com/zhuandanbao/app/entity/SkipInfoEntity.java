package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/20.
 */

public class SkipInfoEntity implements Serializable {

    public SkipInfoEntity() {
    }
    public boolean isPush;
    public int index;
    public String msg;
    public String mobile;
    public String pushId;
    public Object data;
    public String payCode;
    public int item;
    public String expressId;
    public String nvsTitle;
    public String contentTitle;
    public String hintText;
    public int imgId;
}
