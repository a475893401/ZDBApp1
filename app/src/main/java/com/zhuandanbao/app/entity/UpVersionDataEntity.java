package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/17.
 */

public class UpVersionDataEntity implements Serializable {

    public String app_type;
    public int version;
    public String version_min;
    public int is_upgrade;
    public String upgrade_path;
    public String upgrade_desc;

    public UpVersionDataEntity() {

    }
}
