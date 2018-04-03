package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/14.
 */

public class WithdrawLogEntity implements Serializable {

    /**
     * amount : 7.00
     * apply_sid : 11405
     * apply_sn : TX148100936314
     * apply_time : 1481009363
     * apply_time_format : 2016-12-06 15:29:23
     * apply_user : 袁晓文
     * bank_card : 52222112212121212121
     * bank_name : 中国工商银行
     * remarks : 账户余额不足
     * status : -1
     * status_reamrk : 交易失败
     */

    private String amount;
    private String apply_sid;
    private String apply_sn;
    private String apply_time;
    private String apply_time_format;
    private String apply_user;
    private String bank_card;
    private String bank_name;
    private String remarks;
    private String status;
    private String status_reamrk;

    public WithdrawLogEntity(){

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApply_sid() {
        return apply_sid;
    }

    public void setApply_sid(String apply_sid) {
        this.apply_sid = apply_sid;
    }

    public String getApply_sn() {
        return apply_sn;
    }

    public void setApply_sn(String apply_sn) {
        this.apply_sn = apply_sn;
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

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_reamrk() {
        return status_reamrk;
    }

    public void setStatus_reamrk(String status_reamrk) {
        this.status_reamrk = status_reamrk;
    }
}
