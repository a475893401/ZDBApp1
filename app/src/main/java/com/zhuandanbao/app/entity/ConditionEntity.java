package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/6.
 */

public class ConditionEntity implements Serializable {
    private String desc;
    private int id;

    public ConditionEntity(){

    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
