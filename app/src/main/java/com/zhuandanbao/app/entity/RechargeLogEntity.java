package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/14.
 */

public class RechargeLogEntity implements Serializable {

    /**
     * amount : 10.00
     * is_xuni : 0
     * other_sn : 10620170314104024109
     * pay_status_reamrk : 充值失败
     * paymoney : 0.00
     * payname : 支付宝[移动]
     * paystatus : 0
     * paytime :
     * paytime_format :
     * r_sn : 201703141489459226119
     * reason :
     * sendtime : 1489459228
     * sendtime_format : 2017-03-14 10:40:28
     */

    private String amount;
    private String is_xuni;
    private String other_sn;
    private String pay_status_reamrk;
    private String paymoney;
    private String payname;
    private String paystatus;
    private String paytime;
    private String paytime_format;
    private String r_sn;
    private String reason;
    private String sendtime;
    private String sendtime_format;

    public RechargeLogEntity(){

    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIs_xuni() {
        return is_xuni;
    }

    public void setIs_xuni(String is_xuni) {
        this.is_xuni = is_xuni;
    }

    public String getOther_sn() {
        return other_sn;
    }

    public void setOther_sn(String other_sn) {
        this.other_sn = other_sn;
    }

    public String getPay_status_reamrk() {
        return pay_status_reamrk;
    }

    public void setPay_status_reamrk(String pay_status_reamrk) {
        this.pay_status_reamrk = pay_status_reamrk;
    }

    public String getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(String paymoney) {
        this.paymoney = paymoney;
    }

    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPaytime_format() {
        return paytime_format;
    }

    public void setPaytime_format(String paytime_format) {
        this.paytime_format = paytime_format;
    }

    public String getR_sn() {
        return r_sn;
    }

    public void setR_sn(String r_sn) {
        this.r_sn = r_sn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getSendtime_format() {
        return sendtime_format;
    }

    public void setSendtime_format(String sendtime_format) {
        this.sendtime_format = sendtime_format;
    }
}
