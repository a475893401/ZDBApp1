package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/13.
 */

public class RechargeEntity implements Serializable {

    public RechargeEntity(int img,String conetent) {
        this.img=img;
        this.conetent=conetent;

    }

    public int img;
    public String conetent;
}
