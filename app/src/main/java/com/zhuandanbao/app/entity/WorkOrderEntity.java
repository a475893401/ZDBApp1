package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/15.
 */

public class WorkOrderEntity implements Serializable {

    /**
     * content : <p>测试</p>
     * issueType : 财务类/支付密码
     * sn : GD2104847
     * status : 0
     * status_remark : 待受理
     * time : 1489573244
     * title : 测试
     * wait_supplement : 0
     */

    private String content;
    private String issueType;
    private String sn;
    private String status;
    private String status_remark;
    private String time;
    private String title;
    private String wait_supplement;

    public WorkOrderEntity(){

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_remark() {
        return status_remark;
    }

    public void setStatus_remark(String status_remark) {
        this.status_remark = status_remark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWait_supplement() {
        return wait_supplement;
    }

    public void setWait_supplement(String wait_supplement) {
        this.wait_supplement = wait_supplement;
    }
}
