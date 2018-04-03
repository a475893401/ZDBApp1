package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/15.
 */

public class WorkOrderContentEntity implements Serializable {

    /**
     * attachment : ["http://img.zhuandan.com/attachment/Ticket/11405/56d79cd0e0cc7.png"]
     * content : <p>你们这个诚信保证金有什么用啊？为什么要加入啊？怎么加入啊？加入有什么好处呢？</p>
     * del_time :
     * email : jackean@foxmail.com
     * id : 3
     * is_del : 0
     * issueType : 财务类/诚信保证金
     * mobile : 18875008085
     * reply_id : 0
     * reply_time : 1457065168
     * reply_uid : 1
     * reply_user : 袁晓文
     * revoke_time : 1465976666
     * sid : 11405
     * sn : GD30nT
     * status : -1
     * status_remark : 已撤销
     * time : 1456970962
     * title : <p>你们这个诚信保证金有什么用啊？为什么要加入啊？怎么加入...
     * to_sid : 0
     * wait_supplement : 0
     */

    private String content;
    private String del_time;
    private String email;
    private String id;
    private String is_del;
    private String issueType;
    private String mobile;
    private String reply_id;
    private String reply_time;
    private String reply_uid;
    private String reply_user;
    private String revoke_time;
    private String sid;
    private String sn;
    private String status;
    private String status_remark;
    private String time;
    private String title;
    private String to_sid;
    private String wait_supplement;
    private String attachment;

    public WorkOrderContentEntity(){

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDel_time() {
        return del_time;
    }

    public void setDel_time(String del_time) {
        this.del_time = del_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getReply_uid() {
        return reply_uid;
    }

    public void setReply_uid(String reply_uid) {
        this.reply_uid = reply_uid;
    }

    public String getReply_user() {
        return reply_user;
    }

    public void setReply_user(String reply_user) {
        this.reply_user = reply_user;
    }

    public String getRevoke_time() {
        return revoke_time;
    }

    public void setRevoke_time(String revoke_time) {
        this.revoke_time = revoke_time;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
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

    public String getTo_sid() {
        return to_sid;
    }

    public void setTo_sid(String to_sid) {
        this.to_sid = to_sid;
    }

    public String getWait_supplement() {
        return wait_supplement;
    }

    public void setWait_supplement(String wait_supplement) {
        this.wait_supplement = wait_supplement;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
