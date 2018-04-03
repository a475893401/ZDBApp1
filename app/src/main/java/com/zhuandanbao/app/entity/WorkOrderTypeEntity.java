package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/16.
 */

public class WorkOrderTypeEntity implements Serializable{
    /**
     * _child : []
     * custom : []
     * id : 1
     * name : 财务类
     * pid : 0
     * pinyin : CaiWuLei
     */
    private String id;
    private String name;
    private String pid;
    private String pinyin;
    private String item;
    private String custom;

    public WorkOrderTypeEntity(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
}
