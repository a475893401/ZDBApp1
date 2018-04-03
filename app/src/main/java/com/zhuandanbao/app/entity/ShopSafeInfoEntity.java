package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/24.
 */
public class ShopSafeInfoEntity implements Serializable {

    /**
     * auth_email : 0
     * auth_license : 0
     * auth_mobile : 1
     * auth_rel_name : 0
     * is_set_paypass : 0
     */

    private String auth_email;
    private String auth_license;
    private String auth_mobile;
    private String auth_rel_name;
    private int is_set_paypass;

    public ShopSafeInfoEntity(){

    }

    public String getAuth_email() {
        return auth_email;
    }

    public void setAuth_email(String auth_email) {
        this.auth_email = auth_email;
    }

    public String getAuth_license() {
        return auth_license;
    }

    public void setAuth_license(String auth_license) {
        this.auth_license = auth_license;
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

    public int getIs_set_paypass() {
        return is_set_paypass;
    }

    public void setIs_set_paypass(int is_set_paypass) {
        this.is_set_paypass = is_set_paypass;
    }
}
