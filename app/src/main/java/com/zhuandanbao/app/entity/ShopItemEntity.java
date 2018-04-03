package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/17.
 */

public class ShopItemEntity implements Serializable{

    public ShopItemEntity(int img,String title,int msgNum,boolean isShowBack){
        this.img=img;
        this.title=title;
        this.msgNum=msgNum;
        this.isShowBack=isShowBack;
    }
    public ShopItemEntity(){
    }
    public int img;
    public String title;
    public int msgNum;
    public boolean isShowBack;

}
