package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/20.
 */

public class VipRegisterEntity implements Serializable {

    /**
     * loginuser : 15123166702
     * openid : 6c2e34dbab1835d3ea18bdb69532cb5f
     * shop_name :
     * sid : 26775
     */

    private String loginuser;
    private String openid;
    private String shop_name;
    private String sid;

    public VipRegisterEntity(){

    }

    public String getLoginuser() {
        return loginuser;
    }

    public void setLoginuser(String loginuser) {
        this.loginuser = loginuser;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
