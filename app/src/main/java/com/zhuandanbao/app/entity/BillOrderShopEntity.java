package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/9.
 */

public class BillOrderShopEntity implements Serializable {

    public BillOrderShopEntity (){}

    private String sid;
    private String shop_name;
    private String shop_logo;
    private String business_type;
    private String shopkeeper;
    private String mobile;
    private String phone;
    private String areainfo;
    private String address;
    private String is_audit;
    private String assure_credit;
    private String credit;
    private String jdjdan;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(String shopkeeper) {
        this.shopkeeper = shopkeeper;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAreainfo() {
        return areainfo;
    }

    public void setAreainfo(String areainfo) {
        this.areainfo = areainfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(String is_audit) {
        this.is_audit = is_audit;
    }

    public String getAssure_credit() {
        return assure_credit;
    }

    public void setAssure_credit(String assure_credit) {
        this.assure_credit = assure_credit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getJdjdan() {
        return jdjdan;
    }

    public void setJdjdan(String jdjdan) {
        this.jdjdan = jdjdan;
    }
}
