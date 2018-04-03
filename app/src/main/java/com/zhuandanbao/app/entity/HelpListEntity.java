package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/4/11.
 */

public class HelpListEntity  implements Serializable {

    /**
     * acid : 47
     * aid : 237
     * description :
     * time : 1491537372
     * title : [APP]接单方使用指南
     */

    private String acid;
    private String aid;
    private String description;
    private String time;
    private String title;

    public HelpListEntity(){

    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAcid() {
        return acid;
    }

    public String getAid() {
        return aid;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }
}
