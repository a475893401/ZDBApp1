package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/6.
 */

public class InfoLogEntity implements Serializable {

    private String logistics;
    private String logs;
    private String time;
    private int type;
    private String user;
    private String order_data;

    public InfoLogEntity(){

    }

    public String getOrder_data() {
        return order_data;
    }

    public void setOrder_data(String order_data) {
        this.order_data = order_data;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
