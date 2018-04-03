package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/6.
 */

public class CurrShopInfoEntity implements Serializable {
    /**
     * assure_credit : 0
     * city : 820200
     * credit : 135
     * is_audit : 1
     * map_geo : 113.549088,22.198950
     * map_geo_lat : 22.198950
     * map_geo_lng : 113.549088
     * useBalance : 12.3
     */

    private String assure_credit;
    private String city;
    private String credit;
    private String is_audit;
    private String map_geo;
    private String map_geo_lat;
    private String map_geo_lng;
    private String useBalance;

    public CurrShopInfoEntity(){

    }

    public String getAssure_credit() {
        return assure_credit;
    }

    public void setAssure_credit(String assure_credit) {
        this.assure_credit = assure_credit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(String is_audit) {
        this.is_audit = is_audit;
    }

    public String getMap_geo() {
        return map_geo;
    }

    public void setMap_geo(String map_geo) {
        this.map_geo = map_geo;
    }

    public String getMap_geo_lat() {
        return map_geo_lat;
    }

    public void setMap_geo_lat(String map_geo_lat) {
        this.map_geo_lat = map_geo_lat;
    }

    public String getMap_geo_lng() {
        return map_geo_lng;
    }

    public void setMap_geo_lng(String map_geo_lng) {
        this.map_geo_lng = map_geo_lng;
    }

    public String getUseBalance() {
        return useBalance;
    }

    public void setUseBalance(String useBalance) {
        this.useBalance = useBalance;
    }
}
