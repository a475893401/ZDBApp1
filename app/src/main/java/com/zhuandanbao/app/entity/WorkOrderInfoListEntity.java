package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/15.
 */

public class WorkOrderInfoListEntity implements Serializable {


    /**
     * attachment : []
     * content : 啊实打实
     * id : 10
     * reply_id : 3
     * reply_time : 1457064778
     * reply_uid : 1
     * reply_user : 袁晓文
     * sid : 0
     * time : 1457064778
     * to_sid : 0
     * wait_supplement : 0
     */

    private String content;
    private String id;
    private String reply_id;
    private String reply_time;
    private String reply_uid;
    private String reply_user;
    private String sid;
    private String time;
    private String to_sid;
    private String wait_supplement;
    private String attachment;

    public WorkOrderInfoListEntity(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
