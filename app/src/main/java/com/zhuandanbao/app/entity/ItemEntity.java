package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/21.
 */

public class ItemEntity implements Serializable {
    public ItemEntity() {
    }
    public ItemEntity(String name,String content) {
        this.name=name;
        this.content=content;
    }
    public int id;
    public String name;
    public String content;
}
