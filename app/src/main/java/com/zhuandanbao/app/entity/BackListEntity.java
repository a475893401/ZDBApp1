package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/14.
 */

public class BackListEntity implements Serializable{

    /**
     * account : 袁晓文
     * bank_card_num : 55963555333
     * bank_full_name : 测试
     * bcid : 6140
     * city_name : 呼和浩特
     * code : ICBC
     * logo : http://www.zhuandan.com/attachment/bank_ico/icbc.png
     * name : 中国工商银行
     * phone : 95588
     * province_name : 内蒙古
     */
    private String account;
    private String bank_card_num;
    private String bank_full_name;
    private String bcid;
    private String city_name;
    private String code;
    private String logo;
    private String name;
    private String phone;
    private String province_name;
    private boolean isCheck;

    public BackListEntity(){

    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank_card_num() {
        return bank_card_num;
    }

    public void setBank_card_num(String bank_card_num) {
        this.bank_card_num = bank_card_num;
    }

    public String getBank_full_name() {
        return bank_full_name;
    }

    public void setBank_full_name(String bank_full_name) {
        this.bank_full_name = bank_full_name;
    }

    public String getBcid() {
        return bcid;
    }

    public void setBcid(String bcid) {
        this.bcid = bcid;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }
}
