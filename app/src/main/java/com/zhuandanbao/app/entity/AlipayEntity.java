package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/13.
 */

public class AlipayEntity implements Serializable {

    /**
     * notify_url : http://pay.xiangsidi.com/notify_alipay
     * payConf : {}
     * pay_amount : 10
     * pay_code : alipay
     * pay_desc : 转单宝账户充值，订单号201703131489375420708
     * pay_sn : 10620170313112338293
     * subject : 转单宝账户充值，订单号201703131489375420708
     */

    private String notify_url;
    private String payConf;
    private String pay_amount;
    private String pay_code;
    private String pay_desc;
    private String pay_sn;
    private String subject;
    private String partner;
    private String rsa_private;

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getRsa_private() {
        return rsa_private;
    }

    public void setRsa_private(String rsa_private) {
        this.rsa_private = rsa_private;
    }

    public AlipayEntity() {

    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getPayConf() {
        return payConf;
    }

    public void setPayConf(String payConf) {
        this.payConf = payConf;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getPay_desc() {
        return pay_desc;
    }

    public void setPay_desc(String pay_desc) {
        this.pay_desc = pay_desc;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
