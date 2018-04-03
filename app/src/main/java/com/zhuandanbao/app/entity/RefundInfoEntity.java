package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/6.
 */

public class RefundInfoEntity implements Serializable {

    private String accept_time;
    private String amount;
    private String apply_reason;
    private String apply_sid;
    private String apply_time;
    private String apply_time_format;
    private String apply_user;
    private String id;
    private String osn;
    private String reason;
    private String sid;
    private String status;

    public RefundInfoEntity(){

    }


    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApply_reason() {
        return apply_reason;
    }

    public void setApply_reason(String apply_reason) {
        this.apply_reason = apply_reason;
    }

    public String getApply_sid() {
        return apply_sid;
    }

    public void setApply_sid(String apply_sid) {
        this.apply_sid = apply_sid;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getApply_time_format() {
        return apply_time_format;
    }

    public void setApply_time_format(String apply_time_format) {
        this.apply_time_format = apply_time_format;
    }

    public String getApply_user() {
        return apply_user;
    }

    public void setApply_user(String apply_user) {
        this.apply_user = apply_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOsn() {
        return osn;
    }

    public void setOsn(String osn) {
        this.osn = osn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
