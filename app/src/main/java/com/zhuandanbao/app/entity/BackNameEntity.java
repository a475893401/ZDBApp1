package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/14.
 */

public class BackNameEntity implements Serializable {

    /**
     * code : ICBC
     * logo : http://www.zhuandan.com/attachment/bank_ico/icbc.png
     * name : 中国工商银行
     */

    private String code;
    private String logo;
    private String name;
    private boolean isCheck;


    public BackNameEntity(){

    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
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
}
