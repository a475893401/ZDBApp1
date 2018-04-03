package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/15.
 */

public class SalesTypeEntity implements Serializable {

    /**
     * double_return : 0
     * full_return : 1
     * id : 17
     * name : [撤单]发单方申请撤单：订货人取消&订单信息有误&其它
     * part_return : 1
     * pid : 12
     * rtid : 17
     * sort : 11
     */

    private String double_return;
    private String full_return;
    private String id;
    private String name;
    private String part_return;
    private String pid;
    private String rtid;
    private String sort;

    public SalesTypeEntity(){

    }


    public String getDouble_return() {
        return double_return;
    }

    public void setDouble_return(String double_return) {
        this.double_return = double_return;
    }

    public String getFull_return() {
        return full_return;
    }

    public void setFull_return(String full_return) {
        this.full_return = full_return;
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

    public String getPart_return() {
        return part_return;
    }

    public void setPart_return(String part_return) {
        this.part_return = part_return;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
