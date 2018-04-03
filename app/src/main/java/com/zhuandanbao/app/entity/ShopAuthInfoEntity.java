package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/20.
 */

public class ShopAuthInfoEntity implements Serializable {

    /**
     * assure_credit : 0
     * auth_email : 0
     * auth_mobile : 1
     * auth_rel_name : 0
     * credit_money : 0.00
     */

    private String assure_credit;
    private String auth_email;
    private String auth_mobile;
    private String auth_rel_name;
    private String credit_money;

    public ShopAuthInfoEntity(){

    }

    public String getAssure_credit() {
        return assure_credit;
    }

    public void setAssure_credit(String assure_credit) {
        this.assure_credit = assure_credit;
    }

    public String getAuth_email() {
        return auth_email;
    }

    public void setAuth_email(String auth_email) {
        this.auth_email = auth_email;
    }

    public String getAuth_mobile() {
        return auth_mobile;
    }

    public void setAuth_mobile(String auth_mobile) {
        this.auth_mobile = auth_mobile;
    }

    public String getAuth_rel_name() {
        return auth_rel_name;
    }

    public void setAuth_rel_name(String auth_rel_name) {
        this.auth_rel_name = auth_rel_name;
    }

    public String getCredit_money() {
        return credit_money;
    }

    public void setCredit_money(String credit_money) {
        this.credit_money = credit_money;
    }
}
