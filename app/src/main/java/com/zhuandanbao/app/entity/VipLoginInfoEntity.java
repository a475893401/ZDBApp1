package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/20.
 */

public class VipLoginInfoEntity implements Serializable {

    public VipLoginInfoEntity() {

    }

    private String fadan_stroe_num;
    private String is_set_paypass;
    private String jiedan_chengjiao_num;
    private String jiedan_stroe_num;
    private String address; private String areainfo; private String assure_credit;
    private String audit_state;// -1认证失败  0 未提交  1  已提交  2 成功
    private String back_audit_msg; private String business_type; private String child_uid; private String child_username;
    private String city; private String county; private String credit; private String email;
    private String enable; private String enable_reason; private String introduce; private String is_audit;
    private String is_claim; private String is_setContactUser; //1設置 2未設置
    private String is_setbaseinfo;// 1 設置  2未設置
    private String last_login_time;
    private String loginuser; private String map_geo; private String mobile; private String openid;
    private String phone;
    private String province;
    private String qq;
    private String reg_time;
    private String sex;
    private String shop_auth_info;
    private String shop_cat_id;
    private String shop_logo;
    private String shop_name;
    private String shop_type;
    private String shopkeeper;
    private String sid;
    private String stype;//0 沒有設置 1 发单 2 接单  3发单和接单
    private String umeng_token;
    private String update_time;
    private String yingye_state;

    public String getAddress() {
        return address;
    }

    public String getAreainfo() {
        return areainfo;
    }

    public String getAssure_credit() {
        return assure_credit;
    }

    public String getAudit_state() {
        return audit_state;
    }

    public String getBack_audit_msg() {
        return back_audit_msg;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public String getChild_uid() {
        return child_uid;
    }

    public String getChild_username() {
        return child_username;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getCredit() {
        return credit;
    }

    public String getEmail() {
        return email;
    }

    public String getEnable() {
        return enable;
    }

    public String getEnable_reason() {
        return enable_reason;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getIs_audit() {
        return is_audit;
    }

    public String getIs_claim() {
        return is_claim;
    }

    public String getIs_setbaseinfo() {
        return is_setbaseinfo;
    }

    public String getIs_setContactUser() {
        return is_setContactUser;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public String getLoginuser() {
        return loginuser;
    }

    public String getMap_geo() {
        return map_geo;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOpenid() {
        return openid;
    }

    public String getPhone() {
        return phone;
    }

    public String getProvince() {
        return province;
    }

    public String getQq() {
        return qq;
    }

    public String getReg_time() {
        return reg_time;
    }

    public String getSex() {
        return sex;
    }

    public String getShop_auth_info() {
        return shop_auth_info;
    }

    public String getShop_cat_id() {
        return shop_cat_id;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getShop_type() {
        return shop_type;
    }

    public String getShopkeeper() {
        return shopkeeper;
    }

    public String getSid() {
        return sid;
    }

    public String getStype() {
        return stype;
    }

    public String getUmeng_token() {
        return umeng_token;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getYingye_state() {
        return yingye_state;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAreainfo(String areainfo) {
        this.areainfo = areainfo;
    }

    public void setAssure_credit(String assure_credit) {
        this.assure_credit = assure_credit;
    }

    public void setAudit_state(String audit_state) {
        this.audit_state = audit_state;
    }

    public void setBack_audit_msg(String back_audit_msg) {
        this.back_audit_msg = back_audit_msg;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public void setChild_uid(String child_uid) {
        this.child_uid = child_uid;
    }

    public void setChild_username(String child_username) {
        this.child_username = child_username;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public void setEnable_reason(String enable_reason) {
        this.enable_reason = enable_reason;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setIs_audit(String is_audit) {
        this.is_audit = is_audit;
    }

    public void setIs_claim(String is_claim) {
        this.is_claim = is_claim;
    }

    public void setIs_setbaseinfo(String is_setbaseinfo) {
        this.is_setbaseinfo = is_setbaseinfo;
    }

    public void setIs_setContactUser(String is_setContactUser) {
        this.is_setContactUser = is_setContactUser;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public void setLoginuser(String loginuser) {
        this.loginuser = loginuser;
    }

    public void setMap_geo(String map_geo) {
        this.map_geo = map_geo;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setQq(String qq) {
        this.qq = qq;

    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setShop_auth_info(String shop_auth_info) {
        this.shop_auth_info = shop_auth_info;
    }

    public void setShop_cat_id(String shop_cat_id) {
        this.shop_cat_id = shop_cat_id;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public void setShopkeeper(String shopkeeper) {
        this.shopkeeper = shopkeeper;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public void setUmeng_token(String umeng_token) {
        this.umeng_token = umeng_token;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setYingye_state(String yingye_state) {
        this.yingye_state = yingye_state;
    }

    public void setFadan_stroe_num(String fadan_stroe_num) {
        this.fadan_stroe_num = fadan_stroe_num;
    }

    public void setIs_set_paypass(String is_set_paypass) {
        this.is_set_paypass = is_set_paypass;
    }

    public void setJiedan_chengjiao_num(String jiedan_chengjiao_num) {
        this.jiedan_chengjiao_num = jiedan_chengjiao_num;
    }

    public void setJiedan_stroe_num(String jiedan_stroe_num) {
        this.jiedan_stroe_num = jiedan_stroe_num;
    }

    public String getFadan_stroe_num() {
        return fadan_stroe_num;
    }

    public String getIs_set_paypass() {
        return is_set_paypass;
    }

    public String getJiedan_chengjiao_num() {
        return jiedan_chengjiao_num;
    }

    public String getJiedan_stroe_num() {
        return jiedan_stroe_num;
    }
}

