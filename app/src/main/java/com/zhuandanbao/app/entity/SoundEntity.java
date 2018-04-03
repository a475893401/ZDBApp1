package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/17.
 */

public class SoundEntity  implements Serializable {

    public SoundEntity(String title,String status){
        this.title=title;
        this.status=status;
    }

    public String title;
    public String status;
}
