package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/6.
 */

public class SignInfoEntity implements Serializable {

    private String id;
    private String order_id;
    private String osid;
    private String real_goods;
    private String sign_client;
    private String sign_client_ver;
    private String sign_time;
    private String sign_time_format;
    private String sign_type;
    private String sign_user;
    private String sign_voucher;

    public SignInfoEntity(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOsid() {
        return osid;
    }

    public void setOsid(String osid) {
        this.osid = osid;
    }

    public String getReal_goods() {
        return real_goods;
    }

    public void setReal_goods(String real_goods) {
        this.real_goods = real_goods;
    }

    public String getSign_client() {
        return sign_client;
    }

    public void setSign_client(String sign_client) {
        this.sign_client = sign_client;
    }

    public String getSign_client_ver() {
        return sign_client_ver;
    }

    public void setSign_client_ver(String sign_client_ver) {
        this.sign_client_ver = sign_client_ver;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getSign_time_format() {
        return sign_time_format;
    }

    public void setSign_time_format(String sign_time_format) {
        this.sign_time_format = sign_time_format;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign_user() {
        return sign_user;
    }

    public void setSign_user(String sign_user) {
        this.sign_user = sign_user;
    }

    public String getSign_voucher() {
        return sign_voucher;
    }

    public void setSign_voucher(String sign_voucher) {
        this.sign_voucher = sign_voucher;
    }
}
