package com.zhuandanbao.app.entity;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by BFTECH on 2016/7/12.
 */
public class TypeEntity implements Serializable {

    public int id;
    public String name;

    public TypeEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected TypeEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }
}
