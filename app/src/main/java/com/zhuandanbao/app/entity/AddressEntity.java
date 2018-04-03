package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2016/11/3.
 */
public class AddressEntity implements Serializable{

    public double startLat;
    public double startLon;
    public double endLat;
    public double endLon;
    public double centerLat;
    public double centerLon;
    public String startCity;
    public String distance;
    public String endCity;

    public AddressEntity(){

    }


}
