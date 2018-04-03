package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/4/11.
 */

public class HelpTitleEntity  implements Serializable {

    /**
     * acid : 46
     * alias : MoblieChangJianWenTi
     * is_checked : 1
     * name : 常见问题
     */
    private String acid;
    private String alias;
    private int is_checked;
    private String name;

    public HelpTitleEntity(){

    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setIs_checked(int is_checked) {
        this.is_checked = is_checked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcid() {
        return acid;
    }

    public String getAlias() {
        return alias;
    }

    public int getIs_checked() {
        return is_checked;
    }

    public String getName() {
        return name;
    }
}
